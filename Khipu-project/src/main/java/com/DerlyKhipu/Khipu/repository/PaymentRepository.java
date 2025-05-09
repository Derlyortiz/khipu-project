package com.DerlyKhipu.Khipu.repository;

import com.DerlyKhipu.Khipu.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    @Query("SELECT p FROM Payment p WHERE p.status = :status AND p.createdAt < :createdAt")
    List<Payment> findByStatusAndCreatedAtBefore(
            @Param("status") String status,
            @Param("createdAt") LocalDateTime createdAt
    );

    @Modifying
    @Query("UPDATE Payment p SET p.status = :status, p.statusDetail = :detail WHERE p.paymentId = :paymentId")
    void updateStatus(String paymentId, String status, String detail);
}

// Derly Ortiz Ubiera