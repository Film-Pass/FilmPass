package com.example.filmpass.domain.seat.controller;

    
import com.example.filmpass.domain.seat.dto.PagedResponse;
import com.example.filmpass.domain.seat.dto.SeatRequest;
import com.example.filmpass.domain.seat.dto.SeatResponse;
import com.example.filmpass.domain.seat.service.SeatService;
import com.example.filmpass.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seats")
public class SeatController {

    private final SeatService seatService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SeatResponse>> createSeat(@RequestBody SeatRequest request) {
        SeatResponse createdSeat = seatService.createSeat(request);
        return ResponseEntity.ok(ApiResponse.success(createdSeat, "좌석 등록 성공"));
    }

    // 좌석 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<SeatResponse>>> getSeats(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        PagedResponse<SeatResponse> response = seatService.getSeats(pageable);
        return ResponseEntity.ok(ApiResponse.success(response, "좌석 목록 조회 성공"));
    }
}
