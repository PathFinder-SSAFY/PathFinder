package ssafy.autonomous.pathfinder.domain.facility.domain

import ssafy.autonomous.pathfinder.domain.facility.domain.Floors
import javax.persistence.*


@Entity
class BlockWall(
    // 입구 좌상X, 좌상Y, 우하X, 우하Y
    // 입구 사각형 좌표 (UpX, UpZ), (DownX, UpZ), (UpX, DownZ), (DownX, DownZ)
    private val blockWallLeftUpX: Double ?= 0.0,
    private val blockWallLeftUpZ: Double ?= 0.0,
    private val blockWallRightDownX: Double ?= 0.0,
    private val blockWallRightDownZ: Double ?= 0.0,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_wall_id")
    val id: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floors_id")
    private val floors: Floors? = null,
){
    fun getBlockWallLeftUpXZ(): List<Double?>{
        return listOf(blockWallLeftUpX, blockWallLeftUpZ)
    }

    fun getBlockWallRightDownXZ(): List<Double?> {
        return listOf(blockWallRightDownX, blockWallRightDownZ)
    }
}