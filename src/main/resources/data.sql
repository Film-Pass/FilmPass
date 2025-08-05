-- 1) FK 제약 잠시 끄기
SET FOREIGN_KEY_CHECKS = 0;

-- 2) 하위 → 상위 순 TRUNCATE
TRUNCATE TABLE schedules;
TRUNCATE TABLE seats;
TRUNCATE TABLE screens;
TRUNCATE TABLE theaters;
TRUNCATE TABLE movies;

-- 3) FK 제약 다시 켜기
SET FOREIGN_KEY_CHECKS = 1;

-- 4) 영화 정보
INSERT INTO movies (id,title, director, description, running_time, poster_url, is_delete, genre)
VALUES
    (1,'F1 The Movie', '조셉 코신스키', '포뮬러 1을 소재로 하는 조셉 코신스키 감독, 브래드 피트 주연 영화', '2시간 35분', 'https://upload.wikimedia.org/wikipedia/ko/1/11/F1%EB%8D%94%EB%AC%B4%EB%B9%842025.jpg',FALSE, '스포츠'),
    (2,'About Time', '리처드 커티스', '시간 여행이 가능한 남자의 놓쳐버린 그녀를 찾기 위한 시간여행', '2시간', 'https://i.namu.wiki/i/8ybSiGJ9RpH1-TLKJqnvElwllAGj-IJUr9ElFwKELRuBAACV6Pzn5ntjIpGzBUIEgh5iH_G8-BcFyGUeAoRpLNHAFasUXp0NdlilBfkrR8smITw0h8obWhwi68AMNcElva0naeSN4Sy6aLCBaB0peg.webp',FALSE,'로맨스');

-- 5) 극장 정보
INSERT INTO theaters (id, name, location)
VALUES (1, 'FilmPass', '서울특별시 스파르타 123-123');

-- 6) 스크린 정보
INSERT INTO screens (id, name, address, theater_id)
VALUES
    (1, '스크린 1', '서울특별시 스파르타 123-123', 1),
    (2, '스크린 2', '서울특별시 스파르타 123-123', 1);


-- 7) 상영 스케줄 정보
INSERT INTO schedules (start_at, end_at, screen_id, movie_id)
VALUES
-- F1
('2025-08-01 19:00:00', '2025-08-01 21:35:00', 1, 1),
-- About Time
('2025-08-02 18:00:00', '2025-08-02 20:00:00', 2, 2);

-- 8) 좌석 정보 (스크린 1, 2번 / A~J행, 1~7열)
INSERT INTO seats (screen_id, seat_id, broken, status)
VALUES
-- 스크린 1
(1, 'A1', false, 'AVAILABLE'), (1, 'A2', false, 'AVAILABLE'), (1, 'A3', false, 'AVAILABLE'), (1, 'A4', false, 'AVAILABLE'), (1, 'A5', false, 'AVAILABLE'), (1, 'A6', false, 'AVAILABLE'), (1, 'A7', false, 'AVAILABLE'),
(1, 'B1', false, 'AVAILABLE'), (1, 'B2', false, 'AVAILABLE'), (1, 'B3', false, 'AVAILABLE'), (1, 'B4', false, 'AVAILABLE'), (1, 'B5', false, 'AVAILABLE'), (1, 'B6', false, 'AVAILABLE'), (1, 'B7', false, 'AVAILABLE'),
(1, 'C1', false, 'AVAILABLE'), (1, 'C2', false, 'AVAILABLE'), (1, 'C3', false, 'AVAILABLE'), (1, 'C4', false, 'AVAILABLE'), (1, 'C5', false, 'AVAILABLE'), (1, 'C6', false, 'AVAILABLE'), (1, 'C7', false, 'AVAILABLE'),
(1, 'D1', false, 'AVAILABLE'), (1, 'D2', false, 'AVAILABLE'), (1, 'D3', false, 'AVAILABLE'), (1, 'D4', false, 'AVAILABLE'), (1, 'D5', false, 'AVAILABLE'), (1, 'D6', false, 'AVAILABLE'), (1, 'D7', false, 'AVAILABLE'),
(1, 'E1', false, 'AVAILABLE'), (1, 'E2', false, 'AVAILABLE'), (1, 'E3', false, 'AVAILABLE'), (1, 'E4', false, 'AVAILABLE'), (1, 'E5', false, 'AVAILABLE'), (1, 'E6', false, 'AVAILABLE'), (1, 'E7', false, 'AVAILABLE'),
(1, 'F1', false, 'AVAILABLE'), (1, 'F2', false, 'AVAILABLE'), (1, 'F3', false, 'AVAILABLE'), (1, 'F4', false, 'AVAILABLE'), (1, 'F5', false, 'AVAILABLE'), (1, 'F6', false, 'AVAILABLE'), (1, 'F7', false, 'AVAILABLE'),
(1, 'G1', false, 'AVAILABLE'), (1, 'G2', false, 'AVAILABLE'), (1, 'G3', false, 'AVAILABLE'), (1, 'G4', false, 'AVAILABLE'), (1, 'G5', false, 'AVAILABLE'), (1, 'G6', false, 'AVAILABLE'), (1, 'G7', false, 'AVAILABLE'),
(1, 'H1', false, 'AVAILABLE'), (1, 'H2', false, 'AVAILABLE'), (1, 'H3', false, 'AVAILABLE'), (1, 'H4', false, 'AVAILABLE'), (1, 'H5', false, 'AVAILABLE'), (1, 'H6', false, 'AVAILABLE'), (1, 'H7', false, 'AVAILABLE'),
(1, 'I1', false, 'AVAILABLE'), (1, 'I2', false, 'AVAILABLE'), (1, 'I3', false, 'AVAILABLE'), (1, 'I4', false, 'AVAILABLE'), (1, 'I5', false, 'AVAILABLE'), (1, 'I6', false, 'AVAILABLE'), (1, 'I7', false, 'AVAILABLE'),
(1, 'J1', false, 'AVAILABLE'), (1, 'J2', false, 'AVAILABLE'), (1, 'J3', false, 'AVAILABLE'), (1, 'J4', false, 'AVAILABLE'), (1, 'J5', false, 'AVAILABLE'), (1, 'J6', false, 'AVAILABLE'), (1, 'J7', false, 'AVAILABLE'),

-- 스크린 2
(2, 'A1', false, 'AVAILABLE'), (2, 'A2', false, 'AVAILABLE'), (2, 'A3', false, 'AVAILABLE'), (2, 'A4', false, 'AVAILABLE'), (2, 'A5', false, 'AVAILABLE'), (2, 'A6', false, 'AVAILABLE'), (2, 'A7', false, 'AVAILABLE'),
(2, 'B1', false, 'AVAILABLE'), (2, 'B2', false, 'AVAILABLE'), (2, 'B3', false, 'AVAILABLE'), (2, 'B4', false, 'AVAILABLE'), (2, 'B5', false, 'AVAILABLE'), (2, 'B6', false, 'AVAILABLE'), (2, 'B7', false, 'AVAILABLE'),
(2, 'C1', false, 'AVAILABLE'), (2, 'C2', false, 'AVAILABLE'), (2, 'C3', false, 'AVAILABLE'), (2, 'C4', false, 'AVAILABLE'), (2, 'C5', false, 'AVAILABLE'), (2, 'C6', false, 'AVAILABLE'), (2, 'C7', false, 'AVAILABLE'),
(2, 'D1', false, 'AVAILABLE'), (2, 'D2', false, 'AVAILABLE'), (2, 'D3', false, 'AVAILABLE'), (2, 'D4', false, 'AVAILABLE'), (2, 'D5', false, 'AVAILABLE'), (2, 'D6', false, 'AVAILABLE'), (2, 'D7', false, 'AVAILABLE'),
(2, 'E1', false, 'AVAILABLE'), (2, 'E2', false, 'AVAILABLE'), (2, 'E3', false, 'AVAILABLE'), (2, 'E4', false, 'AVAILABLE'), (2, 'E5', false, 'AVAILABLE'), (2, 'E6', false, 'AVAILABLE'), (2, 'E7', false, 'AVAILABLE'),
(2, 'F1', false, 'AVAILABLE'), (2, 'F2', false, 'AVAILABLE'), (2, 'F3', false, 'AVAILABLE'), (2, 'F4', false, 'AVAILABLE'), (2, 'F5', false, 'AVAILABLE'), (2, 'F6', false, 'AVAILABLE'), (2, 'F7', false, 'AVAILABLE'),
(2, 'G1', false, 'AVAILABLE'), (2, 'G2', false, 'AVAILABLE'), (2, 'G3', false, 'AVAILABLE'), (2, 'G4', false, 'AVAILABLE'), (2, 'G5', false, 'AVAILABLE'), (2, 'G6', false, 'AVAILABLE'), (2, 'G7', false, 'AVAILABLE'),
(2, 'H1', false, 'AVAILABLE'), (2, 'H2', false, 'AVAILABLE'), (2, 'H3', false, 'AVAILABLE'), (2, 'H4', false, 'AVAILABLE'), (2, 'H5', false, 'AVAILABLE'), (2, 'H6', false, 'AVAILABLE'), (2, 'H7', false, 'AVAILABLE'),
(2, 'I1', false, 'AVAILABLE'), (2, 'I2', false, 'AVAILABLE'), (2, 'I3', false, 'AVAILABLE'), (2, 'I4', false, 'AVAILABLE'), (2, 'I5', false, 'AVAILABLE'), (2, 'I6', false, 'AVAILABLE'), (2, 'I7', false, 'AVAILABLE'),
(2, 'J1', false, 'AVAILABLE'), (2, 'J2', false, 'AVAILABLE'), (2, 'J3', false, 'AVAILABLE'), (2, 'J4', false, 'AVAILABLE'), (2, 'J5', false, 'AVAILABLE'), (2, 'J6', false, 'AVAILABLE'), (2, 'J7', false, 'AVAILABLE');

