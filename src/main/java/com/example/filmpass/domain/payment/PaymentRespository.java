package com.example.filmpass.domain.payment;

import com.example.filmpass.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRespository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(String orderId);
}
