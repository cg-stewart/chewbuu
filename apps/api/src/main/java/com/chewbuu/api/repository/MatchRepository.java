package com.chewbuu.api.repository;

import com.chewbuu.api.domain.DateRequest;
import com.chewbuu.api.domain.Match;
import com.chewbuu.api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MatchRepository extends JpaRepository<Match, UUID> {
    List<Match> findByDateRequestOrderByMatchScoreDesc(DateRequest dateRequest);
    
    Optional<Match> findByDateRequestAndMatchedUser(DateRequest dateRequest, User matchedUser);
    
    @Query("SELECT m FROM Match m WHERE m.matchedUser = :user AND m.savedForLater = true")
    List<Match> findSavedMatches(@Param("user") User user);

    @Query("SELECT m FROM Match m WHERE m.matchedUser = :user AND m.addedAsFriend = true")
    List<Match> findFriends(@Param("user") User user);
}
