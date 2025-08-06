package com.example.filmpass.domain.screen.entity;

import com.example.filmpass.domain.theater.entity.Theater;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "screens")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    // 생성자
    public Screen(String name, String address, Theater theater) {
        this.name = name;
        this.address = address;
        this.theater = theater;
    }

    public void setIdForTest(Long id) {
        this.id = id;
    }
}
