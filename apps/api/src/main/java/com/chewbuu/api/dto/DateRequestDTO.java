package com.chewbuu.api.dto;

import com.chewbuu.api.domain.enums.ActivityType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class DateRequestDTO {
    private ActivityType activityType;
    private LocalDateTime dateTime;
    private int maxGroupSize;
    private Set<String> selectedVenues;
    private boolean dutchPay;
}
