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

--영화 정보
INSERT INTO movies (id,title, director, description, running_time, poster_url, is_delete)
VALUES
    (1,'F1 The Movie', '조셉 코신스키', '포뮬러 1을 소재로 하는 조셉 코신스키 감독, 브래드 피트 주연 영화', '2시간 35분', 'https://upload.wikimedia.org/wikipedia/ko/1/11/F1%EB%8D%94%EB%AC%B4%EB%B9%842025.jpg',FALSE),
    (2,'About Time', '리처드 커티스', '시간 여행이 가능한 남자의 놓쳐버린 그녀를 찾기 위한 시간여행', '2시간', 'https://i.namu.wiki/i/8ybSiGJ9RpH1-TLKJqnvElwllAGj-IJUr9ElFwKELRuBAACV6Pzn5ntjIpGzBUIEgh5iH_G8-BcFyGUeAoRpLNHAFasUXp0NdlilBfkrR8smITw0h8obWhwi68AMNcElva0naeSN4Sy6aLCBaB0peg.webp',FALSE);

-- 극장 정보
INSERT INTO theaters (id, name, location)
VALUES (1, 'FilmPass', '서울특별시 스파르타 123-123');

--스크린 정보
INSERT INTO screens (id, name, address, theater_id)
VALUES
    (1, '스크린 1', '서울특별시 스파르타 123-123', 1),
    (2, '스크린 2', '서울특별시 스파르타 123-123', 1);


--상영 스케줄 정보
INSERT INTO schedules (start_at, end_at, screen_id, movie_id)
VALUES
-- F1
('2025-08-01 19:00:00', '2025-08-01 21:35:00', 1, 1),
-- About Time
('2025-08-02 18:00:00', '2025-08-02 20:00:00', 2, 2);

-- 좌석 정보 (스크린 1, 2번 / A~J행, 1~7열)
INSERT INTO seats (screen_id, seat_id)
VALUES
-- 스크린 1
(1, 'A1'), (1, 'A2'), (1, 'A3'), (1, 'A4'), (1, 'A5'), (1, 'A6'), (1, 'A7'),
(1, 'B1'), (1, 'B2'), (1, 'B3'), (1, 'B4'), (1, 'B5'), (1, 'B6'), (1, 'B7'),
(1, 'C1'), (1, 'C2'), (1, 'C3'), (1, 'C4'), (1, 'C5'), (1, 'C6'), (1, 'C7'),
(1, 'D1'), (1, 'D2'), (1, 'D3'), (1, 'D4'), (1, 'D5'), (1, 'D6'), (1, 'D7'),
(1, 'E1'), (1, 'E2'), (1, 'E3'), (1, 'E4'), (1, 'E5'), (1, 'E6'), (1, 'E7'),
(1, 'F1'), (1, 'F2'), (1, 'F3'), (1, 'F4'), (1, 'F5'), (1, 'F6'), (1, 'F7'),
(1, 'G1'), (1, 'G2'), (1, 'G3'), (1, 'G4'), (1, 'G5'), (1, 'G6'), (1, 'G7'),
(1, 'H1'), (1, 'H2'), (1, 'H3'), (1, 'H4'), (1, 'H5'), (1, 'H6'), (1, 'H7'),
(1, 'I1'), (1, 'I2'), (1, 'I3'), (1, 'I4'), (1, 'I5'), (1, 'I6'), (1, 'I7'),
(1, 'J1'), (1, 'J2'), (1, 'J3'), (1, 'J4'), (1, 'J5'), (1, 'J6'), (1, 'J7'),

-- 스크린 2
(2, 'A1'), (2, 'A2'), (2, 'A3'), (2, 'A4'), (2, 'A5'), (2, 'A6'), (2, 'A7'),
(2, 'B1'), (2, 'B2'), (2, 'B3'), (2, 'B4'), (2, 'B5'), (2, 'B6'), (2, 'B7'),
(2, 'C1'), (2, 'C2'), (2, 'C3'), (2, 'C4'), (2, 'C5'), (2, 'C6'), (2, 'C7'),
(2, 'D1'), (2, 'D2'), (2, 'D3'), (2, 'D4'), (2, 'D5'), (2, 'D6'), (2, 'D7'),
(2, 'E1'), (2, 'E2'), (2, 'E3'), (2, 'E4'), (2, 'E5'), (2, 'E6'), (2, 'E7'),
(2, 'F1'), (2, 'F2'), (2, 'F3'), (2, 'F4'), (2, 'F5'), (2, 'F6'), (2, 'F7'),
(2, 'G1'), (2, 'G2'), (2, 'G3'), (2, 'G4'), (2, 'G5'), (2, 'G6'), (2, 'G7'),
(2, 'H1'), (2, 'H2'), (2, 'H3'), (2, 'H4'), (2, 'H5'), (2, 'H6'), (2, 'H7'),
(2, 'I1'), (2, 'I2'), (2, 'I3'), (2, 'I4'), (2, 'I5'), (2, 'I6'), (2, 'I7'),
(2, 'J1'), (2, 'J2'), (2, 'J3'), (2, 'J4'), (2, 'J5'), (2, 'J6'), (2, 'J7');

