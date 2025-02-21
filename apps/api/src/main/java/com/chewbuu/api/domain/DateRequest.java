package com.chewbuu.api.domain;

import com.chewbuu.api.domain.enums.ActivityType;
import com.chewbuu.api.domain.enums.DateRequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "date_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DateRequest extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityType activityType;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private int maxGroupSize;

    @ElementCollection
    @CollectionTable(name = "date_request_venues", joinColumns = @JoinColumn(name = "date_request_id"))
    @Column(name = "venue_id")
    private Set<String> selectedVenues = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DateRequestStatus status;

    @Column(nullable = false)
    private boolean safetyAlertActive = false;

    @ManyToMany
    @JoinTable(name = "date_request_participants", joinColumns = @JoinColumn(name = "date_request_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> participants = new HashSet<>();

    @Column(nullable = false)
    private boolean dutchPay = true;

    // Helper method to check if group is full
    @Transient
    public boolean isGroupFull() {
        return participants.size() >= maxGroupSize;
    }
}
