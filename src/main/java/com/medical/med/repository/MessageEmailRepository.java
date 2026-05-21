package com.medical.med.repository;

import com.medical.med.model.EmailMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageEmailRepository extends JpaRepository<EmailMessage, Long> {
    @Query("SELECT e FROM EmailMessage e WHERE e.shippingTime <= :cutoffTime ORDER BY e.shippingTime ASC")
    Page<EmailMessage> findMessageForRetry(@Param("cutoffTime")LocalDateTime cutoffTime, Pageable pageable);

    void deleteByIdIn(List<Long> ids);
}
