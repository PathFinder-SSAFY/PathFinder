-- 시설
INSERT INTO facility
(facility_type, facility_name, density_max, hit_count)
VALUES
    ("CLASSROOM401", "강의실 401", 200, 100),
    ("CLASSROOM402", "강의실 402", 200, 0),
    ("CLASSROOM403", "강의실 403", 200, 50),
    ("CLASSROOMBIG", "4층 대강의실", 200, 30),
    ("RESTROOMMAN", "4층 남자 화장실", 200, 0),
    ("RESTROOMWOMAN", "4층 여자 화장실", 200, 20),
    ("BREAKROOM", "4층 휴게실", 200, 0),
     ("STAIRSLEFT", "4층 왼쪽 계단", 200, 20),
    ("STAIRSRIGHT", "4층 오른쪽 계단", 200, 30),
    ("ELEVATOR", "엘리베이터", 200, 0);


-- 방 입구 좌표
INSERT INTO room_entrance
(entrance_left_up_x, entrance_left_up_y, entrance_right_down_x, entrance_right_down_y, entrance_direction, entrance_zone, facility_id)
VALUES
    (15.509, -6.32, 16.009, -8.00, 2, 0.20, 4),
    (27.88, -10.09, 28.38, -10.9, 1, 0.20, 5),
    (36.77, -10.09, 37.27, -10.9, 1, 0.20, 6),
    (21.22, -5.05, 22.62, -6.05, 3, 0.20, 7),
    (27.89, -5.05, 37.39, -6.05, 3, 0.20, 7),
    (24.77, -7.85, 23.77, -8.35, 1, 20, 8),
    (31.15, -8.35, 33.75, -8.85, 1, 20, 10);
