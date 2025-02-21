package com.chewbuu.api.service;

import com.chewbuu.api.domain.DateRequest;
import com.chewbuu.api.domain.Match;
import com.chewbuu.api.domain.User;

import java.util.List;
import java.util.UUID;

public interface MatchingService {
    List<Match> findMatches(DateRequest dateRequest, int maxResults);

    double calculateMatchScore(DateRequest dateRequest, User potentialMatch);

    void processVideoMessage(UUID matchId);

    void enableChat(UUID matchId);

    void saveForLater(UUID matchId);

    void addAsFriend(UUID matchId);
}
