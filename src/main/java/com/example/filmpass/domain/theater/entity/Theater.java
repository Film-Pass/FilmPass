package com.example.filmpass.domain.theater.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "theaters")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Theater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;          // 극장 식별자

    @Column(nullable = false, unique = true)
    private String name;      // 극장 이름

    @Column(nullable = false)
    private String location;   // 극장 주소

    public void update(String name, String location) {
        this.name = name;
        this.location = location;
    }
}