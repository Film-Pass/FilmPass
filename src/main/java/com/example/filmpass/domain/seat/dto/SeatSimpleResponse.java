package com.example.filmpass.domain.seat.dto;

public class SeatSimpleResponse {
    private Long id;
    private String seatNumber;
    private SeatStatus status;

    public SeatSimpleResponse(Long id, String seatNumber, SeatStatus status) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.status = status;
    }

    public Long getId() { return id; }
    public String getSeatNumber() { return seatNumber; }
    public SeatStatus getStatus() { return status; }
}