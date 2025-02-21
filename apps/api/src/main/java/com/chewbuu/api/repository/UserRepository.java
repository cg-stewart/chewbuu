package com.chewbuu.api.repository;

import com.chewbuu.api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    
    @Query("SELECT u FROM User u WHERE u.verified = true AND " +
           "(:minAge IS NULL OR FUNCTION('YEAR', CURRENT_DATE) - FUNCTION('YEAR', u.dateOfBirth) >= :minAge) AND " +
           "(:maxAge IS NULL OR FUNCTION('YEAR', CURRENT_DATE) - FUNCTION('YEAR', u.dateOfBirth) <= :maxAge) AND " +
           "u.location = :location")
    List<User> findPotentialMatches(
        @Param("minAge") Integer minAge,
        @Param("maxAge") Integer maxAge,
        @Param("location") String location
    );

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.username = :username AND u.deleted = false")
    boolean existsByUsername(@Param("username") String username);
}
