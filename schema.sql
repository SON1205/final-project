-- 데이터베이스 초기화
DROP DATABASE IF EXISTS ssafy_urs;
CREATE DATABASE ssafy_urs;
USE ssafy_urs;

-- users 테이블
CREATE TABLE `Users` (
    `user_id` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `name` VARCHAR(100) NOT NULL,
    `phone` VARCHAR(15) NOT NULL,
    PRIMARY KEY (`user_id`)
);

-- Route 테이블
CREATE TABLE `Route` (
    `route_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `district_name` VARCHAR(100) NOT NULL,
    `theme_name` VARCHAR(50) NOT NULL,
    `duration_time` INT UNSIGNED NOT NULL COMMENT '소요 시간(분 단위)',
    `image_url` VARCHAR(255) DEFAULT NULL COMMENT '대표 이미지 URL',
    `average_rating` DECIMAL(3, 2) DEFAULT 0 COMMENT '평균 평점',
    PRIMARY KEY (`route_id`)
);

-- Completed_Route 테이블
CREATE TABLE `Completed_Route` (
    `user_id` VARCHAR(255) NOT NULL,
    `route_id` INT UNSIGNED NOT NULL,
    `starting_img_url` VARCHAR(255) DEFAULT NULL COMMENT '출발지 이미지 경로',
    `middle_img_url` VARCHAR(255) DEFAULT NULL COMMENT '중간지점 이미지 경로',
    `ending_img_url` VARCHAR(255) DEFAULT NULL COMMENT '도착지 이미지 경로',
    PRIMARY KEY (`user_id`, `route_id`),
    FOREIGN KEY (`user_id`) REFERENCES `Users`(`user_id`),
    FOREIGN KEY (`route_id`) REFERENCES `Route`(`route_id`)
);

-- Bookmark 테이블
CREATE TABLE `Bookmark` (
    `user_id` VARCHAR(255) NOT NULL,
    `route_id` INT UNSIGNED NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '북마크 생성 시간',
    PRIMARY KEY (`user_id`, `route_id`),
    FOREIGN KEY (`user_id`) REFERENCES `Users`(`user_id`),
    FOREIGN KEY (`route_id`) REFERENCES `Route`(`route_id`)
);

-- Review 테이블
CREATE TABLE `Review` (
    `review_id` INT NOT NULL AUTO_INCREMENT,
    `user_id` VARCHAR(255) NOT NULL,
    `route_id` INT UNSIGNED NOT NULL,
    `reg_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `rating` INT UNSIGNED NOT NULL CHECK (`rating` BETWEEN 0 AND 5) COMMENT '평점 (0~5)',
    PRIMARY KEY (`review_id`),
    FOREIGN KEY (`user_id`) REFERENCES `Users`(`user_id`),
    FOREIGN KEY (`route_id`) REFERENCES `Route`(`route_id`)
);

-- Point 테이블 (위치 정보 분리)
CREATE TABLE `Point` (
    `point_id` INT NOT NULL AUTO_INCREMENT,
    -- `point_name` VARCHAR(255) NOT NULL COMMENT '이름', 
    `latitude` DECIMAL(9, 6) NOT NULL COMMENT '위도',
    `longitude` DECIMAL(9, 6) NOT NULL COMMENT '경도',
    PRIMARY KEY (`point_id`)
);

-- Location 테이블 (Route와 Point 간 관계)
CREATE TABLE `Location` (
    `route_id` INT UNSIGNED NOT NULL,
    `point_id` INT NOT NULL,
    `type` ENUM('STARTING', 'MIDDLE', 'ENDING') NOT NULL COMMENT '위치 유형',
    PRIMARY KEY (`route_id`, `point_id`, `type`),
    FOREIGN KEY (`route_id`) REFERENCES `Route`(`route_id`),
    FOREIGN KEY (`point_id`) REFERENCES `Point`(`point_id`)
);

-- 트리거 생성
-- 리뷰 삽입
DROP TRIGGER IF EXISTS `after_review_insert`;
-- DROP TRIGGER IF EXISTS `after_review_delete`;

DELIMITER $$

CREATE TRIGGER `after_review_insert`
AFTER INSERT ON `Review`
FOR EACH ROW
BEGIN
    -- 평균 평점을 저장할 변수 선언 및 초기화
    DECLARE avg_rating DECIMAL(3, 2) DEFAULT 0.00;

    -- 평균 평점 계산 및 변수에 저장
    SELECT COALESCE(CAST(AVG(rv.rating) AS DECIMAL(3, 2)), 0.00)
    INTO avg_rating
    FROM `Review` rv
    WHERE rv.route_id = NEW.route_id;

    -- Route 테이블의 average_rating 업데이트
    UPDATE `Route`
    SET `average_rating` = avg_rating
    WHERE `route_id` = NEW.route_id;
END$$

DELIMITER ;

-- 더미데이터 삽입
INSERT INTO `Users` (`user_id`, `password`, `name`, `phone`) VALUES
('user1', 'password1', 'Alice', '01012345678'),
('user2', 'password2', 'Bob', '01023456789'),
('user3', 'password3', 'Charlie', '01034567890'),
('user4', 'password4', 'David', '01045678901'),
('user5', 'password5', 'Eve', '01056789012');

INSERT INTO `Route` (`district_name`, `theme_name`, `duration_time`, `image_url`, `average_rating`) VALUES
('종로구', 'CALMLY', 25, 'https://i.namu.wiki/i/BlYXTmnqSHYx28wBPfXxbPGsm8kMIJHlUCa-gtyLXVtTDVA9UkX8DlhLtpV8ZrAJjDmsrCqSQN2IPGApnbawVfCqOPiD2u9Hf7DIbMxAsz1MEvyVHknQTzsH10GQ-JGG5FLUq144CA5ufZ8IylgrBg.webp', 4.5),
('중구', 'CALMLY', 90, 'http://example.com/busan_cityscape.jpg', 3.8),
('용산구', 'ALONE', 180, 'http://example.com/jeju_adventure.jpg', 4.7),
('동대문구', 'LIVELY', 60, 'http://example.com/incheon_historical.jpg', 4.1),
('광진구', 'Cultural', 75, 'http://example.com/daegu_cultural.jpg', 3.5),
('종로구', 'DEMURE', 120, 'http://example.com/seoul_nature.jpg', 4.5),
('중구', 'CALMLY', 90, 'http://example.com/busan_cityscape.jpg', 3.8),
('용산구', 'ALONE', 180, 'http://example.com/jeju_adventure.jpg', 4.7),
('동대문구', 'LIVELY', 60, 'http://example.com/incheon_historical.jpg', 4.1),
('광진구', 'Cultural', 75, 'http://example.com/daegu_cultural.jpg', 3.5);

INSERT INTO `Review` (`user_id`, `route_id`, `reg_date`, `rating`) VALUES
('user1', 1, NOW(), 5),
('user2', 1, NOW(), 4),
('user3', 1, NOW(), 4),
('user4', 2, NOW(), 3),
('user5', 2, NOW(), 4),
('user1', 3, NOW(), 5),
('user2', 3, NOW(), 4),
('user3', 3, NOW(), 5),
('user4', 4, NOW(), 4),
('user5', 4, NOW(), 4),
('user1', 5, NOW(), 3),
('user2', 5, NOW(), 4);

INSERT INTO `Completed_Route` (`user_id`, `route_id`, `starting_img_url`, `middle_img_url`, `ending_img_url`) VALUES
('user1', 1, 'http://example.com/start1.jpg', 'http://example.com/middle1.jpg', 'http://example.com/end1.jpg'),
('user2', 2, 'http://example.com/start2.jpg', 'http://example.com/middle2.jpg', 'http://example.com/end2.jpg'),
('user3', 3, 'http://example.com/start3.jpg', 'http://example.com/middle3.jpg', 'http://example.com/end3.jpg'),
('user4', 4, 'http://example.com/start4.jpg', 'http://example.com/middle4.jpg', 'http://example.com/end4.jpg'),
('user5', 5, 'http://example.com/start5.jpg', 'http://example.com/middle5.jpg', 'http://example.com/end5.jpg');

INSERT INTO `Bookmark` (`user_id`, `route_id`, `created_at`) VALUES
('user1', 1, NOW()),
('user2', 2, NOW()),
('user3', 3, NOW()),
('user4', 4, NOW()),
('user5', 5, NOW());

INSERT INTO `Point` (`latitude`, `longitude`) VALUES
(37.577462, 126.991932), 
(37.569737, 126.995222),
(37.569348, 126.999860),
(37.4563, 126.7052), -- Incheon
(35.8714, 128.6014), -- Daegu
(37.5665, 126.9780), -- Seoul
(35.1796, 129.0756), -- Busan
(33.4996, 126.5312), -- Jeju
(37.4563, 126.7052), -- Incheon
(35.8714, 128.6014), -- Daegu
(37.5665, 126.9780), -- Seoul
(35.1796, 129.0756), -- Busan
(33.4996, 126.5312), -- Jeju
(37.4563, 126.7052), -- Incheon
(35.8714, 128.6014); -- Daegu

INSERT INTO `Location` (`route_id`, `point_id`, `type`) VALUES
(1, 1, 'STARTING'),
(1, 2, 'MIDDLE'),
(1, 3, 'STARTING'),
(4, 4, 'STARTING'),
(5, 5, 'STARTING'),
(2, 6, 'MIDDLE'),
(2, 7, 'MIDDLE'),
(3, 8, 'MIDDLE'),
(4, 9, 'MIDDLE'),
(5, 10, 'MIDDLE'),
(2, 11, 'ENDING'),
(2, 12, 'ENDING'),
(3, 13, 'ENDING'),
(4, 14, 'ENDING'),
(5, 15, 'ENDING');

select * from Users;
-- 더미 데이터 확인
-- SELECT * FROM `Review`;
-- SELECT * FROM `Completed_Route`;
-- SELECT * FROM `Bookmark`;
-- SELECT * FROM `Point`;
-- SELECT * FROM `Location`;

-- SELECT * FROM `Route`;

-- 트리거 확인
-- 트리거 테스트: 리뷰 추가
-- INSERT INTO `Review` (`user_id`, `route_id`, `reg_date`, `rating`) VALUES
-- ('user4', 1, NOW(), 5);