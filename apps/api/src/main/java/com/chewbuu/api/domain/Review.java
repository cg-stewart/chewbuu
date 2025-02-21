package com.chewbuu.api.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "date_request_id", nullable = false)
    private DateRequest dateRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_user_id", nullable = false)
    private User reviewedUser;

    @Column(nullable = false)
    private String venueId;

    @Column(nullable = false)
    private int venueRating;

    // Five categories for person rating (1-5 scale)
    @Column(nullable = false)
    private int punctualityRating;

    @Column(nullable = false)
    private int personalityRating;

    @Column(nullable = false)
    private int appearanceRating;

    @Column(nullable = false)
    private int conversationRating;

    @Column(nullable = false)
    private int respectRating;

    @ElementCollection
    @CollectionTable(name = "review_media", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "media_url")
    private List<String> mediaUrls = new ArrayList<>();

    @Column(length = 1000)
    private String comment;

    // Helper method to calculate average person rating
    @Transient
    public double getAveragePersonRating() {
        return (punctualityRating + personalityRating + appearanceRating +
                conversationRating + respectRating) / 5.0;
    }
}
