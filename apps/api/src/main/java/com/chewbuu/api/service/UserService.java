package com.chewbuu.api.service;

import com.chewbuu.api.domain.User;
import com.chewbuu.api.domain.enums.SubscriptionTier;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(User user);

    User getUserById(UUID id);

    User getUserByUsername(String username);

    List<User> findPotentialMatches(User initiator, int maxResults);

    boolean canInitiateDateRequest(User user);

    boolean canMatchWith(User initiator, User target);

    void updateSubscriptionTier(UUID userId, SubscriptionTier tier);
}
