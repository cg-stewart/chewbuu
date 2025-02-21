package com.chewbuu.api.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "date_request_id", nullable = false)
    private DateRequest dateRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User matchedUser;

    @Column(nullable = false)
    private double matchScore;

    @Column(nullable = false)
    private int videoMessageCount = 0;

    @Column(nullable = false)
    private boolean chatEnabled = false;

    @Column(nullable = false)
    private boolean savedForLater = false;

    @Column(nullable = false)
    private boolean addedAsFriend = false;

    // Helper method to check if chat can be enabled
    @Transient
    public boolean canEnableChat() {
        return videoMessageCount >= 3;
    }
}
