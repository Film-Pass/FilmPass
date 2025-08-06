package com.example.filmpass.domain.screen.entity;

import com.example.filmpass.domain.screen.enums.ScreenType;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScreenType screenType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    private Integer amount = 10000;

    //  생성자
    public Screen(String name, String address, ScreenType screenType, Theater theater) {
        this.name = name;
        this.address = address;
        this.screenType = screenType;
        this.theater = theater;
    }

    //  update
    public void update(String name, String address, ScreenType screenType, Theater theater) {
        this.name = name;
        this.address = address;
        this.screenType = screenType;
        this.theater = theater;
    }

    // 테스트용 ID 설정자
    public void setIdForTest(Long id) {
        this.id = id;
    }
}
