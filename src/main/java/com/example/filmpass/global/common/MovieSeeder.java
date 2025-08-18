//package com.example.filmpass.global.common;
//
//import com.example.filmpass.domain.movie.entity.Movie;
//import com.example.filmpass.domain.movie.repository.MovieRepository;
//import com.example.filmpass.domain.movie.service.MovieService;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.domain.PageRequest;
//
//import java.time.LocalDate;
//import java.time.temporal.ChronoUnit;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//@Configuration
//public class MovieSeeder {
//
//
//    private int seedCount = 5000;
//
//    // 데이터 5000 개를 무작위 값을 insert 하여 생성
//    @Bean
//    CommandLineRunner seedMovies(MovieRepository movieRepository, MovieService movieService) {
//        return args -> {
//            long current = movieRepository.count();
//
//            if (movieRepository.count() >= seedCount) {
//                System.out.println("[seed] enough movies exist, skip seeding.");
//            } else {
//                System.out.println("[seed] inserting " + seedCount + " movies ...");
//                Random r = new Random(42);
//
//                int batch = 500; // 배치 등록
//                List<Movie> buf = new ArrayList<>(batch);
//
//                for (int i = 1; i <= seedCount; i++) {
//
//                    String title = "Movie " + i;
//                    String director = "Director " + (r.nextInt(100) + 1);
//
//                    String[] genres = {"ACTION", "DRAMA", "COMEDY", "SF", "ROMANCE", "THRILLER", "ANIMATION"};
//                    String genre = genres[r.nextInt(genres.length)];
//
//                    String runningTime = (80 + r.nextInt(61)) + "min"; // 80~140분 사이 무작위 값
//
//                    // 랜덤 개봉일 (2020-01-01 ~ 2024-12-31)
//                    LocalDate start = LocalDate.of(2020, 1, 1);
//                    LocalDate randomDate = start.plusDays(r.nextInt((int) ChronoUnit.DAYS.between(start, LocalDate.of(2024, 12, 31))));
//                    String releaseDate = randomDate.toString();
//
//                    String description = "Description for " + title;
//                    String posterUrl = "https://example.com/posters/" + r.nextInt(1000) + ".jpg";
//
//                    // 생성자 호출
//                    Movie movie = new Movie(title, director, genre, runningTime, releaseDate, description, posterUrl);
//                    buf.add(movie);
//
//                    if (buf.size() == batch) {
//                        movieRepository.saveAll(buf);
//                        buf.clear();
//                    }
//                }
//                if (!buf.isEmpty()) movieRepository.saveAll(buf);
//                System.out.println("[seed] done.");
//
//                current = movieRepository.count();
//            }
//
//            // 캐시 워밍업
//            if (current >= seedCount) {
//                long total = movieRepository.count(); // 1. 전체 영화 데이터 개수 조회
//                int pages = (int) Math.ceil((double) total / 20); // 2. 총 페이지 수 계산
//                System.out.println("[seed] warming cache, pages=" + pages);
//
//                // 3. 페이지별로 순회
//                for (int p = 0; p < pages; p++) {
//                    // @Cacheable이 붙은 서비스 호출 → 호출 결과가 캐시에 저장됨
//                    movieService.findAllMovieV2(PageRequest.of(p, 20));
//                }
//
//                System.out.println("[seed] warm-up done.");
//            }
//
//        };
//    }
//}
