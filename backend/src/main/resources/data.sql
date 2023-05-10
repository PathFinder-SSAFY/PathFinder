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



    val entranceLeftUpX: Double ?= 0.0,
    val entranceLeftUpY: Double ?= 0.0,
    val entranceRightDownX: Double ?= 0.0,
    val entranceRightDownY: Double ?= 0.0,
    val entranceDirection: Int ? = 0, // 1: 윗 방향, 2: 오른쪽 방향, 3: 아랫 방향, 4: 왼쪽 방향
    val entranceZone: Int ? = 0,


-- 방 입구 좌표
INSERT INTO room_entrance
(entrance_left_up_x, entrance_left_up_y, entrance_right_down_x, entrance_right_down_y, entranceRight ,facility_id)
VALUES
    (15.509, -6.32, 16.009, -8.00, 4),
    (27.88, -10.09, 28.38, -10.9, 5),
    (36.77, -10.09, 37.27, -10.9, 6),
    (21.22, -5.05, 22.62, -6.05, 7),
    (27.89, -5.05, 37.39, -6.05, 7),
    (24.77, -7.85, 23.77, -8.35, 8),
    (31.15, -8.35, 33.75, -8.85, 10);
