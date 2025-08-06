package com.example.filmpass.review;

import com.example.filmpass.domain.review.controller.ReviewController;
import com.example.filmpass.domain.review.dto.ReviewRequestDto;
import com.example.filmpass.domain.review.dto.ReviewResponseDto;
import com.example.filmpass.domain.review.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@WebMvcTest(controllers = ReviewController.class)
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("리뷰 생성 성공")
    public void createReview_success() throws Exception {
        ReviewRequestDto requestDto = new ReviewRequestDto(1L, 1L, 5, "최고의 영화입니다.");

        ReviewResponseDto responseDto = new ReviewResponseDto(
                1L,
                1L,
                5,
                "최고의 영화입니다.",
                LocalDateTime.now()
        );

        Mockito.when(reviewService.createReview(any(ReviewRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/reviews")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.reviewId").value(responseDto.getReviewId()))
                .andExpect(jsonPath("$.data.movieId").value(responseDto.getMovieId()))
                .andExpect(jsonPath("$.data.rating").value(responseDto.getRating()))
                .andExpect(jsonPath("$.data.content").value(responseDto.getContent()));
    }

    @Test
    @DisplayName("영화별 리뷰 조회 성공")
    public void getReviewsByMovie_success() throws Exception {
        Long movieId = 1L;
        PageRequest pageable = PageRequest.of(0, 10);

        List<ReviewResponseDto> reviewList = List.of(
                new ReviewResponseDto(1L, movieId, 4, "재밌어요", LocalDateTime.now()),
                new ReviewResponseDto(2L, movieId, 5, "최고예요", LocalDateTime.now())
        );
        Page<ReviewResponseDto> page = new PageImpl<>(reviewList, pageable, reviewList.size());

        Mockito.when(reviewService.getReviewsByMovie(eq(movieId), any(PageRequest.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/reviews/{movieId}", movieId)
                .with(csrf())
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].reviewId").value(1L))
                .andExpect(jsonPath("$.data.content[1].rating").value(5));
    }

    @Test
    @DisplayName("리뷰 수정 성공")
    public void updateReview_success() throws Exception {
        Long reviewId = 1L;
        ReviewRequestDto requestDto = new ReviewRequestDto(1L, 1L, 4, "수정된 리뷰 내용");

        ReviewResponseDto responseDto = new ReviewResponseDto(
                reviewId,
                1L,
                4,
                "수정된 리뷰 내용",
                LocalDateTime.now()
        );

        Mockito.when(reviewService.updateReview(eq(reviewId), any(ReviewRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(patch("/api/reviews/{reviewId}", reviewId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.reviewId").value(responseDto.getReviewId()))
                .andExpect(jsonPath("$.data.rating").value(responseDto.getRating()))
                .andExpect(jsonPath("$.data.content").value(responseDto.getContent()));
    }

    @Test
    @DisplayName("리뷰 삭제 성공")
    public void deleteReview_success() throws Exception {
        Long reviewId = 1L;

        // void 메서드니까 별도 Mockito.when 불필요. 단순히 호출만 확인.

        mockMvc.perform(delete("/api/reviews/{reviewId}", reviewId).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("리뷰가 삭제되었습니다."));
    }

    @Test
    @DisplayName("평론가 리뷰 생성 성공")
    public void createCriticReview_success() throws Exception {
        ReviewRequestDto requestDto = new ReviewRequestDto(1L, 1L, 5, "평론가의 리뷰");

        ReviewResponseDto responseDto = new ReviewResponseDto(
                1L,
                1L,
                5,
                "평론가의 리뷰",
                LocalDateTime.now()
        );

        Mockito.when(reviewService.createCriticReview(any(ReviewRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/reviews/critics")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.reviewId").value(responseDto.getReviewId()))
                .andExpect(jsonPath("$.data.content").value(responseDto.getContent()));
    }
}
