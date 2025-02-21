package com.chewbuu.api.domain;

import com.chewbuu.api.domain.enums.SubscriptionTier;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted = false")
public class User extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String introVideoUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionTier subscriptionTier;

    @Column(nullable = false)
    private double averageRating;

    @ElementCollection
    @CollectionTable(name = "user_interests", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "interest")
    private Set<String> interests = new HashSet<>();

    @Column(nullable = false)
    private boolean verified = false;

    @Column(nullable = false)
    private int flakeCount = 0;

    @Column(nullable = false)
    private int lateCount = 0;

    // Calculated age field
    @Transient
    public int getAge() {
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }

    // Helper method to check if user can drink
    @Transient
    public boolean isOfDrinkingAge() {
        return getAge() >= 21;
    }
}
