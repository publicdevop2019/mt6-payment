package com.hw.repo;


import com.hw.entity.BizPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepo extends JpaRepository<BizPayment, Long> {
    @Query("SELECT p FROM #{#entityName} as p WHERE p.orderId = ?1")
    Optional<BizPayment> getPaymentByOrderId(String orderId);
}
