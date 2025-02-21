package com.chewbuu.api.service;

import com.chewbuu.api.domain.User;
import com.chewbuu.api.domain.enums.SubscriptionTier;
import com.chewbuu.api.exception.ChewbuuException;
import com.chewbuu.api.repository.DateRequestRepository;
import com.chewbuu.api.repository.UserRepository;
import com.chewbuu.api.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DateRequestRepository dateRequestRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, dateRequestRepository);
    }

    @Test
    void shouldNotAllowUsersUnder19() {
        // Given
        User underageUser = createUser("young", LocalDate.now().minusYears(18));

        // When/Then
        assertThatThrownBy(() -> userService.createUser(underageUser))
            .isInstanceOf(ChewbuuException.class)
            .hasMessageContaining("must be 19 or older");
    }

    @Test
    void shouldMatchUsersBasedOnAgeAndTier() {
        // Given
        User user20 = createUser("user20", LocalDate.now().minusYears(20));
        User user19 = createUser("user19", LocalDate.now().minusYears(19));
        User user21 = createUser("user21", LocalDate.now().minusYears(21));
        User user25 = createUser("user25", LocalDate.now().minusYears(25));

        // Test matching for 20-year-old regular tier
        assertThat(userService.canMatchWith(user20, user19)).isTrue();  // Can match with 19
        assertThat(userService.canMatchWith(user20, user21)).isTrue();  // Can match with 21
        assertThat(userService.canMatchWith(user20, user25)).isFalse(); // Cannot match with 25

        // Test matching for 21+ regular tier
        assertThat(userService.canMatchWith(user21, user19)).isFalse(); // Cannot match with 19
        assertThat(userService.canMatchWith(user21, user25)).isTrue();  // Can match with 25

        // Test SUGAR tier can match with anyone 19+
        user25.setSubscriptionTier(SubscriptionTier.SUGAR);
        assertThat(userService.canMatchWith(user25, user19)).isTrue();  // Can match with 19
        assertThat(userService.canMatchWith(user25, user21)).isTrue();  // Can match with 21
        assertThat(userService.canMatchWith(user25, user25)).isTrue();  // Can match with 25
    }

    @Test
    void shouldLimitDateRequestsForFreeTier() {
        // Given
        User freeUser = createUser("free", LocalDate.now().minusYears(25));
        freeUser.setSubscriptionTier(SubscriptionTier.SINGLE);

        when(dateRequestRepository.countActiveDateRequestsForToday(any(), any(), any()))
            .thenReturn(2);

        // When/Then
        assertThat(userService.canInitiateDateRequest(freeUser)).isFalse();

        // Given
        User premiumUser = createUser("premium", LocalDate.now().minusYears(25));
        premiumUser.setSubscriptionTier(SubscriptionTier.SUGAR);

        // When/Then
        assertThat(userService.canInitiateDateRequest(premiumUser)).isTrue();
    }

    private User createUser(String username, LocalDate dateOfBirth) {
        return User.builder()
            .username(username)
            .firstName("Test")
            .lastName("User")
            .dateOfBirth(dateOfBirth)
            .location("New York")
            .introVideoUrl("https://example.com/video.mp4")
            .subscriptionTier(SubscriptionTier.SINGLE)
            .averageRating(0.0)
            .verified(true)
            .build();
    }
}
