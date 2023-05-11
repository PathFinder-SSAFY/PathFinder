package ssafy.autonomous.pathfinder.domain.facility.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import ssafy.autonomous.pathfinder.domain.facility.domain.Facility
import ssafy.autonomous.pathfinder.domain.facility.domain.RoomEntrance
import ssafy.autonomous.pathfinder.domain.facility.dto.request.FacilityCurrentLocationDto
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
    override fun facilityDynamic(facilityTypesRequest: FacilityTypesRequestDto): List<Facility> {
        val inputFacilityType = facilityTypesRequest.filteringSearch

        // Repository -> Containing 사용
//        val resultFacilityTypes: List<Facility> = facilityRepository.findAllByFacilityTypeLikeOrderByHitCountDesc("%$inputFacilityType%")
        return getFacilityTypesDynamic(inputFacilityType)
    }
    // 필터링에 입력 후, 검색 버튼 클릭
    @Transactional
    override fun getFacilityTypes(facilitySearchRequest: FacilityTypesRequestDto): Facility {
        val inputFacilityType = facilitySearchRequest.filteringSearch
//        logger.info("msg : 시설 종류 :  $inputFacilityType")
        val curFacility: Facility = facilityRepository.findByFacilityName(inputFacilityType).orElseThrow { FacilityNotFoundException() }

        // (1) 검색 되었을 때 횟수 1증가
        curFacility.plusHitCount()

        // (2) 검색 되었을 때 횟수 1증가 (EntityManager - merge)
        facilityQuerydslRepository.updateFacility(curFacility)

        return curFacility
    }

    // 3-3. 현재 위치 입력 후, 해당 범위 내인지 확인한다.
    override fun getCurrentLocation(facilityCurrentLocationDto: FacilityCurrentLocationDto): String {
        val roomEntrance: MutableList<RoomEntrance> = getAllRoomEntrance()

//        roomEntrance.filter{
//            findWithinRange(facilityCurrentLocationDto, it)
//        }

        // 4층 시설을 확인하며, 해당 공간에 위치있다면 String 반환, 없다면 null
        for(inRoomEntrance in roomEntrance){
            val curFacilityName = inRoomEntrance.facility.getFacilityName()
            if(findWithinRange(facilityCurrentLocationDto, inRoomEntrance)) return "$curFacilityName 입구입니다."
            else if(isNearFacilityEntrance(facilityCurrentLocationDto, inRoomEntrance)) return "$curFacilityName 입구 앞 입니다."

        }
        
    }

    // 입력한 문자열을 기반으로 방 이름 리스트를 가져온다.
    fun getFacilityTypesDynamic(inputFacilityType: String): List<Facility> {
        return facilityRepository.findAllByFacilityNameContainingOrderByHitCountDesc(inputFacilityType)
    }
    
    fun getAllRoomEntrance(): MutableList<RoomEntrance> {
        return roomEntranceRepository.findAll()
    }

    // 시설 입구에 있는지 확인
    fun findWithinRange(facilityCurrentLocationDto: FacilityCurrentLocationDto, roomEntrance: RoomEntrance) : Boolean {
        val (leftUpX, leftUpY) = roomEntrance.getEntranceLeftUpXY()
        val (rightDownX, rightDownY) = roomEntrance.getEntranceRightDownXY()

        if(isWithinRangeX(facilityCurrentLocationDto.curX, leftUpX,rightDownX)
                && isWithinRangeY(facilityCurrentLocationDto.curY, leftUpY, rightDownY)) return true
        return false
    }
    
    // 시설 입구 앞인지 확인
    fun isNearFacilityEntrance(facilityCurrentLocationDto: FacilityCurrentLocationDto, roomEntrance: RoomEntrance) : Boolean{
        val (leftUpX, leftUpY) = roomEntrance.getEntranceLeftUpXY()
        val (rightDownX, rightDownY) = roomEntrance.getEntranceRightDownXY()
        val entranceDirection: Int? = roomEntrance.getEntranceDirection()
        val entranceZone : Double? = roomEntrance.getEntranceZone()

        /*
        * 1 : Y + 20 (상 방향)
        * 2 : X + 20 (오른쪽 방향)
        * 3 : Y - 20 (하 방향)
        * 4 : X - 20 (왼쪽 방향)
        * */
        if(entranceDirection == 1 && isWithinRangeX(facilityCurrentLocationDto.curX, leftUpX, rightDownX)
            &&  isWithinRangeY(facilityCurrentLocationDto.curY, leftUpY, leftUpY!! + entranceZone!!)) return true
        else if(entranceDirection == 2 && isWithinRangeY(facilityCurrentLocationDto.curY, leftUpY, rightDownY)
            &&  isWithinRangeY(facilityCurrentLocationDto.curX, rightDownX, rightDownX!! + entranceZone!!)) return true
        else if(entranceDirection == 3 && isWithinRangeY(facilityCurrentLocationDto.curX, leftUpX, rightDownX)
            &&  isWithinRangeY(facilityCurrentLocationDto.curY, rightDownY, rightDownY!! - entranceZone!!)) return true
        else if(entranceDirection == 2 && isWithinRangeY(facilityCurrentLocationDto.curY, leftUpY, rightDownY)
            &&  isWithinRangeY(facilityCurrentLocationDto.curX, leftUpX, leftUpX!! - entranceZone!!)) return true
        return false

    }

    fun isWithinRangeX(curX: Double, leftUpX:Double?, rightUpX:Double?) : Boolean{
        return curX in leftUpX!! .. rightUpX!!
    }

    fun isWithinRangeY(curY: Double, leftUpY: Double? ,rightUpY: Double?) : Boolean{
        return curY in leftUpY!! .. rightUpY!!
    }

}

