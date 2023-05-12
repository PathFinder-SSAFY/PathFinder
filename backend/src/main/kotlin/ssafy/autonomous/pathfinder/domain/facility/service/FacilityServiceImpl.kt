package ssafy.autonomous.pathfinder.domain.facility.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import ssafy.autonomous.pathfinder.domain.facility.domain.BlockWall
import ssafy.autonomous.pathfinder.domain.facility.domain.Facility
import ssafy.autonomous.pathfinder.domain.facility.domain.RoomEntrance
import ssafy.autonomous.pathfinder.domain.facility.dto.request.FacilityCurrentLocationRequestDto
import ssafy.autonomous.pathfinder.domain.facility.dto.request.FacilityTypesRequestDto
import ssafy.autonomous.pathfinder.domain.facility.dto.response.WallBlindSpotsResponseDto
import ssafy.autonomous.pathfinder.domain.facility.exception.FacilityNotFoundException
import ssafy.autonomous.pathfinder.domain.facility.repository.BlockWallRepository
import ssafy.autonomous.pathfinder.domain.facility.repository.FacilityRepository
import ssafy.autonomous.pathfinder.domain.facility.repository.FacilityQuerydslRepository
import ssafy.autonomous.pathfinder.domain.facility.repository.RoomEntranceRepository
import java.math.BigDecimal
import java.util.*
import javax.transaction.Transactional
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow

@Service
class FacilityServiceImpl(
    private val facilityRepository: FacilityRepository,
    private val roomEntranceRepository: RoomEntranceRepository,
    private val facilityQuerydslRepository: FacilityQuerydslRepository,
    private val blockWallRepository: BlockWallRepository
) : FacilityService {

    private val logger = KotlinLogging.logger {}

    // 필터링 입력할 때마다 리스트로 출력
    override fun facilityDynamic(facilityTypesRequest: FacilityTypesRequestDto): List<String> {
        val inputFacilityType = facilityTypesRequest.filteringSearch

        return getFacilityTypesDynamic(inputFacilityType).map{
                facility -> facility.getFacilityName()
        }
    }

    // 필터링에 입력 후, 검색 버튼 클릭
    @Transactional
    override fun getFacilityTypes(facilitySearchRequest: FacilityTypesRequestDto): Facility {
        val inputFacilityType = facilitySearchRequest.filteringSearch
//        logger.info("msg : 시설 종류 :  $inputFacilityType")
        val curFacility: Facility =
            facilityRepository.findByFacilityName(inputFacilityType).orElseThrow { FacilityNotFoundException() }

        // (1) 검색 되었을 때 횟수 1증가
        curFacility.plusHitCount()

        // (2) 검색 되었을 때 횟수 1증가 (EntityManager - merge)
        facilityQuerydslRepository.updateFacility(curFacility)

        return curFacility
    }

    // 3-3. 현재 위치 입력 후, 해당 범위 내인지 확인한다.
    override fun getCurrentLocation(facilityCurrentLocationRequestDto: FacilityCurrentLocationRequestDto): String {
        val roomEntrance: MutableList<RoomEntrance> = getAllRoomEntrance()

        for (inRoomEntrance in roomEntrance) {
            val curFacilityName = inRoomEntrance.facility.getFacilityName()

            when {
                findWithinRange(facilityCurrentLocationRequestDto, inRoomEntrance) -> return "$curFacilityName 입구입니다."
                isNearFacilityEntrance(facilityCurrentLocationRequestDto, inRoomEntrance) -> return "$curFacilityName 입구 앞 입니다."
                isInsideFacility(facilityCurrentLocationRequestDto, inRoomEntrance) -> return "$curFacilityName 안 입니다."
            }
        }
        return "4층 복도입니다."

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

    fun getWallBlindSpotsList(blockWallList: List<BlockWall>): List<WallBlindSpotsResponseDto> {
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

    // 입력한 문자열을 기반으로 방 이름 리스트를 가져온다.
    fun getFacilityTypesDynamic(inputFacilityType: String): List<Facility> {
        return facilityRepository.findByFacilityNameContainingOrderByHitCountDesc(inputFacilityType)
    }

    fun getAllRoomEntrance(): MutableList<RoomEntrance> {
        return roomEntranceRepository.findAll()
    }

    fun getAllBlockWall(): MutableList<BlockWall>{
        return blockWallRepository.findAll()
    }

    // 시설 입구에 있는지 확인
    fun findWithinRange(facilityCurrentLocationRequestDto: FacilityCurrentLocationRequestDto, roomEntrance: RoomEntrance): Boolean {
        val (leftUpX, leftUpZ) = roomEntrance.getEntranceLeftUpXZ()
        val (rightDownX, rightDownZ) = roomEntrance.getEntranceRightDownXZ()

//        logger.info("시설 입구 확인")
//        logger.info("시설 입구 범위 LX, LY : $leftUpX , $leftUpZ")
//        logger.info("시설 입구 범위 RX, RY : $rightDownX, $rightDownZ")

        if (isWithinRangeX(facilityCurrentLocationRequestDto.x, leftUpX, rightDownX)
            && isWithinRangeZ(facilityCurrentLocationRequestDto.z, rightDownZ, leftUpZ)
        ) return true
        return false
    }

    // 시설 입구 앞인지 확인
    fun isNearFacilityEntrance(
        facilityCurrentLocationRequestDto: FacilityCurrentLocationRequestDto,
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
        if (entranceDirection == 1 && isWithinRangeX(facilityCurrentLocationRequestDto.x, leftUpX, rightDownX)
            && isWithinRangeZ(facilityCurrentLocationRequestDto.z, leftUpZ, leftUpZ!! + entranceZone!!)
        ) return true
        else if (entranceDirection == 2 && isWithinRangeZ(facilityCurrentLocationRequestDto.z, rightDownZ, leftUpZ)
            && isWithinRangeX(facilityCurrentLocationRequestDto.x, rightDownX, rightDownX!! + entranceZone!!)
        ) return true
        else if (entranceDirection == 3 && isWithinRangeX(facilityCurrentLocationRequestDto.x, leftUpX, rightDownX)
            && isWithinRangeZ(facilityCurrentLocationRequestDto.z, rightDownZ!! - entranceZone!!, rightDownZ)
        ) return true
        else if (entranceDirection == 4 && isWithinRangeZ(facilityCurrentLocationRequestDto.z, rightDownZ, leftUpZ)
            && isWithinRangeX(facilityCurrentLocationRequestDto.x, leftUpX!! - entranceZone!!, leftUpX )
        ) return true
        return false

    }

    // 시설 안인지 확인한다.
    fun isInsideFacility(facilityCurrentLocationRequestDto: FacilityCurrentLocationRequestDto, roomEntrance: RoomEntrance): Boolean {
        val (facilityUpX, facilityUpZ) = roomEntrance.facility.getFacilityLeftUpXZ()
        val (facilityDownX, facilityDownZ) = roomEntrance.facility.getFacilityRightDownXZ()

//        logger.info("시설인지 확인한다.")

        // 시설 내부인지 확인한다.
        if (isWithinRangeX(facilityCurrentLocationRequestDto.x, facilityUpX, facilityDownX)
            && isWithinRangeZ(facilityCurrentLocationRequestDto.z, facilityDownZ, facilityUpZ)
        ) return true
        return false
    }

    fun isWithinRangeX(x: Double, leftUpX: Double?, rightDownX: Double?): Boolean {
//        logger.info("x : $x , leftUpX : $leftUpX , rightUpX : $rightUpX")
        return x in leftUpX!!..rightDownX!!
    }

    fun isWithinRangeZ(z: Double, rightDownZ: Double?, leftUpZ: Double?): Boolean {
//        logger.info("z : $z , leftUpZ : $leftUpZ , rightUpZ : $rightUpZ")
        return z in rightDownZ!!..leftUpZ!!
    }

    fun getObstaclePositionsByInterval(blockWallList: List<BlockWall>): List<Pair<Double, Double>>{
        val coordinates: MutableList<Pair<Double,Double>> = mutableListOf<Pair<Double, Double>>()

        // 1m 간격으로 저장해서 전달한다.
        for(inBlockWall in blockWallList){
            coordinates += generateObstacleSegment(inBlockWall)
        }

        return coordinates
    }

    fun generateObstacleSegment(inBlockWall: BlockWall): MutableList<Pair<Double, Double>> {
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

        logger.info("========================================")
        logger.info("시작 위치 : $leftUpX 도착 위치 : $rightDownX")
        logger.info("시작 위치 : $leftUpZ 도착 위치 : $rightDownZ")


        for(x in leftUpXToInt..rightDownXToInt step stepInt){
            for(z in rightDownZToInt .. leftUpZToInt step stepInt){
                val currentX = BigDecimal.valueOf(x.toDouble() / 10.0.pow(scale)).toDouble()
                val currentZ = BigDecimal.valueOf(z.toDouble() / 10.0.pow(scale)).toDouble()
                logger.info("x : $currentX z : $currentZ")
                obstacleList.add(Pair(currentX, currentZ))
            }
        }

        return obstacleList

    }

}

