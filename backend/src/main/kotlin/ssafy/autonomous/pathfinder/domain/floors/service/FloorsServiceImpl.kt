package ssafy.autonomous.pathfinder.domain.floors.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import ssafy.autonomous.pathfinder.domain.building.domain.Customer
import ssafy.autonomous.pathfinder.domain.building.exception.CustomerNotFoundException
import ssafy.autonomous.pathfinder.domain.building.repository.CustomerQuerydslRepository
import ssafy.autonomous.pathfinder.domain.building.repository.CustomerRepository
import ssafy.autonomous.pathfinder.domain.facility.domain.BlockWall
import ssafy.autonomous.pathfinder.domain.facility.domain.Facility
import ssafy.autonomous.pathfinder.domain.facility.dto.request.FacilityNameRequestDto
import ssafy.autonomous.pathfinder.domain.facility.dto.response.WallBlindSpotsResponseDto
import ssafy.autonomous.pathfinder.domain.facility.exception.FacilityNotInRangeException
import ssafy.autonomous.pathfinder.domain.facility.repository.BlockWallRepository
import ssafy.autonomous.pathfinder.domain.facility.repository.FacilityQuerydslRepository
import ssafy.autonomous.pathfinder.domain.facility.repository.RoomEntranceRepository
import ssafy.autonomous.pathfinder.domain.facility.service.FacilityService
import ssafy.autonomous.pathfinder.domain.floors.domain.Beacon
import ssafy.autonomous.pathfinder.domain.floors.domain.RoomEntrance
import ssafy.autonomous.pathfinder.domain.floors.dto.request.FloorsCurrentLocationRequestDto
import ssafy.autonomous.pathfinder.domain.floors.dto.request.FloorsCurrentLocationUpdateRequestDto
import ssafy.autonomous.pathfinder.domain.floors.exception.HandleInWallBoundaryException
import ssafy.autonomous.pathfinder.domain.floors.exception.HandleInvalidUserLocationOnCurrentFloorException
import ssafy.autonomous.pathfinder.domain.floors.repository.BeaconRepository
import java.math.BigDecimal
import java.util.*
import javax.transaction.Transactional
import kotlin.math.pow

@Service
class FloorsServiceImpl(
    private val beaconRepository: BeaconRepository,
    private val roomEntranceRepository: RoomEntranceRepository,
    private val blockWallRepository: BlockWallRepository,
    private val customerRepository: CustomerRepository,
    private val customerQuerydslRepository: CustomerQuerydslRepository,
    private val facilityQuerydslRepository: FacilityQuerydslRepository,
    private val facilityService: FacilityService

) : FloorsService {

    private val logger = KotlinLogging.logger{}


    override fun getBeaconList(): List<Beacon> {
        return beaconRepository.findAll()
    }

    // 4-1. 현재 위치 입력 후, 해당 범위 내인지 확인한다.
    override fun getCurrentLocation(floorsCurrentLocationRequestDto: FloorsCurrentLocationRequestDto): String {
        val roomEntrance: MutableList<RoomEntrance> = getAllRoomEntrance()

        for (inRoomEntrance in roomEntrance) {
            val curFacilityName = inRoomEntrance.facility.getFacilityName()

            when {
                facilityService.findWithinRange(floorsCurrentLocationRequestDto, inRoomEntrance) -> return "$curFacilityName 입구"
                facilityService.isNearFacilityEntrance(floorsCurrentLocationRequestDto, inRoomEntrance) -> return "$curFacilityName 입구 앞"
                facilityService.isInsideFacility(floorsCurrentLocationRequestDto, inRoomEntrance.facility) -> return curFacilityName
                isBlockWall(floorsCurrentLocationRequestDto) -> throw HandleInWallBoundaryException()
            }
        }

        // 시설 내에 있지 않고, 범위를 벗어났을 경우 (4층 모든 좌표 확인 후, 4층 복도인지 외부인지 판단하기 위한 함수)
        if(isOutOfRange(floorsCurrentLocationRequestDto)) throw HandleInvalidUserLocationOnCurrentFloorException()
        return "4층 복도"

    }

    // 4-2 벽 0.1m 간력 점들을 반환하는 함수
    /*
    * x, y축 List로 전달
    * 좌표는 반올림
    * */
    override fun getWallPositions(): List<Pair<Double, Double>> {
        val blockWallList : List<BlockWall> = getAllBlockWall()

        // 장애물 구간 만들기
        return getObstaclePositionsByInterval(blockWallList)
    }


    // 4-2-1 벽(장애물) 사각지대 위치 전달
    override fun getWallBlindSpots(): List<WallBlindSpotsResponseDto> {
        val blockWallList : List<BlockWall> = getAllBlockWall()

        return getWallBlindSpotsList(blockWallList)
    }

    // 4-3 지도상 고객 위치 업데이트
    @Transactional
    override fun updateCustomerLocation(id : String, floorsCurrentLocationUpdateRequestDto: FloorsCurrentLocationUpdateRequestDto): String {
        val floorsCurrentLocationRequestDto = FloorsCurrentLocationRequestDto(
            floorsCurrentLocationUpdateRequestDto.x,
            floorsCurrentLocationUpdateRequestDto.y,
            floorsCurrentLocationUpdateRequestDto.z
        )
        val floorsCurrentLocationFacilityName = floorsCurrentLocationUpdateRequestDto.facilityName

        // x, y, z와 시설 위치 좌표인지 확인하는 함수
        // 28.50, 0.0, -13.12 와 4층 남자 화장실 안 일 경우 : true
        if(facilityService.isInsideFacilityCheck(floorsCurrentLocationRequestDto, floorsCurrentLocationFacilityName)){
            // id로 customerRepository에서 customer를 조회한다.
            // 조회한 결과, customer가 시설 위치를 기준으로 판단한다.
            return updateFacilitiesBasedOnCustomerLocation(id, floorsCurrentLocationFacilityName)
        }

        // 시설 범위에 포함되지 않을 시 예외처리
        throw FacilityNotInRangeException()
//        return "x, y, z로 조회된 시설와는 다른 시설 이름입니다."

    }

    private fun getCustomerById(id : String): Optional<Customer> {
        return customerRepository.findById(id.toInt())
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
//            logger.info("X : $curX , Z : $curZ")
//            logger.info("leftUpX : $leftUpX , leftUpZ : $leftUpZ ")
//            logger.info("rightDownX : $rightDownX , rightDownZ : $rightDownZ ")
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
    
    // 시설 밀집도 변경 함수
    private fun updateFacilitiesBasedOnCustomerLocation(id: String, floorsCurrentLocationFacilityName : String): String{
        val customer = getCustomerById(id).orElseThrow { CustomerNotFoundException() }
        val beforeLocationFacility = customer.getCurrentLocationFacility()

        // floorsCurrentLocationFacilityName와 customer 위치가 다르다면
        // - customer 위치가 null이 아니라면, 해당 위치 시설을 조회 후 update한다.
        if(beforeLocationFacility != floorsCurrentLocationFacilityName){
            if(beforeLocationFacility != null){
                // 이전 시설이 null이 아니라면 이전 시설 밀집도 -1
                val decreasedDensityFacility = facilityService.getFacilityByFacilityName(FacilityNameRequestDto(beforeLocationFacility))
                decreasedDensityFacility.minusDensityMax()
                facilityQuerydslRepository.updateFacility(decreasedDensityFacility)
            }

            // 고객 위치 업데이트
            customer.updateCurrentLocationFacility(floorsCurrentLocationFacilityName)
            customerQuerydslRepository.updateCustomer(customer)

            // 밀집도 1 증가
            val increasedDensityFacility = facilityService.getFacilityByFacilityName(FacilityNameRequestDto(floorsCurrentLocationFacilityName))
            increasedDensityFacility.plusDensityMax()
            facilityQuerydslRepository.updateFacility(increasedDensityFacility)
            return "시설 업데이트 됐습니다."
        }

        return "이전 시설 위치와 현재 시설 위치가 같습니다."
    }

}