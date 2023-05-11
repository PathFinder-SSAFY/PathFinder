package ssafy.autonomous.pathfinder.domain.facility.domain

import javax.persistence.*


/*
* 시설 입구 엔티티
* */
@Entity
class RoomEntrance(
    // 입구 좌상X, 좌상Y, 우하X, 우하Y
    // 입구 사각형 좌표 (UpX, UpY), (DownX, UpY), (UpX, DownY), (DownX, DownY)
    private val entranceLeftUpX: Double ?= 0.0,
    private val entranceLeftUpY: Double ?= 0.0,
    private val entranceRightDownX: Double ?= 0.0,
    private val entranceRightDownY: Double ?= 0.0,
    private val entranceDirection: Int ? = 0, // 1: 윗 방향, 2: 오른쪽 방향, 3: 아랫 방향, 4: 왼쪽 방향
    private val entranceZone: Double ? = 0.0, // 밀접 거리

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_coordinates_id")
    val id: Int,


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    var facility: Facility

){
    fun getEntranceLeftUpXY(): List<Double?> {
        return listOf(entranceLeftUpX, entranceLeftUpY)
    }

    fun getEntranceRightDownXY(): List<Double?> {
        return listOf(entranceRightDownX, entranceRightDownY)
    }

    fun getEntranceDirection(): Int? {
        return this.entranceDirection
    }

    fun getEntranceZone(): Double? {
        return this.entranceZone
    }
}