package ssafy.autonomous.pathfinder.domain.facility.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import ssafy.autonomous.pathfinder.domain.facility.domain.Facility
import ssafy.autonomous.pathfinder.domain.facility.domain.FacilityCoordinate
import ssafy.autonomous.pathfinder.domain.facility.dto.request.FacilityNameRequestDto
import ssafy.autonomous.pathfinder.domain.facility.dto.request.FacilityTypesRequestDto
import ssafy.autonomous.pathfinder.domain.facility.dto.response.FacilityIsValidResponseDto
import ssafy.autonomous.pathfinder.domain.facility.dto.response.FacilityMidPointResponseDto
import ssafy.autonomous.pathfinder.domain.facility.dto.response.FacilitySearchNeedCoordinateDto
import ssafy.autonomous.pathfinder.domain.facility.exception.FacilityNotFoundException
import ssafy.autonomous.pathfinder.domain.facility.repository.FacilityRepository
import ssafy.autonomous.pathfinder.domain.facility.repository.FacilityQuerydslRepository
import ssafy.autonomous.pathfinder.domain.facility.repository.RoomEntranceRepository
import ssafy.autonomous.pathfinder.domain.floors.domain.RoomEntrance
import java.math.BigDecimal
import java.util.*
import javax.transaction.Transactional
import kotlin.math.pow
import kotlin.math.roundToLong

@Service
class FacilityServiceImpl(
    private val facilityRepository: FacilityRepository,
    private val facilityQuerydslRepository: FacilityQuerydslRepository,
    private val roomEntranceRepository: RoomEntranceRepository
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

    // aStart 알고리즘에 필요한 입구 좌표 구하기
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

    fun fetchFacilityCoordinates(facility: Facility) : FacilitySearchNeedCoordinateDto{
        // x, y, z 좌표 조회
        val facilityType = FacilityCoordinate.valueOf(facility.getFacilityType())
        val xCoordinate = facilityType.x
        val yCoordinate = facilityType.y
        val zCoordinate = facilityType.z

        return FacilitySearchNeedCoordinateDto(
            x = xCoordinate,
            y = yCoordinate,
            z = zCoordinate
        )
    }
    // 입력한 문자열을 기반으로 방 이름 리스트를 가져온다.
    fun getFacilityTypesDynamic(inputFacilityType: String): List<Facility> {
        return facilityRepository.findByFacilityNameContainingOrderByHitCountDesc(inputFacilityType)
    }

    // 시설 입력했을 때, 올바른 값인지 판단하는 함수 (예외가 아닌 null을 전달)
    fun processValidFacilityName(inputFacilityType: String): Optional<Facility> {
        return facilityRepository.findByFacilityName(inputFacilityType)
    }


}

