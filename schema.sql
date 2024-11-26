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
    `middle_img_url` VARCHAR(255) DEFAULT NULL COMMENT '중간지점 이미지 경로',
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
('종로구', 'CALMLY', 15, 'https://i.namu.wiki/i/-qkEuOnwE_wiiHg9O0zZXYQ429-zmqmNUNkEb4gY4xW56Fy5VPhJmq-zSWrcGNMWlvpGhin1jYDKO2vc2fBobn2h_DW1dynR51JlLDnJ3M_cYup7n39hzCGNeAU4iGWhcKMEwoNyzFzWi9JBseTQ0g.webp', 3.8),
('종로구', 'CALMLY', 45, 'https://i.namu.wiki/i/bk-wTlTaSBZL3BrOrCoF4BR67QGm88WtZIOMryHWOjKZFcWAoExM_F2jOTQVCCSmqBmTlAc82wxLEQTQkg8ujURDAtiaVsYJjmqfb0ef5GA5YmoiWT1MSNwc_iknoqjdZSSMo3IKS_VSvn53cgL4Vg.webp', 4.7),
('종로구', 'ALONE', 15, 'https://i.namu.wiki/i/eeD7e6BzcQ9rq-FEWWSNg5lGMaHzFTY3WsRVQqs1TX3iCxp_9ofxN8ZDzjzuhlPB2HEqK6HrHdvIAxzdwEcFDrvLZLkR9px1YFu-hacRNCYnmSf5ueDJZhwni6-_YO3pGnM-im-12wGaVDzfboiJ-w.webp', 4.1),
('종로구', 'ALONE', 45, 'https://i.namu.wiki/i/M1FlR-gNpPH2768h07zpc9-rW2kgncuDh-GE9yhKTV_4BRQuBIfl3XCjYeICDYg4zFBxnOWqCWh7NcaRO4PKBV6NwWT4weUSDfX_T2Q8p69BAsZ4JrU4I0EvKaptter3h01TtoA974HUattmGj8Rrw.webp', 3.5),
('종로구', 'ALONE', 25, 'https://i.namu.wiki/i/hxOWTqJVP_GbsPrMdB4w0WOYG7h8deJQRTCqLsl5WsJMFFHyUxAjZR1X2OT7DcBAkHbCmCX6qmSH6idsxHYxHvdU_XLldxtJpx23nYq3hEwAKWESIhe-tQJUPtOr5Zh7P8uJPR1KdaxTm2pZXe3ZdA.webp', 4.5),
('종로구', 'DEMURE', 25, 'https://i.namu.wiki/i/9x5o3R8eRnjWUT0fqMZBe1_BdQFs_SL-GWpvfMoqubkvn63KEzN9ic6dEdY9HT-lBeq8fDS3cxx6S7TNUsvTrRPGRix70QqIIOZmAw_bzqGSam84BCSOxh4BiQvm7EjIphVqYht8d2bjriypUHPHfw.webp', 3.8),
('종로구', 'LIVELY', 25, 'https://i.namu.wiki/i/Ve6OGYjprHNxHvnZCcSnRNmceOquI_OcsiQu6lPSFg2-oSZPm5OkQ_j_n2CA1hbcfYI3fAEQkfOpI9cjJc0IBE4foY_Lo9rxcW9lOA-S-hOVF2LjIPWE-QBZw9_Z6jmY0rqnXRPAngg4bXCrL6TfKA.webp', 4.7),
('종로구', 'LIVELY', 45, 'https://i.namu.wiki/i/lUPvkIFo2NuvTxgTBNx26ytTKQlnZ4Apf64w5L4BXYMwEfr_gbHyDD94YsI9tZDeIiL5KQOERSkdFK174jxeqLExGorLouwsITagV9EcsBkGAZkXP5vHLDAMNPSRWQ2YMw_cYxAW_bAWatzs6OmcPA.webp', 4.1),
('종로구', 'DEMURE', 15, 'https://i.namu.wiki/i/BOntcAhkVK3vifAY-PweYDTo_KZgiFmsnvqKggBQVZ48_19k-SXuuEjw6w6ZE-jSYd_eT19o5I2yYZd7KfT0m9_C2ioSlxhW1Wq0VpjiFlUqDp7Chdb_tP4VqIMusMZT1g_Gi-Q_tGN_rJe5uPXjAg.webp', 3.5),
('종로구', 'DEMURE', 45, 'https://i.namu.wiki/i/UIJErJHZkrrMmeB4JGh5MoLHmrK9Z1ao-8QPVk_iWN38fbR4HW47N6dm5ykTzHWXBsUbPYAtdvcJH_DxKpQiJa0neNUDXlCAQbV-_jH03CvfQGf8dMUhceKhwAiCvFE_pVRj2jze5p4dsBlo3x6-iA.webp', 3.5),
('종로구', 'LIVELY', 15, 'https://i.namu.wiki/i/YR_1sCHBjB1OFGT6k41AGtatZgO-uw_6pgpQPvzAFe1nVoG_5CkGFZChzEdDB-JnJD2dY0-ShAIj1pny2ZCc_Iea7nvcDo8bx9b5_LWuOPK9HtO-GhfSDKh5A-u8hAVIxpqmLMdp2bCwd6b2vPhjdw.webp', 4.5),
('광진구', 'CALMLY', 15, 'https://i.namu.wiki/i/h6uz4e-3G8hcxcYbYIV_GdX4CH4qaLWaANoh5Blr_AdO_TTWlPYfNxZ4Y2t0-bfwolwnfFFeBuyE69uHumz7m8JgefHw62IPb6nKmDX962dgm-xwbBYPL-2sql8ADAXXwAqlxLtk-tAL7kAunokK1g.webp', 3.8),
('용산구', 'ALONE', 25, 'https://i.namu.wiki/i/3XdmvqNteCaYZuY_KkTzp21ORQE2Bj-3vfwvkOhCabPK1QYjOiRueBDPv86Zpifg2tDMnqovPZZbqTJGTOeZSkmsGqW2VtxcZewG5W_5L2waupAKjDlJtAOrATuEK5OUwtgY5uoW4Hkhv3uWrOcDAg.webp', 4.7),
('금천구', 'LIVELY', 15, 'https://i.namu.wiki/i/9hmZ86xBzJYIVoE4PwMOXWuDezF5BRtbGnkr__C9hFfFDKs7ZczqnYFWgEvU7aMsPH59-fRc2IXsifh3iGsUoFyNgez9Iu082YjL5v4eybSk2YV67QjY7kq1FQlR-ekbHL-7vqYuie3tqRu_WaQU1Q.webp', 2.1),
('서초구', 'DEMURE', 45, 'https://i.namu.wiki/i/Evytt2HjwZZnOh7te-rS0ldxBiPXhKVJo65Q6_5T5wN_b_L9hMRsi46LvTyDJiNDmqyosxSTRYXSNW4RIdtd4-dNZA777uoctHWBqbi7E6h7q2QXLHcArVaf34ZhVKHAGaIjKiY1PbKFr3fpfSqOcQ.webp', 3.5),
('광진구', 'CALMLY', 45, 'https://i.namu.wiki/i/Ohd-riWx0hIHLcGOHAfIJUI6ha25Jj9Ce5fr0OnyVmCeYYYz2ReZBZGAy157Lh1rPs_N80Z6_orl1wam1N5fHAFP-z9ZUeBqJ77EViSqn3Dfsw8QIqDEv7k-Ryg13mWd5M6EjtkYN2ZcQccucKPz1A.webp', 4.3),
('용산구', 'ALONE', 15, 'https://i.namu.wiki/i/rY73sLCyYmVwkjjEDmIcVxfjXEx5Uro_F-JVSq60ZyZTcJ0e6hT4-g5QC39kb6-_jnw3Lf2g7uoeUzVEuOddByeL5aTMVx8tNuu-nNG4knV8Bc_ofAGk4qnC24kszEqSTzui8ykqClJskU3VXSLtZA.webp', 4.2),
('금천구', 'LIVELY', 25, 'https://i.namu.wiki/i/qEAwwyxcvYJiG0HqWoALPFRuL6WrlcRgm3OzeekRvxv6DIdBaAYaarXZ7ystVkreahiyRTXETGzfGgZeI5rcEycJ-FyLMEbSZi_NRxFIxHxfMU0J38oPVWqiUP3j3PQ1k0iALJazbK6yeZCttD2tuw.webp', 4.1),
('서초구', 'DEMURE', 15, 'https://i.namu.wiki/i/zp86MqpqZLtn-iGEF3mOjCVmBmoUuj3hGwZkSHrXEMWhUbLEFEnevhof792adjR9EOCjHGMRWGV0LcIuEWzGZ2xs4yUy0eb3WZcdQuJyNaLsFmdWK6d72OrYpPuCe66F4x8fn5hr5eDsZIW-ZDSk0A.webp', 4.5);

INSERT INTO `Review` (`user_id`, `route_id`, `reg_date`, `rating`) VALUES
('user2', 1, NOW(), 4),
('user3', 1, NOW(), 4),
('user4', 2, NOW(), 3),
('user5', 2, NOW(), 4),
('user1', 3, NOW(), 5),
('user2', 3, NOW(), 4),
('user3', 3, NOW(), 5),
('user4', 4, NOW(), 4),
('user5', 6, NOW(), 4),
('user1', 7, NOW(), 3),
('user2', 8, NOW(), 4),
('user1', 11, NOW(), 2),
('user1', 12, NOW(), 3),
('user1', 9, NOW(), 4),
('user1', 8, NOW(), 5),
('user1', 14, NOW(), 4),
('user1', 17, NOW(), 5);

INSERT INTO `Completed_Route` (`user_id`, `route_id`, `middle_img_url`) VALUES
('user2', 1, "C:\\Users\\sonjh\\Desktop\\workspace\\final-project\\uploadImg\\user2_1_aaa.webp"),
('user3', 1, "C:\\Users\\sonjh\\Desktop\\workspace\\final-project\\uploadImg\\user3_1_aaaaa.webp"),
('user4', 2, "C:\\Users\\sonjh\\Desktop\\workspace\\final-project\\uploadImg\\user4_2_asdf.webp"),
('user5', 2, "C:\\Users\\sonjh\\Desktop\\workspace\\final-project\\uploadImg\\user5_2_asdfxc.webp"),
('user1', 3, "C:\\Users\\sonjh\\Desktop\\workspace\\final-project\\uploadImg\\user1_3_bbb.webp"),
('user2', 3, "C:\\Users\\sonjh\\Desktop\\workspace\\final-project\\uploadImg\\user2_3_ccc.webp"),
('user3', 3, "C:\\Users\\sonjh\\Desktop\\workspace\\final-project\\uploadImg\\user3_3_dddd.webp"),
('user4', 4, "C:\\Users\\sonjh\\Desktop\\workspace\\final-project\\uploadImg\\user4_4_dddd.webp"),
('user5', 6, "C:\\Users\\sonjh\\Desktop\\workspace\\final-project\\uploadImg\\user5_6_jilkbnvbcdder.webp"),
('user1', 7, "C:\\Users\\sonjh\\Desktop\\workspace\\final-project\\uploadImg\\user1_7_jilkbnvbcdder.webp"),
('user2', 8, "C:\\Users\\sonjh\\Desktop\\workspace\\final-project\\uploadImg\\user2_8_kjlh.webp"),
('user1', 11, "C:\\Users\\sonjh\\Desktop\\workspace\\final-project\\uploadImg\\user1_11_qterjhgf.webp"),
('user1', 12, "C:\\Users\\sonjh\\Desktop\\workspace\\final-project\\uploadImg\\user1_12_abc.webp"),
('user1', 9, "C:\\Users\\sonjh\\Desktop\\workspace\\final-project\\uploadImg\\user1_9_weq.webp"),
('user1', 8, "C:\\Users\\sonjh\\Desktop\\workspace\\final-project\\uploadImg\\user1_8_ccc.webp"),
('user1', 14, "C:\\Users\\sonjh\\Desktop\\workspace\\final-project\\uploadImg\\user1_14_kjlh.webp"),
('user1', 17, "C:\\Users\\sonjh\\Desktop\\workspace\\final-project\\uploadImg\\user1_17_aaaaa.webp");

INSERT INTO `Bookmark` (`user_id`, `route_id`, `created_at`) VALUES
('user1', 2, NOW()),
('user2', 2, NOW()),
('user3', 3, NOW()),
('user4', 4, NOW()),
('user5', 5, NOW());

INSERT INTO `Point` (`latitude`, `longitude`) VALUES
(37.577462, 126.991932), 
(37.569737, 126.995222),
(37.569348, 126.999860),
(37.577519, 126.989070),
(37.580221, 126.986369),
(37.582247, 126.987374),
(37.583587, 126.974076),
(37.583518, 126.986873),
(37.600473, 126.958629),
(37.600275, 126.962801),
(37.603864, 126.962629),
(37.582139, 126.983505),
(37.583902, 126.984840),
(37.594909, 126.990797),
(35.8714, 128.6014), -- Daegu
(35.8714, 128.6014), -- Daegu
(37.5665, 126.9780), -- Seoul
(35.1796, 129.0756), -- Busan
(33.4996, 126.5312), -- Jeju
(37.4563, 126.7052), -- Incheon
(35.8714, 128.6014), -- Daegu
(35.8714, 128.6014), -- Daegu
(37.5665, 126.9780), -- Seoul
(35.1796, 129.0756), -- Busan
(33.4996, 126.5312), -- Jeju
(37.4563, 126.7052), -- Incheon
(35.8714, 128.6014),
(35.8714, 128.6014), -- Daegu
(35.8714, 128.6014), -- Daegu
(37.5665, 126.9780), -- Seoul
(35.1796, 129.0756), -- Busan
(33.4996, 126.5312), -- Jeju
(37.4563, 126.7052), -- Incheon
(35.8714, 128.6014), -- Daegu
(35.8714, 128.6014), -- Daegu
(37.5665, 126.9780), -- Seoul
(35.1796, 129.0756), -- Busan
(33.4996, 126.5312), -- Jeju
(37.4563, 126.7052), -- Incheon
(35.8714, 128.6014), -- Daegu
(35.8714, 128.6014), -- Daegu
(35.8714, 128.6014), -- Daegu
(37.5665, 126.9780), -- Seoul
(35.1796, 129.0756), -- Busan
(33.4996, 126.5312), -- Jeju
(37.4563, 126.7052), -- Incheon
(35.8714, 128.6014), -- Daegu
(35.8714, 128.6014), -- Daegu
(37.5665, 126.9780), -- Seoul
(35.1796, 129.0756), -- Busan
(33.4996, 126.5312), -- Jeju
(37.4563, 126.7052), -- Incheon
(35.8714, 128.6014); -- Daegu

INSERT INTO `Location` (`route_id`, `point_id`, `type`) VALUES
(1, 1, 'STARTING'),
(1, 2, 'MIDDLE'),
(1, 3, 'ENDING'),
(2, 4, 'STARTING'),
(2, 5, 'MIDDLE'),
(2, 6, 'ENDING'),
(3, 7, 'STARTING'),
(3, 8, 'MIDDLE'),
(3, 9, 'ENDING'),
(4, 10, 'STARTING'),
(4, 11, 'MIDDLE'),
(4, 12, 'ENDING'),
(5, 13, 'STARTING'),
(5, 14, 'MIDDLE'),
(5, 15, 'ENDING'),
(6, 16, 'STARTING'),
(6, 17, 'MIDDLE'),
(6, 18, 'ENDING'),
(7, 19, 'STARTING'),
(7, 20, 'MIDDLE'),
(7, 21, 'ENDING'),
(8, 22, 'STARTING'),
(8, 23, 'MIDDLE'),
(8, 24, 'ENDING'),
(9, 25, 'STARTING'),
(9, 26, 'MIDDLE'),
(9, 27, 'ENDING'),
(10, 28, 'STARTING'),
(10, 29, 'MIDDLE'),
(10, 30, 'ENDING'),
(11, 31, 'STARTING'),
(11, 32, 'MIDDLE'),
(11, 33, 'ENDING'),
(12, 34, 'STARTING'),
(12, 35, 'MIDDLE'),
(12, 36, 'ENDING'),
(13, 37, 'STARTING'),
(13, 38, 'MIDDLE'),
(13, 39, 'ENDING'),
(14, 40, 'STARTING'),
(14, 41, 'MIDDLE'),
(14, 42, 'ENDING'),
(15, 43, 'STARTING'),
(15, 44, 'MIDDLE'),
(15, 45, 'ENDING'),
(16, 46, 'STARTING'),
(16, 47, 'MIDDLE'),
(16, 48, 'ENDING');

select * from Users;
-- 더미 데이터 확인
-- SELECT * FROM `Review`;
SELECT * FROM `Completed_Route`; 
-- SELECT * FROM `Bookmark`;
-- SELECT * FROM `Point`;
-- SELECT * FROM `Location`;

-- SELECT * FROM `Route`;

-- 트리거 확인
-- 트리거 테스트: 리뷰 추가
-- INSERT INTO `Review` (`user_id`, `route_id`, `reg_date`, `rating`) VALUES
-- ('user4', 1, NOW(), 5);