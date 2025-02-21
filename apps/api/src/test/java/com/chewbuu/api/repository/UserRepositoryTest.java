package com.chewbuu.api.repository;

import com.chewbuu.api.domain.User;
import com.chewbuu.api.domain.enums.SubscriptionTier;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndRetrieveUser() {
        // Given
        User user = User.builder()
                .username("testuser")
                .firstName("Test")
                .lastName("User")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .location("New York")
                .introVideoUrl("https://example.com/video.mp4")
                .subscriptionTier(SubscriptionTier.SINGLE)
                .averageRating(0.0)
                .verified(true)
                .build();

        // When
        User savedUser = userRepository.save(user);
        Optional<User> foundUser = userRepository.findByUsername("testuser");

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
        assertThat(foundUser.get().getAge()).isEqualTo(25); // As of 2025
    }

    @Test
    void shouldFindPotentialMatches() {
        // Given
        User user1 = createUser("user1", LocalDate.of(2000, 1, 1), "New York"); // 25 years old
        User user2 = createUser("user2", LocalDate.of(2002, 1, 1), "New York"); // 23 years old
        User user3 = createUser("user3", LocalDate.of(1990, 1, 1), "Boston");   // 35 years old
        userRepository.saveAll(List.of(user1, user2, user3));

        // When
        List<User> matches = userRepository.findPotentialMatches(21, 30, "New York");

        // Then
        assertThat(matches).hasSize(2);
        assertThat(matches).extracting("username").containsExactlyInAnyOrder("user1", "user2");
    }

    private User createUser(String username, LocalDate dob, String location) {
        return User.builder()
                .username(username)
                .firstName("Test")
                .lastName("User")
                .dateOfBirth(dob)
                .location(location)
                .introVideoUrl("https://example.com/video.mp4")
                .subscriptionTier(SubscriptionTier.SINGLE)
                .averageRating(0.0)
                .verified(true)
                .build();
    }
}
