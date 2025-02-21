package com.chewbuu.api.service.impl;

import com.chewbuu.api.domain.DateRequest;
import com.chewbuu.api.domain.User;
import com.chewbuu.api.domain.enums.DateRequestStatus;
import com.chewbuu.api.domain.enums.SubscriptionTier;
import com.chewbuu.api.dto.DateRequestDTO;
import com.chewbuu.api.exception.ChewbuuException;
import com.chewbuu.api.repository.DateRequestRepository;
import com.chewbuu.api.service.DateRequestService;
import com.chewbuu.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DateRequestServiceImpl implements DateRequestService {

    private final DateRequestRepository dateRequestRepository;
    private final UserService userService;

    @Override
    public DateRequest createDateRequest(User initiator, DateRequestDTO dto) {
        validateDateRequest(initiator, dto);

        DateRequest dateRequest = DateRequest.builder()
            .initiator(initiator)
            .activityType(dto.getActivityType())
            .dateTime(dto.getDateTime())
            .maxGroupSize(dto.getMaxGroupSize())
            .selectedVenues(dto.getSelectedVenues())
            .status(DateRequestStatus.PENDING)
            .dutchPay(dto.isDutchPay())
            .build();

        return dateRequestRepository.save(dateRequest);
    }

    private void validateDateRequest(User initiator, DateRequestDTO dto) {
        if (!userService.canInitiateDateRequest(initiator)) {
            throw new ChewbuuException(
                "You have reached your daily limit for date requests",
                HttpStatus.BAD_REQUEST
            );
        }

        if (dto.getDateTime().isBefore(LocalDateTime.now())) {
            throw new ChewbuuException(
                "Date time must be in the future",
                HttpStatus.BAD_REQUEST
            );
        }

        // Validate group size based on subscription tier
        if (initiator.getSubscriptionTier() == SubscriptionTier.SINGLE && dto.getMaxGroupSize() > 1) {
            throw new ChewbuuException(
                "Free tier users can only create solo dates",
                HttpStatus.BAD_REQUEST
            );
        }

        if (dto.getMaxGroupSize() > 4) {
            throw new ChewbuuException(
                "Maximum group size is 4",
                HttpStatus.BAD_REQUEST
            );
        }

        // Premium tier users can opt to pay for everyone
        if (!dto.isDutchPay() && initiator.getSubscriptionTier() != SubscriptionTier.SUGAR) {
            throw new ChewbuuException(
                "Only premium tier users can pay for the entire group",
                HttpStatus.BAD_REQUEST
            );
        }
    }

    @Override
    public DateRequest getDateRequestById(UUID id) {
        return dateRequestRepository.findById(id)
            .orElseThrow(() -> new ChewbuuException("Date request not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public List<DateRequest> getActiveDateRequests() {
        return dateRequestRepository.findActiveDateRequests(
            DateRequestStatus.PENDING,
            LocalDateTime.now()
        );
    }

    @Override
    public void addParticipant(UUID dateRequestId, UUID userId) {
        DateRequest dateRequest = getDateRequestById(dateRequestId);
        User participant = userService.getUserById(userId);

        if (dateRequest.isGroupFull()) {
            throw new ChewbuuException("Group is already full", HttpStatus.BAD_REQUEST);
        }

        // Check if participant can join based on age restrictions
        if (!userService.canMatchWith(dateRequest.getInitiator(), participant)) {
            throw new ChewbuuException(
                "Age restriction prevents joining this date",
                HttpStatus.BAD_REQUEST
            );
        }

        dateRequest.getParticipants().add(participant);
        dateRequest.setStatus(DateRequestStatus.MATCHED);
        dateRequestRepository.save(dateRequest);
    }

    @Override
    public void toggleSafetyAlert(UUID dateRequestId, boolean enabled) {
        DateRequest dateRequest = getDateRequestById(dateRequestId);
        
        if (dateRequest.getStatus() != DateRequestStatus.ACTIVE) {
            throw new ChewbuuException(
                "Safety alert can only be toggled for active dates",
                HttpStatus.BAD_REQUEST
            );
        }

        dateRequest.setSafetyAlertActive(enabled);
        dateRequestRepository.save(dateRequest);

        if (enabled) {
            // TODO: Trigger safety protocols (emergency services, recording, etc.)
        }
    }

    @Override
    public void completeDateRequest(UUID dateRequestId) {
        DateRequest dateRequest = getDateRequestById(dateRequestId);
        dateRequest.setStatus(DateRequestStatus.COMPLETED);
        dateRequestRepository.save(dateRequest);
    }

    @Override
    public void cancelDateRequest(UUID dateRequestId) {
        DateRequest dateRequest = getDateRequestById(dateRequestId);
        dateRequest.setStatus(DateRequestStatus.CANCELLED);
        dateRequestRepository.save(dateRequest);
    }
}
