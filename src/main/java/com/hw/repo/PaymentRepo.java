package com.hw.repo;


import com.hw.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepo extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p WHERE p.orderId = ?1")
    Optional<Payment> getPaymentByOrderId(String orderId);
}
