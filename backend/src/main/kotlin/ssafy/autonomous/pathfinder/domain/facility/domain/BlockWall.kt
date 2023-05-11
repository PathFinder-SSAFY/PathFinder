package ssafy.autonomous.pathfinder.domain.facility.domain

import ssafy.autonomous.pathfinder.domain.facility.domain.Floors
import javax.persistence.*


@Entity
class BlockWall(
    // 입구 좌상X, 좌상Y, 우하X, 우하Y
    // 입구 사각형 좌표 (UpX, UpY), (DownX, UpY), (UpX, DownY), (DownX, DownY)
    private val blockWallLeftUpX: Double ?= 0.0,
    private val blockWallLeftUpY: Double ?= 0.0,
    private val blockWallRightDownX: Double ?= 0.0,
    private val blockWallRightDownY: Double ?= 0.0,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_wall_id")
    val id: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floors_id")
    private val floors: Floors? = null,
){
    fun getBlockWallLeftUpXY(): List<Double?>{
        return listOf(blockWallLeftUpX, blockWallLeftUpY)
    }

    fun getBlockWallRightDownXY(): List<Double?> {
        return listOf(blockWallRightDownX, blockWallRightDownY)
    }
}