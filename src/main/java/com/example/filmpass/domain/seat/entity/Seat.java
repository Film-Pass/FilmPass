package com.example.filmpass.domain.seat.entity;

import com.example.filmpass.domain.screen.entity.Screen;
import com.example.filmpass.domain.seat.dto.SeatStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seats")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 좌석 식별자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen; // 상영관 (Screen) 식별자 - 연관관계

    @Column(name = "seat_id",nullable = false)
    private String seatNumber; // 좌석 이름 (예: A1, B2)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status = SeatStatus.AVAILABLE;

    // 좌석 등록
    public Seat(Screen screen, String seatNumber) {
        this.screen = screen;
        this.seatNumber = seatNumber;
        this.status = SeatStatus.AVAILABLE;  // 기본 상태 명확히 할당
    }

    // 고장난 좌석으로 변경
    public void markAsBroken() {
        this.status = SeatStatus.BROKEN;
    }

    // 좌석 상태를 사용 가능으로 변경
    public void markAsAvailable() {
        this.status = SeatStatus.AVAILABLE;
    }

    // 좌석 수정
    public void update(Screen screen, String seatNumber) {
        this.screen = screen;
        this.seatNumber = seatNumber;
    }

    // 고장 여부 확인
    public boolean isBroken() {
        return this.status == SeatStatus.BROKEN;
    }
}
