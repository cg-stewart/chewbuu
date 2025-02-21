package com.chewbuu.api.service;

import com.chewbuu.api.domain.DateRequest;
import com.chewbuu.api.domain.User;
import com.chewbuu.api.dto.DateRequestDTO;

import java.util.List;
import java.util.UUID;

public interface DateRequestService {
    DateRequest createDateRequest(User initiator, DateRequestDTO dto);

    DateRequest getDateRequestById(UUID id);

    List<DateRequest> getActiveDateRequests();

    void addParticipant(UUID dateRequestId, UUID userId);

    void toggleSafetyAlert(UUID dateRequestId, boolean enabled);

    void completeDateRequest(UUID dateRequestId);

    void cancelDateRequest(UUID dateRequestId);
}
