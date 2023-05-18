package ssafy.autonomous.pathfinder.domain.facility.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import ssafy.autonomous.pathfinder.domain.facility.domain.Facility
import ssafy.autonomous.pathfinder.domain.facility.domain.FacilityCoordinate
import ssafy.autonomous.pathfinder.domain.facility.dto.request.FacilityNameRequestDto
import ssafy.autonomous.pathfinder.domain.facility.dto.request.FacilityTypesRequestDto
import ssafy.autonomous.pathfinder.domain.facility.dto.response.FacilityCoordinatesResponseDto
import ssafy.autonomous.pathfinder.domain.facility.dto.response.FacilityIsValidResponseDto
import ssafy.autonomous.pathfinder.domain.facility.dto.response.FacilityMidPointResponseDto
import ssafy.autonomous.pathfinder.domain.facility.exception.FacilityNotFoundException
import ssafy.autonomous.pathfinder.domain.facility.repository.FacilityRepository
import ssafy.autonomous.pathfinder.domain.facility.repository.FacilityQuerydslRepository
import ssafy.autonomous.pathfinder.domain.facility.repository.RoomEntranceRepository
import ssafy.autonomous.pathfinder.domain.floors.domain.RoomEntrance
import ssafy.autonomous.pathfinder.domain.floors.dto.request.FloorsCurrentLocationRequestDto
import java.util.*
import javax.transaction.Transactional
import kotlin.math.pow

@Service
class FacilityServiceImpl(
    private val facilityRepository: FacilityRepository,
    private val facilityQuerydslRepository: FacilityQuerydslRepository,
    private val roomEntranceRepository: RoomEntranceRepository
) : FacilityService {

    private val logger = KotlinLogging.logger {}

    override fun facilityDynamic(facilityTypesRequest: FacilityTypesRequestDto): List<String> {
        val inputFacilityType = facilityTypesRequest.filteringSearch

        return getFacilityTypesDynamic(inputFacilityType).map{
                facility -> facility.getFacilityName()
        }
    }

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

    override fun isValidFacilityValue(facilityTypesRequest: FacilityTypesRequestDto): FacilityIsValidResponseDto {
        val filteringSearch = facilityTypesRequest.filteringSearch
        val searchResultFacility = processValidFacilityName(filteringSearch)

        // 비어있다면
        if(searchResultFacility.isEmpty) return FacilityIsValidResponseDto()

        // 찾았다면
        return FacilityIsValidResponseDto(
            isValid = true,
            fetchFacilityCoordinates(searchResultFacility.get())
        )
    }

    override fun getEntrancePointFacility(facilityNameRequestDto: FacilityNameRequestDto): FacilityMidPointResponseDto {
        val roomEntrance: RoomEntrance? =
            processValidFacilityName(facilityNameRequestDto.facilityName).get().getEntrance()

        // 사용자가 요청한 입구 좌표 한 점을 android에게 던져준다.
        return FacilityMidPointResponseDto(
            aStarX = roomEntrance?.getAStarXYZ()?.get(0),
            aStarY = roomEntrance?.getAStarXYZ()?.get(1),
            aStarZ = roomEntrance?.getAStarXYZ()?.get(2)
        )
    }

    override fun getFacilityByFacilityName(facilityNameRequestDto: FacilityNameRequestDto): Facility {
        return processValidFacilityName(facilityNameRequestDto.facilityName).get()
    }

    override fun findWithinRange(floorsCurrentLocationRequestDto: FloorsCurrentLocationRequestDto, roomEntrance: RoomEntrance): Boolean {
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

    override fun isNearFacilityEntrance(
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

    override fun isInsideFacility(floorsCurrentLocationRequestDto: FloorsCurrentLocationRequestDto, facility: Facility): Boolean {
        val (facilityUpX, facilityUpZ) = facility.getFacilityLeftUpXZ()
        val (facilityDownX, facilityDownZ) = facility.getFacilityRightDownXZ()

//        logger.info("시설 입구 확인")
//        logger.info("시설 입구 범위 LX, LY : $facilityUpX , $facilityUpZ")
//        logger.info("시설 입구 범위 RX, RY : $facilityDownX, $facilityDownZ")

        // 시설 내부인지 확인한다.
        if (isWithinRangeX(floorsCurrentLocationRequestDto.x, facilityUpX, facilityDownX)
            && isWithinRangeZ(floorsCurrentLocationRequestDto.z, facilityDownZ, facilityUpZ)
        ) return true
        return false
    }

    override fun isInsideFacilityCheck(
        floorsCurrentLocationRequestDto: FloorsCurrentLocationRequestDto,
        facilityName: String
    ): Boolean {
        val currentFacility : Facility = processValidFacilityName(facilityName).get()
        return isInsideFacility(floorsCurrentLocationRequestDto, currentFacility)
    }

    // 입력한 문자열을 기반으로 방 이름 리스트를 가져온다.
    fun getFacilityTypesDynamic(inputFacilityType: String): List<Facility> {
        return facilityRepository.findByFacilityNameContainingOrderByHitCountDesc(inputFacilityType)
    }

    // 시설 입력했을 때, 올바른 값인지 판단하는 함수 (예외가 아닌 null을 전달)
    fun processValidFacilityName(inputFacilityType: String): Optional<Facility> {
        return facilityRepository.findByFacilityName(inputFacilityType)
    }


    // 입구 좌표를 기반, 중점 좌표 얻기
    fun getMidpoint(roomEntrance: RoomEntrance?) : Pair<Double,Double> {
        val (facilityEntranceUpX, facilityEntranceUpZ) = roomEntrance!!.getEntranceLeftUpXZ()
        val (facilityEntranceDownX, facilityEntranceDownZ) = roomEntrance.getEntranceRightDownXZ()

        val scale = 1
        val facilityEntranceUpXToInt = (facilityEntranceUpX!! * 10.0.pow(scale)).toInt()
        val facilityEntranceDownXToInt = (facilityEntranceDownX!! * 10.0.pow(scale)).toInt()
        val facilityEntranceUpZToInt = (facilityEntranceUpZ!! * 10.0.pow(scale)).toInt()
        val facilityEntranceDownZToInt = (facilityEntranceDownZ!! * 10.0.pow(scale)).toInt()

        val midX = ((((facilityEntranceUpXToInt +facilityEntranceDownXToInt) / 2).toDouble()) / 10.0.pow(scale)).toDouble()
        val midZ = ((((facilityEntranceUpZToInt + facilityEntranceDownZToInt) / 2).toDouble()) / 10.0.pow(scale)).toDouble()

        logger.info("X : $midX Y : $midZ")

        return Pair(midX, midZ)
    }

    fun fetchFacilityCoordinates(facility: Facility) : FacilityCoordinatesResponseDto{
        // x, y, z 좌표 조회
        val facilityType = FacilityCoordinate.valueOf(facility.getFacilityType())
        val xCoordinate = facilityType.x
        val yCoordinate = facilityType.y
        val zCoordinate = facilityType.z

        return FacilityCoordinatesResponseDto(
            x = xCoordinate,
            y = yCoordinate,
            z = zCoordinate
        )
    }

    private fun isWithinRangeX(x: Double, leftUpX: Double?, rightDownX: Double?): Boolean {
//        logger.info("x : $x , leftUpX : $leftUpX , rightUpX : $rightUpX")
        return x in leftUpX!!..rightDownX!!
    }

    private fun isWithinRangeZ(z: Double, rightDownZ: Double?, leftUpZ: Double?): Boolean {
//        logger.info("z : $z , leftUpZ : $leftUpZ , rightUpZ : $rightUpZ")
        return z in rightDownZ!!..leftUpZ!!
    }
}