package com.example.filmpass.domain.seat.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SeatRequest {

    @NotBlank(message = "좌석 번호는 필수입니다.")  // 문자열은 NotBlank가 더 적합
    @Pattern(regexp = "^[A-Z]\\d{1,2}$", message = "좌석 번호 형식이 올바르지 않습니다.")
    private String seatNumber;  // 예: "A1"

    @NotNull(message = "상영관 ID는 필수입니다.")
    private Long screenId;
}
