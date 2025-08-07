package com.example.filmpass.domain.payment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentKey;

    @Column(unique = true)
    private String orderId;

    private Integer amount;

    private LocalDateTime approvedAt;


    public Payment(String paymentKey, String orderId, Integer amount, LocalDateTime approvedAt) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
        this.approvedAt = approvedAt;
    }
}
