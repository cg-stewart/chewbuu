package com.chewbuu.api.service.impl;

import com.chewbuu.api.domain.User;
import com.chewbuu.api.domain.enums.SubscriptionTier;
import com.chewbuu.api.exception.ChewbuuException;
import com.chewbuu.api.repository.DateRequestRepository;
import com.chewbuu.api.repository.UserRepository;
import com.chewbuu.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DateRequestRepository dateRequestRepository;

    @Override
    public User createUser(User user) {
        if (user.getAge() < 19) {
            throw new ChewbuuException("Users must be 19 or older to join Chewbuu", HttpStatus.BAD_REQUEST);
        }
        
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ChewbuuException("Username already exists", HttpStatus.CONFLICT);
        }
        
        return userRepository.save(user);
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ChewbuuException("User not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new ChewbuuException("User not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public List<User> findPotentialMatches(User initiator, int maxResults) {
        // Define age range based on initiator's age and subscription tier
        int initiatorAge = initiator.getAge();
        Integer minAge = null;
        Integer maxAge = null;

        if (initiator.getSubscriptionTier() != SubscriptionTier.SUGAR) {
            if (initiatorAge < 21) {
                // 19-20 year olds can only match with 19-21
                minAge = 19;
                maxAge = 21;
            } else {
                // 21+ can only match with 21+
                minAge = 21;
                maxAge = null; // No upper limit
            }
        }
        // SUGAR tier has no age restrictions except minimum age of 19

        List<User> potentialMatches = userRepository.findPotentialMatches(
            minAge,
            maxAge,
            initiator.getLocation()
        );

        return potentialMatches.stream()
            .filter(target -> canMatchWith(initiator, target))
            .limit(maxResults)
            .toList();
    }

    @Override
    public boolean canInitiateDateRequest(User user) {
        if (user.getSubscriptionTier() == SubscriptionTier.SINGLE) {
            LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
            LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
            
            int todayRequests = dateRequestRepository.countActiveDateRequestsForToday(
                user, startOfDay, endOfDay);
            
            return todayRequests < 2; // Free tier limited to 2 dates per day
        }
        return true; // Other tiers have unlimited date requests
    }

    @Override
    public boolean canMatchWith(User initiator, User target) {
        if (!target.isVerified()) {
            return false;
        }

        // Check if either user is SUGAR tier (no restrictions)
        if (initiator.getSubscriptionTier() == SubscriptionTier.SUGAR ||
            target.getSubscriptionTier() == SubscriptionTier.SUGAR) {
            return true;
        }

        int initiatorAge = initiator.getAge();
        int targetAge = target.getAge();

        // Age matching rules
        if (initiatorAge < 21) {
            // 19-20 year olds can only match with 19-21
            return targetAge >= 19 && targetAge <= 21;
        } else {
            // 21+ can only match with 21+
            return targetAge >= 21;
        }
    }

    @Override
    public void updateSubscriptionTier(UUID userId, SubscriptionTier tier) {
        User user = getUserById(userId);
        user.setSubscriptionTier(tier);
        userRepository.save(user);
    }
}
