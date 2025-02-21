package com.chewbuu.api.repository;

import com.chewbuu.api.domain.DateRequest;
import com.chewbuu.api.domain.Review;
import com.chewbuu.api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByReviewedUser(User reviewedUser);
    
    Optional<Review> findByDateRequestAndReviewer(DateRequest dateRequest, User reviewer);
    
    @Query("SELECT AVG(r.venueRating) FROM Review r WHERE r.venueId = :venueId")
    Double getAverageVenueRating(@Param("venueId") String venueId);

    @Query("SELECT AVG(r.punctualityRating + r.personalityRating + r.appearanceRating + " +
           "r.conversationRating + r.respectRating) / 5.0 FROM Review r WHERE r.reviewedUser = :user")
    Double getAverageUserRating(@Param("user") User user);
}
