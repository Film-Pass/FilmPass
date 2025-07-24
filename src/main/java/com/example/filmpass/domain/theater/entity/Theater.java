package com.example.filmpass.domain.theater.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "theaters")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Theater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;          // 극장 식별자

    @Column(nullable = false, unique = true)
    private String name;      // 극장 이름

    @Column(nullable = false)
    private String location;   // 극장 주소

    public Theater(String name, String location) {
        this.name = name;
        this.location = location;
    }
}