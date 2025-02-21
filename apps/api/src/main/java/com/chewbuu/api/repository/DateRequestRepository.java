package com.chewbuu.api.repository;

import com.chewbuu.api.domain.DateRequest;
import com.chewbuu.api.domain.User;
import com.chewbuu.api.domain.enums.DateRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface DateRequestRepository extends JpaRepository<DateRequest, UUID> {
    List<DateRequest> findByInitiatorAndStatus(User initiator, DateRequestStatus status);
    
    @Query("SELECT dr FROM DateRequest dr WHERE dr.status = :status AND dr.dateTime > :now")
    List<DateRequest> findActiveDateRequests(
        @Param("status") DateRequestStatus status,
        @Param("now") LocalDateTime now
    );

    @Query("SELECT COUNT(dr) FROM DateRequest dr WHERE dr.initiator = :user AND " +
           "dr.status IN ('PENDING', 'MATCHED', 'ACCEPTED') AND " +
           "dr.dateTime BETWEEN :startOfDay AND :endOfDay")
    int countActiveDateRequestsForToday(
        @Param("user") User user,
        @Param("startOfDay") LocalDateTime startOfDay,
        @Param("endOfDay") LocalDateTime endOfDay
    );
}
