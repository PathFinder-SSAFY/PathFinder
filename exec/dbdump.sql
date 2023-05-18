-- --------------------------------------------------------
-- 호스트:                          k8d206.p.ssafy.io
-- 서버 버전:                        8.0.33 - MySQL Community Server - GPL
-- 서버 OS:                        Linux
-- HeidiSQL 버전:                  11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- 테이블 pathfinder.administrator 구조 내보내기
CREATE TABLE IF NOT EXISTS `administrator` (
  `administrator_id` int NOT NULL AUTO_INCREMENT,
  `auth_provider` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
  `naver_id` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
  PRIMARY KEY (`administrator_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- 테이블 데이터 pathfinder.administrator:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `administrator` DISABLE KEYS */;
/*!40000 ALTER TABLE `administrator` ENABLE KEYS */;

-- 테이블 pathfinder.beacon 구조 내보내기
CREATE TABLE IF NOT EXISTS `beacon` (
  `beacon_id` int NOT NULL,
  `beaconx` double NOT NULL,
  `beacony` double NOT NULL,
  `beaconz` double NOT NULL,
  `floors_id` int DEFAULT NULL,
  PRIMARY KEY (`beacon_id`),
  KEY `FKdogrowr7l972g366cc4fi7mwl` (`floors_id`),
  CONSTRAINT `FKdogrowr7l972g366cc4fi7mwl` FOREIGN KEY (`floors_id`) REFERENCES `floors` (`floors_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- 테이블 데이터 pathfinder.beacon:~15 rows (대략적) 내보내기
/*!40000 ALTER TABLE `beacon` DISABLE KEYS */;
INSERT INTO `beacon` (`beacon_id`, `beaconx`, `beacony`, `beaconz`, `floors_id`) VALUES
	(33157, 27.19, 2.38, -1.7, 1),
	(33158, 31.25, 2.38, -5.66, 1),
	(33159, 37.29, 2.38, -5.15, 1),
	(33160, 19, 2.38, -4.99, 1),
	(33164, 24.18, 2.38, -5.33, 1),
	(33233, 32.49, 2.38, -0.12, 1),
	(33234, 21.68, 2.09, -5.62, 1),
	(33235, 17.1, 2.38, -7.86, 1),
	(33236, 29.04, 2.38, -8.61, 1),
	(33240, 22.24, 2.38, -1.7, 1),
	(33241, 23.6, -1.07, -8.93, 1),
	(33242, 24.24, 2.38, -7.84, 1),
	(33246, 19.18, 2.38, -6, 1),
	(33247, 16.26, 2.38, -2.99, 1),
	(33250, 26.84, 2.38, -5.8, 1);
/*!40000 ALTER TABLE `beacon` ENABLE KEYS */;

-- 테이블 pathfinder.block_wall 구조 내보내기
CREATE TABLE IF NOT EXISTS `block_wall` (
  `block_wall_id` int NOT NULL AUTO_INCREMENT,
  `block_wall_left_upx` double DEFAULT NULL,
  `block_wall_left_upz` double DEFAULT NULL,
  `block_wall_right_downx` double DEFAULT NULL,
  `block_wall_right_downz` double DEFAULT NULL,
  `floors_id` int DEFAULT NULL,
  PRIMARY KEY (`block_wall_id`),
  KEY `FK1s4ss8k87epoy3pju91n681bv` (`floors_id`),
  CONSTRAINT `FK1s4ss8k87epoy3pju91n681bv` FOREIGN KEY (`floors_id`) REFERENCES `floors` (`floors_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- 테이블 데이터 pathfinder.block_wall:~25 rows (대략적) 내보내기
/*!40000 ALTER TABLE `block_wall` DISABLE KEYS */;
INSERT INTO `block_wall` (`block_wall_id`, `block_wall_left_upx`, `block_wall_left_upz`, `block_wall_right_downx`, `block_wall_right_downz`, `floors_id`) VALUES
	(1, 15.26, -0.47, 16.26, -6.13, 1),
	(2, 15.26, -7.84, 16.26, -14.44, 1),
	(3, 15.51, -5.075, 21.21, -6.125, 1),
	(4, 22.64, -5.075, 24.24, -6.125, 1),
	(5, 24.245, -5.35, 26.875, -5.85, 1),
	(6, 26.87, -5.075, 27.87, -6.125, 1),
	(7, 37.38, -0.47, 37.88, -6.13, 1),
	(8, 16, -7.84, 18.14, -8.35, 1),
	(9, 19.16, -7.84, 23.76, -8.35, 1),
	(10, 20.01, -7.85, 21.01, -13.85, 1),
	(11, 15.72, -13.32, 23.72, -13.82, 1),
	(12, 23.3, -9.52, 23.8, -15.52, 1),
	(13, 24.78, -7.84, 28.08, -8.35, 1),
	(14, 29.14, -8.35, 31.14, -8.35, 1),
	(15, 24.79, -8.35, 25.29, -9.55, 1),
	(16, 23.81, -9.52, 28.08, -10.02, 1),
	(17, 28.13, -10.92, 29.13, -11.42, 1),
	(18, 29.16, -9.84, 29.66, -15.84, 1),
	(19, 30.64, -8.34, 31.14, -15.34, 1),
	(20, 22.82, -15, 42.82, -15.5, 1),
	(21, 35.48, -8.34, 35.98, -15.34, 1),
	(22, 35.9, -10.92, 36.9, -11.42, 1),
	(23, 37.11, -9.55, 40.41, -10.04, 1),
	(24, 37.11, -7.85, 40.41, -8.35, 1),
	(25, 33.775, -8.35, 36.025, -8.55, 1);
/*!40000 ALTER TABLE `block_wall` ENABLE KEYS */;

-- 테이블 pathfinder.building 구조 내보내기
CREATE TABLE IF NOT EXISTS `building` (
  `building_id` int NOT NULL AUTO_INCREMENT,
  `building_name` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
  `administrator_id` int DEFAULT NULL,
  `properties_id` int DEFAULT NULL,
  PRIMARY KEY (`building_id`),
  KEY `FK8hdna7dfcc9i4fpkmw6bv7htc` (`administrator_id`),
  KEY `FK8b762l4trnyk1db2bvqlbgif` (`properties_id`),
  CONSTRAINT `FK8b762l4trnyk1db2bvqlbgif` FOREIGN KEY (`properties_id`) REFERENCES `properties` (`properties_id`),
  CONSTRAINT `FK8hdna7dfcc9i4fpkmw6bv7htc` FOREIGN KEY (`administrator_id`) REFERENCES `administrator` (`administrator_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- 테이블 데이터 pathfinder.building:~1 rows (대략적) 내보내기
/*!40000 ALTER TABLE `building` DISABLE KEYS */;
INSERT INTO `building` (`building_id`, `building_name`, `administrator_id`, `properties_id`) VALUES
	(1, '구미 삼성 청년 아카데미', NULL, NULL);
/*!40000 ALTER TABLE `building` ENABLE KEYS */;

-- 테이블 pathfinder.customer 구조 내보내기
CREATE TABLE IF NOT EXISTS `customer` (
  `customer_id` int NOT NULL AUTO_INCREMENT,
  `current_location_facility` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- 테이블 데이터 pathfinder.customer:~35 rows (대략적) 내보내기
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` (`customer_id`, `current_location_facility`) VALUES
	(1, '4층 대강의실'),
	(2, NULL),
	(3, '휴게실 401'),
	(4, NULL),
	(5, NULL),
	(6, NULL),
	(7, NULL),
	(8, NULL),
	(9, NULL),
	(10, NULL),
	(11, NULL),
	(12, NULL),
	(13, NULL),
	(14, NULL),
	(15, NULL),
	(16, NULL),
	(17, NULL),
	(18, NULL),
	(19, '4층 대강의실'),
	(20, NULL),
	(21, NULL),
	(22, NULL),
	(23, '휴게실 402'),
	(24, '휴게실 401'),
	(25, NULL),
	(26, '휴게실 402'),
	(27, NULL),
	(28, NULL),
	(29, NULL),
	(30, NULL),
	(31, NULL),
	(32, NULL),
	(33, NULL),
	(34, NULL),
	(35, NULL);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;

-- 테이블 pathfinder.direction 구조 내보내기
CREATE TABLE IF NOT EXISTS `direction` (
  `direction_id` int NOT NULL AUTO_INCREMENT,
  `down_ward` bit(1) DEFAULT NULL,
  `left_ward` bit(1) DEFAULT NULL,
  `right_ward` bit(1) DEFAULT NULL,
  `up_ward` bit(1) DEFAULT NULL,
  `room_map_id` int DEFAULT NULL,
  PRIMARY KEY (`direction_id`),
  KEY `FKgns7wupc2gaxgj8e8l6jukrsg` (`room_map_id`),
  CONSTRAINT `FKgns7wupc2gaxgj8e8l6jukrsg` FOREIGN KEY (`room_map_id`) REFERENCES `room_map` (`room_map_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- 테이블 데이터 pathfinder.direction:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `direction` DISABLE KEYS */;
/*!40000 ALTER TABLE `direction` ENABLE KEYS */;

-- 테이블 pathfinder.emergency_equipment 구조 내보내기
CREATE TABLE IF NOT EXISTS `emergency_equipment` (
  `emergency_id` int NOT NULL AUTO_INCREMENT,
  `emergency_name` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
  `emergency_type` int DEFAULT NULL,
  `emergency_x` double DEFAULT NULL,
  `emergency_y` double DEFAULT NULL,
  `emergency_z` double DEFAULT NULL,
  `floors_id` int DEFAULT NULL,
  PRIMARY KEY (`emergency_id`),
  KEY `FKgqebj9i4kp418uf0t6yh4ju1m` (`floors_id`),
  CONSTRAINT `FKgqebj9i4kp418uf0t6yh4ju1m` FOREIGN KEY (`floors_id`) REFERENCES `floors` (`floors_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- 테이블 데이터 pathfinder.emergency_equipment:~5 rows (대략적) 내보내기
/*!40000 ALTER TABLE `emergency_equipment` DISABLE KEYS */;
INSERT INTO `emergency_equipment` (`emergency_id`, `emergency_name`, `emergency_type`, `emergency_x`, `emergency_y`, `emergency_z`, `floors_id`) VALUES
	(1, '소화기1', 1, 21, 0, -7, 1),
	(2, '자동 심장 충격기', 0, 35, 0, -7, 1),
	(3, '화재 비상 용품', 2, 35, 0, -7, 1),
	(4, '소화기2', 1, 36, 0, -5, 1),
	(5, '소화전', 1, 17, 0, -7, 1);
/*!40000 ALTER TABLE `emergency_equipment` ENABLE KEYS */;

-- 테이블 pathfinder.facility 구조 내보내기
CREATE TABLE IF NOT EXISTS `facility` (
  `facility_id` int NOT NULL AUTO_INCREMENT,
  `density_max` int NOT NULL,
  `facility_left_upx` double DEFAULT NULL,
  `facility_left_upz` double DEFAULT NULL,
  `facility_name` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
  `facility_right_downx` double DEFAULT NULL,
  `facility_right_downz` double DEFAULT NULL,
  `facility_type` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
  `hit_count` int NOT NULL,
  `floors_id` int DEFAULT NULL,
  PRIMARY KEY (`facility_id`),
  KEY `FKc3hjcm6v6n3g65p2kig9mlj2n` (`floors_id`),
  CONSTRAINT `FKc3hjcm6v6n3g65p2kig9mlj2n` FOREIGN KEY (`floors_id`) REFERENCES `floors` (`floors_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- 테이블 데이터 pathfinder.facility:~12 rows (대략적) 내보내기
/*!40000 ALTER TABLE `facility` DISABLE KEYS */;
INSERT INTO `facility` (`facility_id`, `density_max`, `facility_left_upx`, `facility_left_upz`, `facility_name`, `facility_right_downx`, `facility_right_downz`, `facility_type`, `hit_count`, `floors_id`) VALUES
	(1, 0, -1, -1, '강의실 401', -1, -1, 'CLASSROOM401', 100, 1),
	(2, 0, -1, -1, '강의실 402', -1, -1, 'CLASSROOM402', 0, 1),
	(3, 0, -1, -1, '강의실 403', -1, -1, 'CLASSROOM403', 50, 1),
	(4, 2, 0, 0, '4층 대강의실', 15.26, -13.86, 'CLASSROOMBIG', 30, 1),
	(5, 0, 23.6, -9.96, '4층 남자 화장실', 29.23, -14.99, 'RESTROOMMAN', 0, 1),
	(6, 0, 35.78, -9.96, '4층 여자 화장실', 41.45, -14.99, 'RESTROOMWOMAN', 20, 1),
	(7, 2, 16.38, -0.09, '휴게실 401', 22.52, -5.25, 'BREAKROOM401', 0, 1),
	(8, 2, 22.52, -0.09, '휴게실 402', 27.71, -5.25, 'BREAKROOM402', 0, 1),
	(9, 0, 27.71, -0.09, '휴게실 403', 37.33, -5.25, 'BREAKROOM403', 0, 1),
	(10, 0, 21.05, -7.86, '4층 왼쪽 계단', 23.45, -13.86, 'STAIRSLEFT', 20, 1),
	(11, 0, -1, -1, '4층 오른쪽 계단', -1, -1, 'STAIRSRIGHT', 30, 1),
	(12, 0, 30.9, -8.68, '엘리베이터', 33.92, -14.99, 'ELEVATOR', 0, 1);
/*!40000 ALTER TABLE `facility` ENABLE KEYS */;

-- 테이블 pathfinder.floating_popularity 구조 내보내기
CREATE TABLE IF NOT EXISTS `floating_popularity` (
  `floating_id` int NOT NULL AUTO_INCREMENT,
  `time` datetime(6) DEFAULT NULL,
  `facility_id` int DEFAULT NULL,
  PRIMARY KEY (`floating_id`),
  KEY `FKngscjp6heo7xyk4pmu9ps3a8e` (`facility_id`),
  CONSTRAINT `FKngscjp6heo7xyk4pmu9ps3a8e` FOREIGN KEY (`facility_id`) REFERENCES `facility` (`facility_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- 테이블 데이터 pathfinder.floating_popularity:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `floating_popularity` DISABLE KEYS */;
/*!40000 ALTER TABLE `floating_popularity` ENABLE KEYS */;

-- 테이블 pathfinder.floors 구조 내보내기
CREATE TABLE IF NOT EXISTS `floors` (
  `floors_id` int NOT NULL AUTO_INCREMENT,
  `floor_number` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
  `map_image_url` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
  `building_id` int DEFAULT NULL,
  PRIMARY KEY (`floors_id`),
  KEY `FKnvlu4u3n1e6ld2ae8sicswgnp` (`building_id`),
  CONSTRAINT `FKnvlu4u3n1e6ld2ae8sicswgnp` FOREIGN KEY (`building_id`) REFERENCES `building` (`building_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- 테이블 데이터 pathfinder.floors:~1 rows (대략적) 내보내기
/*!40000 ALTER TABLE `floors` DISABLE KEYS */;
INSERT INTO `floors` (`floors_id`, `floor_number`, `map_image_url`, `building_id`) VALUES
	(1, '4', 'https://d206-buket.s3.ap-northeast-2.amazonaws.com/3floor_map.png', 1);
/*!40000 ALTER TABLE `floors` ENABLE KEYS */;

-- 테이블 pathfinder.properties 구조 내보내기
CREATE TABLE IF NOT EXISTS `properties` (
  `properties_id` int NOT NULL AUTO_INCREMENT,
  `field` int NOT NULL,
  PRIMARY KEY (`properties_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- 테이블 데이터 pathfinder.properties:~1 rows (대략적) 내보내기
/*!40000 ALTER TABLE `properties` DISABLE KEYS */;
INSERT INTO `properties` (`properties_id`, `field`) VALUES
	(1, 1);
/*!40000 ALTER TABLE `properties` ENABLE KEYS */;

-- 테이블 pathfinder.room_entrance 구조 내보내기
CREATE TABLE IF NOT EXISTS `room_entrance` (
  `room_coordinates_id` int NOT NULL AUTO_INCREMENT,
  `a_star_coordinatex` double DEFAULT NULL,
  `a_star_coordinatey` double DEFAULT NULL,
  `a_star_coordinatez` double DEFAULT NULL,
  `entrance_direction` int DEFAULT NULL,
  `entrance_left_upx` double DEFAULT NULL,
  `entrance_left_upz` double DEFAULT NULL,
  `entrance_right_downx` double DEFAULT NULL,
  `entrance_right_downz` double DEFAULT NULL,
  `entrance_zone` double DEFAULT NULL,
  `facility_id` int DEFAULT NULL,
  PRIMARY KEY (`room_coordinates_id`),
  KEY `FKbta8gms9tj2sgth5sdvbfdl71` (`facility_id`),
  CONSTRAINT `FKbta8gms9tj2sgth5sdvbfdl71` FOREIGN KEY (`facility_id`) REFERENCES `facility` (`facility_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- 테이블 데이터 pathfinder.room_entrance:~12 rows (대략적) 내보내기
/*!40000 ALTER TABLE `room_entrance` DISABLE KEYS */;
INSERT INTO `room_entrance` (`room_coordinates_id`, `a_star_coordinatex`, `a_star_coordinatey`, `a_star_coordinatez`, `entrance_direction`, `entrance_left_upx`, `entrance_left_upz`, `entrance_right_downx`, `entrance_right_downz`, `entrance_zone`, `facility_id`) VALUES
	(1, 0, 0, 0, 3, 0, 0, 0, 0, 0.2, 1),
	(2, 0, 0, 0, 3, 0, 0, 0, 0, 0.2, 2),
	(3, 0, 0, 0, 3, 0, 0, 0, 0, 0.2, 3),
	(4, 15.25, 0, -7, 2, 15.51, -6.32, 16.009, -8, 0.2, 4),
	(5, 28.75, 0, -10, 1, 27.88, -10, 28.38, -11, 0.2, 5),
	(6, 36.5, 0, -10, 1, 36.77, -10, 37.27, -11, 0.2, 6),
	(7, 22, 0, -5.5, 3, 21.22, -5.05, 22.62, -6.05, 0.2, 7),
	(8, 22, 0, -5.5, 3, 21.22, -5.05, 22.62, -6.05, 0.2, 8),
	(9, 30, 0, -5.5, 3, 27.89, -5.05, 37.39, -6.05, 0.2, 9),
	(10, 24, 0, -7.5, 1, 23.77, -7.85, 24.77, -8.35, 0.2, 10),
	(11, 0, 0, 0, 3, 0, 0, 0, 0, 0.2, 11),
	(12, 32, 0, -9, 1, 31.15, -8.35, 33.75, -8.85, 0.2, 12);
/*!40000 ALTER TABLE `room_entrance` ENABLE KEYS */;


-- 테이블 pathfinder.room_map 구조 내보내기
CREATE TABLE IF NOT EXISTS `room_map` (
  `room_map_id` int NOT NULL AUTO_INCREMENT,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `floors_id` int DEFAULT NULL,
  PRIMARY KEY (`room_map_id`),
  KEY `FKkydbmr8rdkp323uskqwex5txi` (`floors_id`),
  CONSTRAINT `FKkydbmr8rdkp323uskqwex5txi` FOREIGN KEY (`floors_id`) REFERENCES `floors` (`floors_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- 테이블 데이터 pathfinder.room_map:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `room_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `room_map` ENABLE KEYS */;
-- 테이블 pathfinder.weight 구조 내보내기
CREATE TABLE IF NOT EXISTS `weight` (
  `weight_id` int NOT NULL AUTO_INCREMENT,
  `denesly` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
  `hardness` varchar(255) COLLATE utf8mb3_bin DEFAULT NULL,
  `room_map_id` int DEFAULT NULL,
  PRIMARY KEY (`weight_id`),
  KEY `FKhaobc3o1daw4tn1018m2q7sgc` (`room_map_id`),
  CONSTRAINT `FKhaobc3o1daw4tn1018m2q7sgc` FOREIGN KEY (`room_map_id`) REFERENCES `room_map` (`room_map_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

-- 테이블 데이터 pathfinder.weight:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `weight` DISABLE KEYS */;
/*!40000 ALTER TABLE `weight` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
