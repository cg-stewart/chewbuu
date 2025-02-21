package com.chewbuu.api.service.impl;

import com.chewbuu.api.domain.DateRequest;
import com.chewbuu.api.domain.Match;
import com.chewbuu.api.domain.User;
import com.chewbuu.api.exception.ChewbuuException;
import com.chewbuu.api.repository.MatchRepository;
import com.chewbuu.api.service.MatchingService;
import com.chewbuu.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchingServiceImpl implements MatchingService {

    private final UserService userService;
    private final MatchRepository matchRepository;

    private static final double WEIGHT_RATING = 0.3;
    private static final double WEIGHT_INTERESTS = 0.3;
    private static final double WEIGHT_RELIABILITY = 0.2;
    private static final double WEIGHT_ACTIVITY_HISTORY = 0.2;

    @Override
    public List<Match> findMatches(DateRequest dateRequest, int maxResults) {
        User initiator = dateRequest.getInitiator();

        // Get potential matches based on age and location
        List<User> potentialMatches = userService.findPotentialMatches(initiator, 100);

        // Calculate scores and create matches
        List<Match> matches = potentialMatches.stream()
                .map(user -> {
                    double score = calculateMatchScore(dateRequest, user);
                    return Match.builder()
                            .dateRequest(dateRequest)
                            .matchedUser(user)
                            .matchScore(score)
                            .build();
                })
                .sorted(Comparator.comparingDouble(Match::getMatchScore).reversed())
                .limit(maxResults)
                .collect(Collectors.toList());

        return matchRepository.saveAll(matches);
    }

    @Override
    public double calculateMatchScore(DateRequest dateRequest, User potentialMatch) {
        User initiator = dateRequest.getInitiator();

        // 1. Rating Score (0-1)
        double ratingScore = calculateRatingScore(potentialMatch);

        // 2. Interest Compatibility (0-1)
        double interestScore = calculateInterestScore(initiator, potentialMatch);

        // 3. Reliability Score (based on flake/late count) (0-1)
        double reliabilityScore = calculateReliabilityScore(potentialMatch);

        // 4. Activity History Score (0-1)
        double activityScore = calculateActivityHistoryScore(dateRequest, potentialMatch);

        // Calculate weighted average
        return (ratingScore * WEIGHT_RATING) +
                (interestScore * WEIGHT_INTERESTS) +
                (reliabilityScore * WEIGHT_RELIABILITY) +
                (activityScore * WEIGHT_ACTIVITY_HISTORY);
    }

    private double calculateRatingScore(User user) {
        // Convert 5-star rating to 0-1 scale
        return user.getAverageRating() / 5.0;
    }

    private double calculateInterestScore(User initiator, User potentialMatch) {
        Set<String> initiatorInterests = initiator.getInterests();
        Set<String> matchInterests = potentialMatch.getInterests();

        if (initiatorInterests.isEmpty() || matchInterests.isEmpty()) {
            return 0.5; // Neutral score if no interests
        }

        // Calculate Jaccard similarity (intersection over union)
        Set<String> intersection = new HashSet<>(initiatorInterests);
        intersection.retainAll(matchInterests);

        Set<String> union = new HashSet<>(initiatorInterests);
        union.addAll(matchInterests);

        return (double) intersection.size() / union.size();
    }

    private double calculateReliabilityScore(User user) {
        // Penalize for flakes and late arrivals
        int totalIncidents = user.getFlakeCount() + user.getLateCount();

        // Exponential decay function: 1 * e^(-0.5 * incidents)
        return Math.exp(-0.5 * totalIncidents);
    }

    private double calculateActivityHistoryScore(DateRequest dateRequest, User potentialMatch) {
        // Check if user has been to the selected venues before
        Set<String> selectedVenues = dateRequest.getSelectedVenues();
        // TODO: Implement venue history check
        // For now, return neutral score
        return 0.5;
    }

    @Override
    public void processVideoMessage(UUID matchId) {
        Match match = getMatch(matchId);
        match.setVideoMessageCount(match.getVideoMessageCount() + 1);

        // Enable chat if 3 video messages have been exchanged
        if (match.canEnableChat()) {
            match.setChatEnabled(true);
        }

        matchRepository.save(match);
    }

    @Override
    public void enableChat(UUID matchId) {
        Match match = getMatch(matchId);
        if (!match.canEnableChat()) {
            throw new ChewbuuException(
                    "Must exchange 3 video messages before enabling chat",
                    HttpStatus.BAD_REQUEST);
        }
        match.setChatEnabled(true);
        matchRepository.save(match);
    }

    @Override
    public void saveForLater(UUID matchId) {
        Match match = getMatch(matchId);
        match.setSavedForLater(true);
        matchRepository.save(match);
    }

    @Override
    public void addAsFriend(UUID matchId) {
        Match match = getMatch(matchId);
        match.setAddedAsFriend(true);
        matchRepository.save(match);
    }

    private Match getMatch(UUID matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> new ChewbuuException("Match not found", HttpStatus.NOT_FOUND));
    }
}
