package ssafy.autonomous.pathfinder.domain.floors.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import ssafy.autonomous.pathfinder.domain.facility.domain.BlockWall
import ssafy.autonomous.pathfinder.domain.facility.domain.Facility
import ssafy.autonomous.pathfinder.domain.facility.dto.response.WallBlindSpotsResponseDto
import ssafy.autonomous.pathfinder.domain.facility.repository.BlockWallRepository
import ssafy.autonomous.pathfinder.domain.facility.repository.RoomEntranceRepository
import ssafy.autonomous.pathfinder.domain.floors.domain.Beacon
import ssafy.autonomous.pathfinder.domain.floors.domain.RoomEntrance
import ssafy.autonomous.pathfinder.domain.floors.dto.request.FloorsCurrentLocationRequestDto
import ssafy.autonomous.pathfinder.domain.floors.exception.HandleInWallBoundaryException
import ssafy.autonomous.pathfinder.domain.floors.exception.HandleInvalidUserLocationOnCurrentFloorException
import ssafy.autonomous.pathfinder.domain.floors.repository.BeaconRepository
import java.math.BigDecimal
import kotlin.math.pow

@Service
class FloorsServiceImpl(
    private val beaconRepository: BeaconRepository,
    private val roomEntranceRepository: RoomEntranceRepository,
    private val blockWallRepository: BlockWallRepository
) : FloorsService {

    private val logger = KotlinLogging.logger{}


    override fun getBeaconList(): List<Beacon> {
        return beaconRepository.findAll()
    }

    // 3-3. 현재 위치 입력 후, 해당 범위 내인지 확인한다.
    override fun getCurrentLocation(floorsCurrentLocationRequestDto: FloorsCurrentLocationRequestDto): String {
        val roomEntrance: MutableList<RoomEntrance> = getAllRoomEntrance()

        for (inRoomEntrance in roomEntrance) {
            val curFacilityName = inRoomEntrance.facility.getFacilityName()

            when {
                findWithinRange(floorsCurrentLocationRequestDto, inRoomEntrance) -> return "$curFacilityName 입구"
                isNearFacilityEntrance(floorsCurrentLocationRequestDto, inRoomEntrance) -> return "$curFacilityName 입구 앞"
                isInsideFacility(floorsCurrentLocationRequestDto, inRoomEntrance.facility) -> return "$curFacilityName 안"
                isBlockWall(floorsCurrentLocationRequestDto) -> throw HandleInWallBoundaryException()
            }
        }

        // 시설 내에 있지 않고, 범위를 벗어났을 경우 (4층 모든 좌표 확인 후, 4층 복도인지 외부인지 판단하기 위한 함수)
        if(isOutOfRange(floorsCurrentLocationRequestDto)) throw HandleInvalidUserLocationOnCurrentFloorException()
        return "4층 복도"

    }

    // 3-4 벽 0.1m 간력 점들을 반환하는 함수
    /*
    * x, y축 List로 전달
    * 좌표는 반올림
    * */
    override fun getWallPositions(): List<Pair<Double, Double>> {
        val blockWallList : List<BlockWall> = getAllBlockWall()

        // 장애물 구간 만들기
        return getObstaclePositionsByInterval(blockWallList)
    }


    // 3-4-1 벽(장애물) 사각지대 위치 전달
    override fun getWallBlindSpots(): List<WallBlindSpotsResponseDto> {
        val blockWallList : List<BlockWall> = getAllBlockWall()

        return getWallBlindSpotsList(blockWallList)
    }

    private fun getAllRoomEntrance(): MutableList<RoomEntrance> {
        return roomEntranceRepository.findAll()
    }

    private fun getAllBlockWall(): MutableList<BlockWall>{
        return blockWallRepository.findAll()
    }


    private fun getWallBlindSpotsList(blockWallList: List<BlockWall>): List<WallBlindSpotsResponseDto> {
        val wallBlindSpots: MutableList<WallBlindSpotsResponseDto> = mutableListOf<WallBlindSpotsResponseDto>()

        for (inBlockWall in blockWallList) {
            val (leftUpX, leftUpZ) = inBlockWall.getBlockWallLeftUpXZ()
            val (rightDownX, rightDownZ) = inBlockWall.getBlockWallRightDownXZ()
            wallBlindSpots.add(
                WallBlindSpotsResponseDto(
                    leftUpX,
                    leftUpZ,
                    rightDownX,
                    rightDownZ
                )
            )
        }
        return wallBlindSpots
    }

    // 시설 입구에 있는지 확인
    private fun findWithinRange(floorsCurrentLocationRequestDto: FloorsCurrentLocationRequestDto, roomEntrance: RoomEntrance): Boolean {
        val (leftUpX, leftUpZ) = roomEntrance.getEntranceLeftUpXZ()
        val (rightDownX, rightDownZ) = roomEntrance.getEntranceRightDownXZ()

//        logger.info("시설 입구 확인")
//        logger.info("시설 입구 범위 LX, LY : $leftUpX , $leftUpZ")
//        logger.info("시설 입구 범위 RX, RY : $rightDownX, $rightDownZ")

        if (isWithinRangeX(floorsCurrentLocationRequestDto.x, leftUpX, rightDownX)
            && isWithinRangeZ(floorsCurrentLocationRequestDto.z, rightDownZ, leftUpZ)
        ) return true
        return false
    }

    // 시설 입구 앞인지 확인
    private fun isNearFacilityEntrance(
        floorsCurrentLocationRequestDto: FloorsCurrentLocationRequestDto,
        roomEntrance: RoomEntrance
    ): Boolean {
        val (leftUpX, leftUpZ) = roomEntrance.getEntranceLeftUpXZ()
        val (rightDownX, rightDownZ) = roomEntrance.getEntranceRightDownXZ()
        val entranceDirection: Int? = roomEntrance.getEntranceDirection()
        val entranceZone: Double? = roomEntrance.getEntranceZone()


//        logger.info("시설 입구 앞 확인")
//        logger.info("시설 입구 범위 LX, LY : $leftUpX , $leftUpZ")
//        logger.info("시설 입구 범위 RX, RY : $rightDownX, $rightDownZ")

        /*
        * 1 : Y + 20 (상 방향)
        * 2 : X + 20 (오른쪽 방향)
        * 3 : Y - 20 (하 방향)
        * 4 : X - 20 (왼쪽 방향)
        * */
        if (entranceDirection == 1 && isWithinRangeX(floorsCurrentLocationRequestDto.x, leftUpX, rightDownX)
            && isWithinRangeZ(floorsCurrentLocationRequestDto.z, leftUpZ, leftUpZ!! + entranceZone!!)
        ) return true
        else if (entranceDirection == 2 && isWithinRangeZ(floorsCurrentLocationRequestDto.z, rightDownZ, leftUpZ)
            && isWithinRangeX(floorsCurrentLocationRequestDto.x, rightDownX, rightDownX!! + entranceZone!!)
        ) return true
        else if (entranceDirection == 3 && isWithinRangeX(floorsCurrentLocationRequestDto.x, leftUpX, rightDownX)
            && isWithinRangeZ(floorsCurrentLocationRequestDto.z, rightDownZ!! - entranceZone!!, rightDownZ)
        ) return true
        else if (entranceDirection == 4 && isWithinRangeZ(floorsCurrentLocationRequestDto.z, rightDownZ, leftUpZ)
            && isWithinRangeX(floorsCurrentLocationRequestDto.x, leftUpX!! - entranceZone!!, leftUpX )
        ) return true
        return false

    }

    // 시설 안인지 확인한다.
    private fun isInsideFacility(floorsCurrentLocationRequestDto: FloorsCurrentLocationRequestDto, facility: Facility): Boolean {
        val (facilityUpX, facilityUpZ) = facility.getFacilityLeftUpXZ()
        val (facilityDownX, facilityDownZ) = facility.getFacilityRightDownXZ()

        logger.info("시설 입구 확인")
        logger.info("시설 입구 범위 LX, LY : $facilityUpX , $facilityUpZ")
        logger.info("시설 입구 범위 RX, RY : $facilityDownX, $facilityDownZ")

        // 시설 내부인지 확인한다.
        if (isWithinRangeX(floorsCurrentLocationRequestDto.x, facilityUpX, facilityDownX)
            && isWithinRangeZ(floorsCurrentLocationRequestDto.z, facilityDownZ, facilityUpZ)
        ) return true
        return false
    }

    private fun isWithinRangeX(x: Double, leftUpX: Double?, rightDownX: Double?): Boolean {
//        logger.info("x : $x , leftUpX : $leftUpX , rightUpX : $rightUpX")
        return x in leftUpX!!..rightDownX!!
    }

    private fun isWithinRangeZ(z: Double, rightDownZ: Double?, leftUpZ: Double?): Boolean {
//        logger.info("z : $z , leftUpZ : $leftUpZ , rightUpZ : $rightUpZ")
        return z in rightDownZ!!..leftUpZ!!
    }

    private fun getObstaclePositionsByInterval(blockWallList: List<BlockWall>): List<Pair<Double, Double>>{
        val coordinates: MutableList<Pair<Double,Double>> = mutableListOf<Pair<Double, Double>>()

        // 1m 간격으로 저장해서 전달한다.
        for(inBlockWall in blockWallList){
            coordinates += generateObstacleSegment(inBlockWall)
        }

        return coordinates
    }


    private fun generateObstacleSegment(inBlockWall: BlockWall): MutableList<Pair<Double, Double>> {
        val (leftUpX, leftUpZ) = inBlockWall.getBlockWallLeftUpXZ()
        val (rightDownX, rightDownZ) = inBlockWall.getBlockWallRightDownXZ()

        // 소수점 구간 반복문 돌리기 위해 사용한 변수
        val scale = 1
        val leftUpXToInt = (leftUpX!! * 10.0.pow(scale)).toInt()
        val rightDownXToInt = (rightDownX!! * 10.0.pow(scale)).toInt()
        val leftUpZToInt = (leftUpZ!! * 10.0.pow(scale)).toInt()
        val rightDownZToInt = (rightDownZ!! * 10.0.pow(scale)).toInt()
        val stepInt = (0.1 * 10.0.pow(scale)).toInt()

        val obstacleList : MutableList<Pair<Double, Double>> = mutableListOf()

//        logger.info("========================================")
//        logger.info("시작 위치 : $leftUpX 도착 위치 : $rightDownX")
//        logger.info("시작 위치 : $leftUpZ 도착 위치 : $rightDownZ")


        for(x in leftUpXToInt..rightDownXToInt step stepInt){
            for(z in rightDownZToInt .. leftUpZToInt step stepInt){
                val currentX = BigDecimal.valueOf(x.toDouble() / 10.0.pow(scale)).toDouble()
                val currentZ = BigDecimal.valueOf(z.toDouble() / 10.0.pow(scale)).toDouble()
//                logger.info("x : $currentX z : $currentZ")
                obstacleList.add(Pair(currentX, currentZ))
            }
        }

        return obstacleList

    }

    // 벽 위치일 경우
    private fun isBlockWall(floorsCurrentLocationRequestDto: FloorsCurrentLocationRequestDto): Boolean{
        val blockWall : List<BlockWall> = getAllBlockWall()
        val curX = floorsCurrentLocationRequestDto.x
        val curZ = floorsCurrentLocationRequestDto.z



        // wall
        val isInsideBlockWall = blockWall.any { wall ->
            val leftUpX = wall.getBlockWallLeftUpXZ()[0]
            val leftUpZ = wall.getBlockWallLeftUpXZ()[1]
            val rightDownX = wall.getBlockWallRightDownXZ()[0]
            val rightDownZ = wall.getBlockWallRightDownXZ()[1]
            logger.info("X : $curX , Z : $curZ")
            logger.info("leftUpX : $leftUpX , leftUpZ : $leftUpZ ")
            logger.info("rightDownX : $rightDownX , rightDownZ : $rightDownZ ")
            leftUpX!! <= curX && curX <= rightDownX!! && rightDownZ!! <= curZ &&curZ <= leftUpZ!!
        }

        // block 범위에 포함되는 경우
        if(isInsideBlockWall) return true
        return false
    }

    // 층 외부에 좌표가 잡혔을 경우
    private fun isOutOfRange(floorsCurrentLocationRequestDto: FloorsCurrentLocationRequestDto): Boolean {
        val curX = floorsCurrentLocationRequestDto.x
        val curZ = floorsCurrentLocationRequestDto.z

        val roundedValueCurX = (curX * 10.0.pow(2)).toInt()
        val roundedValueCurZ = (curZ * 10.0.pow(2)).toInt()

//        logger.info("현재 X 좌표 : $roundedValueCurX + 현재 Z 좌표 : $roundedValueCurZ")

        /*
         범위를 벗어난 경우
         0 <= X <= 65.85
         -12.95 <= Z <= 0
         범위 : (0, 0) (65.85, -12.95)
         */
        if(roundedValueCurX !in 0..6585 || roundedValueCurZ !in -1295 .. 0) return true
        return false
    }
}