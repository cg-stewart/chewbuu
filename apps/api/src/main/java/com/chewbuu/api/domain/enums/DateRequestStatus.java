package com.chewbuu.api.domain.enums;

public enum DateRequestStatus {
    PENDING,    // Initial state
    MATCHED,    // Found potential matches
    ACCEPTED,   // Match accepted
    ACTIVE,     // Date in progress
    COMPLETED,  // Date finished
    CANCELLED   // Date cancelled
}
