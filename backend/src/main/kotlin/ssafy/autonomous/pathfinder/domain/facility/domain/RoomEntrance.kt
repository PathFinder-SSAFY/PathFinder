package ssafy.autonomous.pathfinder.domain.facility.domain

import javax.persistence.*


/*
* 시설 입구 엔티티
* */
@Entity
class RoomEntrance(
    // 입구 좌상, 우상, 좌하, 우하 추가
    val entranceLeftUp: Double ?= 0.0,
    val entranceRightUp: Double ?= 0.0,
    val entranceLeftDown: Double ?= 0.0,
    val entranceRightDown: Double ?= 0.0,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_coordinates_id")
    val id: Int,


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    var facility: Facility

    )