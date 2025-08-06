package com.example.filmpass.review;

import com.example.filmpass.domain.movie.entity.Movie;
import com.example.filmpass.domain.movie.repository.MovieRepository;
import com.example.filmpass.domain.review.dto.ReviewRequestDto;
import com.example.filmpass.domain.review.dto.ReviewResponseDto;
import com.example.filmpass.domain.review.entity.Review;
import com.example.filmpass.domain.review.repository.ReviewRepository;
import com.example.filmpass.domain.review.service.ReviewService;
import com.example.filmpass.domain.user.entity.User;
import com.example.filmpass.domain.user.repository.UserRepository;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service: ReviewService 테스트")
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReviewService reviewService;

    private User user;
    private Movie movie;
    private ReviewRequestDto request;

    @BeforeEach
    void setUp() {
        user = new User("test@example.com", "password", "critic123", "홍길동");
        user.setIdForTest(1L);
        user.setCritic(false);

        movie = new Movie("테스트 영화", "감독", "설명", "2시간", "posterUrl", "장르");
        movie.setIdForTest(1L);

        request = new ReviewRequestDto(1L, 1L, 4, "아주 재밌는 영화입니다.");
    }

    @Test
    @DisplayName("리뷰 생성 성공")
    void createReview_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        Review review = new Review(4, "아주 재밌는 영화입니다.", movie, user);
        review.setIdForTest(100L);

        when(reviewRepository.save(any())).thenReturn(review);

        ReviewResponseDto result = reviewService.createReview(request);

        assertThat(result).isNotNull();
        assertThat(result.getReviewId()).isEqualTo(100L);
        assertThat(result.getMovieId()).isEqualTo(1L);
        assertThat(result.getRating()).isEqualTo(4);
        assertThat(result.getContent()).isEqualTo("아주 재밌는 영화입니다.");
        assertThat(result.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("리뷰 삭제 - 존재하지 않는 리뷰")
    void deleteReview_fail_notFound() {
        when(reviewRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.deleteReview(999L))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.REVIEW_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("리뷰 삭제 성공")
    void deleteReview_success() {
        Review review = new Review(4, "삭제할 리뷰", movie, user);
        review.setIdForTest(10L);

        when(reviewRepository.findById(10L)).thenReturn(Optional.of(review));

        reviewService.deleteReview(10L);

        verify(reviewRepository, times(1)).delete(review);
    }

    @Test
    @DisplayName("평론가 리뷰 작성 실패 - 비평가가 작성 시 예외 발생")
    void createCriticReview_fail_ifNotCritic() {
        user.setCritic(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> reviewService.createCriticReview(request))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.CRITIC_ONLY.getMessage());
    }

    @Test
    @DisplayName("평론가 리뷰 작성 성공")
    void createCriticReview_success() {
        user.setCritic(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        Review review = new Review(4, "비평가 리뷰", movie, user);
        review.setIdForTest(50L);

        when(reviewRepository.save(any())).thenReturn(review);

        ReviewResponseDto result = reviewService.createCriticReview(request);

        assertThat(result).isNotNull();
        assertThat(result.getReviewId()).isEqualTo(50L);
        assertThat(result.getMovieId()).isEqualTo(1L);
        assertThat(result.getContent()).contains("비평가");
    }
}
