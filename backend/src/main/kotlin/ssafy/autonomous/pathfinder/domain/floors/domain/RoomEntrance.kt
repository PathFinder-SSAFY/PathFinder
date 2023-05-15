package ssafy.autonomous.pathfinder.domain.floors.domain

import ssafy.autonomous.pathfinder.domain.facility.domain.Facility
import javax.persistence.*


/*
* 시설 입구 엔티티
* */
@Entity
class RoomEntrance(
    // 입구 좌상X, 좌상Y, 우하X, 우하Y
    // 입구 사각형 좌표 (UpX, UpZ), (DownX, UpZ), (UpX, DownZ), (DownX, DownZ)
    private val entranceLeftUpX: Double ?= 0.0,
    private val entranceLeftUpZ: Double ?= 0.0,
    private val entranceRightDownX: Double ?= 0.0,
    private val entranceRightDownZ: Double ?= 0.0,
    private val entranceDirection: Int ? = 0, // 1: 윗 방향, 2: 오른쪽 방향, 3: 아랫 방향, 4: 왼쪽 방향
    private val entranceZone: Double ? = 0.0, // 밀접 거리

    // 시설 입구 알고리즘 돌리기 위한, 중심 좌표
    private val aStarCoordinateX: Double ? = 0.0,
    private val aStarCoordinateY: Double ? = 0.0,
    private val aStarCoordinateZ: Double ? = 0.0,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_coordinates_id")
    val id: Int,


    @OneToOne
    @JoinColumn(name = "facility_id")
    var facility: Facility

){
    fun getEntranceLeftUpXZ(): List<Double?> {
        return listOf(entranceLeftUpX, entranceLeftUpZ)
    }

    fun getEntranceRightDownXZ(): List<Double?> {
        return listOf(entranceRightDownX, entranceRightDownZ)
    }

    fun getAStarXYZ() : List<Double?> {
        return listOf(aStarCoordinateX, aStarCoordinateY, aStarCoordinateZ)
    }

    fun getEntranceDirection(): Int? {
        return this.entranceDirection
    }

    fun getEntranceZone(): Double? {
        return this.entranceZone
    }
}