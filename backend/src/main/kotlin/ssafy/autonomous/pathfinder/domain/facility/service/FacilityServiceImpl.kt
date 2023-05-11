package ssafy.autonomous.pathfinder.domain.facility.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import ssafy.autonomous.pathfinder.domain.facility.domain.Facility
import ssafy.autonomous.pathfinder.domain.facility.domain.RoomEntrance
import ssafy.autonomous.pathfinder.domain.facility.dto.request.FacilityCurrentLocationRequestDto
import ssafy.autonomous.pathfinder.domain.facility.dto.request.FacilityTypesRequestDto
import ssafy.autonomous.pathfinder.domain.facility.exception.FacilityNotFoundException
import ssafy.autonomous.pathfinder.domain.facility.repository.FacilityRepository
import ssafy.autonomous.pathfinder.domain.facility.repository.FacilityQuerydslRepository
import ssafy.autonomous.pathfinder.domain.facility.repository.RoomEntranceRepository
import java.util.*
import javax.transaction.Transactional

@Service
class FacilityServiceImpl(
    private val facilityRepository: FacilityRepository,
    private val roomEntranceRepository: RoomEntranceRepository,
    private val facilityQuerydslRepository: FacilityQuerydslRepository
) : FacilityService {

    private val logger = KotlinLogging.logger {}

    // 필터링 입력할 때마다 리스트로 출력
    override fun facilityDynamic(facilityTypesRequest: FacilityTypesRequestDto): List<String> {
        val inputFacilityType = facilityTypesRequest.filteringSearch

        // Repository -> Containing 사용
//        val resultFacilityTypes: List<Facility> = facilityRepository.findAllByFacilityTypeLikeOrderByHitCountDesc("%$inputFacilityType%")
        val facilityList: List<Facility> = getFacilityTypesDynamic(inputFacilityType)
        return facilityList.map{ facility -> facility.getFacilityName()}
    }

    // 입력한 문자열을 기반으로 방 이름 리스트를 가져온다.
    fun getFacilityTypesDynamic(inputFacilityType: String): List<Facility> {
        return facilityJpaRepository.findByFacilityNameContainingOrderByHitCountDesc(inputFacilityType)
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

    // 입력한 문자열을 기반으로 방 이름 리스트를 가져온다.
    fun getFacilityTypesDynamic(inputFacilityType: String): List<Facility> {
        return facilityRepository.findAllByFacilityNameContainingOrderByHitCountDesc(inputFacilityType)
    }

    fun getAllRoomEntrance(): MutableList<RoomEntrance> {
        return roomEntranceRepository.findAll()
    }

    // 시설 입구에 있는지 확인
    fun findWithinRange(facilityCurrentLocationRequestDto: FacilityCurrentLocationRequestDto, roomEntrance: RoomEntrance): Boolean {
        val (leftUpX, leftUpY) = roomEntrance.getEntranceLeftUpXY()
        val (rightDownX, rightDownY) = roomEntrance.getEntranceRightDownXY()

        logger.info("시설 입구 확인")
        logger.info("시설 입구 범위 LX, LY : $leftUpX , $leftUpY")
        logger.info("시설 입구 범위 RX, RY : $rightDownX, $rightDownY")

        if (isWithinRangeX(facilityCurrentLocationRequestDto.curX, leftUpX, rightDownX)
            && isWithinRangeY(facilityCurrentLocationRequestDto.curY, rightDownY, leftUpY)
        ) return true
        return false
    }

    // 시설 입구 앞인지 확인
    fun isNearFacilityEntrance(
        facilityCurrentLocationRequestDto: FacilityCurrentLocationRequestDto,
        roomEntrance: RoomEntrance
    ): Boolean {
        val (leftUpX, leftUpY) = roomEntrance.getEntranceLeftUpXY()
        val (rightDownX, rightDownY) = roomEntrance.getEntranceRightDownXY()
        val entranceDirection: Int? = roomEntrance.getEntranceDirection()
        val entranceZone: Double? = roomEntrance.getEntranceZone()


        logger.info("시설 입구 앞 확인")
        logger.info("시설 입구 범위 LX, LY : $leftUpX , $leftUpY")
        logger.info("시설 입구 범위 RX, RY : $rightDownX, $rightDownY")
        
        /*
        * 1 : Y + 20 (상 방향)
        * 2 : X + 20 (오른쪽 방향)
        * 3 : Y - 20 (하 방향)
        * 4 : X - 20 (왼쪽 방향)
        * */
        if (entranceDirection == 1 && isWithinRangeX(facilityCurrentLocationRequestDto.curX, leftUpX, rightDownX)
            && isWithinRangeY(facilityCurrentLocationRequestDto.curY, leftUpY, leftUpY!! + entranceZone!!)
        ) return true
        else if (entranceDirection == 2 && isWithinRangeY(facilityCurrentLocationRequestDto.curY, leftUpY, rightDownY)
            && isWithinRangeX(facilityCurrentLocationRequestDto.curX, rightDownX, rightDownX!! + entranceZone!!)
        ) return true
        else if (entranceDirection == 3 && isWithinRangeX(facilityCurrentLocationRequestDto.curX, leftUpX, rightDownX)
            && isWithinRangeY(facilityCurrentLocationRequestDto.curY, rightDownY, rightDownY!! - entranceZone!!)
        ) return true
        else if (entranceDirection == 2 && isWithinRangeY(facilityCurrentLocationRequestDto.curY, leftUpY, rightDownY)
            && isWithinRangeX(facilityCurrentLocationRequestDto.curX, leftUpX, leftUpX!! - entranceZone!!)
        ) return true
        return false

    }

    // 시설 안인지 확인한다.
    fun isInsideFacility(facilityCurrentLocationRequestDto: FacilityCurrentLocationRequestDto, roomEntrance: RoomEntrance): Boolean {
        val (facilityUpX, facilityUpY) = roomEntrance.facility.getFacilityLeftUpXY()
        val (facilityDownX, facilityDownY) = roomEntrance.facility.getFacilityRightDownXY()

        logger.info("시설인지 확인한다.")

        // 시설 내부인지 확인한다.
        if (isWithinRangeX(facilityCurrentLocationRequestDto.curX, facilityUpX, facilityDownX)
            && isWithinRangeY(facilityCurrentLocationRequestDto.curY, facilityDownY, facilityUpY)
        ) return true
        return false
    }

    fun isWithinRangeX(curX: Double, leftUpX: Double?, rightUpX: Double?): Boolean {
        logger.info("curX : $curX , leftUpX : $leftUpX , rightUpX : $rightUpX")
        return curX in leftUpX!!..rightUpX!!
    }

    fun isWithinRangeY(curY: Double, leftUpY: Double?, rightUpY: Double?): Boolean {
        logger.info("curY : $curY , leftUpY : $leftUpY , rightUpY : $rightUpY")
        return curY in leftUpY!!..rightUpY!!
    }

}

