package ssafy.autonomous.pathfinder.domain.facility.service

import junit.framework.TestCase.assertEquals
import mu.KotlinLogging
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ssafy.autonomous.pathfinder.domain.facility.domain.Facility
import ssafy.autonomous.pathfinder.domain.facility.dto.request.FacilityNameRequestDto
import ssafy.autonomous.pathfinder.domain.facility.dto.request.FacilityTypesRequestDto
import ssafy.autonomous.pathfinder.domain.facility.exception.FacilityNotFoundException
import ssafy.autonomous.pathfinder.domain.facility.repository.FacilityQuerydslRepository
import ssafy.autonomous.pathfinder.domain.facility.repository.FacilityRepository
import ssafy.autonomous.pathfinder.domain.facility.service.FacilityService
import ssafy.autonomous.pathfinder.domain.floors.dto.request.FloorsCurrentLocationRequestDto
import ssafy.autonomous.pathfinder.domain.floors.service.FloorsService
import javax.transaction.Transactional

@SpringBootTest
class FacilityServiceImplTest @Autowired constructor(
    private val floorsService: FloorsService,
    private val facilityService: FacilityService,
    private val facilityRepository: FacilityRepository,
    private val facilityQuerydslRepository: FacilityQuerydslRepository
){


    private val logger = KotlinLogging.logger{}

    @Test
    @DisplayName("시설 타입 조회")
    fun  facilityDynamic(){
        // given

        val facilityList = mutableListOf<String>()
        facilityList.add(FacilityTypesRequestDto("강의실 401").filteringSearch)
        facilityList.add(FacilityTypesRequestDto("강의실 403").filteringSearch)
        facilityList.add(FacilityTypesRequestDto("4층 대강의실").filteringSearch)
//        facilityList.add(FacilityTypesRequestDto("강의실 402").filteringSearch)


        val request = FacilityTypesRequestDto("강의실")

        // when
        val passfinderResponse = facilityService.facilityDynamic(request)


        // then
        Assertions.assertEquals(facilityList, passfinderResponse)
    }

    // 테스트에서 repository 사용 후, 삭제
//    @AfterEach
//    fun clean() {
//        facilityRepository.deleteAll()
//    }

    @Test
    @Transactional
    @DisplayName("필터링 검색 결과")
    fun getFacilityTypes(){
        // given
        val requestFacilityType = FacilityTypesRequestDto("4층 대강의실")
        // - 시설이 존재하지 않을 경우, FacilityNotFoundException 발생한다.
        val requestFacilityTypes: Facility = facilityRepository.findByFacilityName(requestFacilityType.filteringSearch).orElseThrow{ FacilityNotFoundException() }
        requestFacilityTypes.plusHitCount()

        // Querydsl
        val responseFacility = facilityQuerydslRepository.updateFacility(requestFacilityTypes)
        val count = responseFacility.getHisCount()
        logger.info("횟수 : $count")

        // when
        val responseFacility2 =  facilityService.getFacilityTypes(requestFacilityType)
        val count2 = responseFacility2.getHisCount()
        logger.info("횟수2 : $count2")


        // then
        Assertions.assertEquals(responseFacility, responseFacility2)

        assertThat(responseFacility.getHisCount()).isEqualTo(responseFacility2.getHisCount())
        // - 이와 같이 조회할 시, 똑같은 Entity를 가르치므로 getHisCount의 결과 값이 같게 된다.
        // - 미리 변수에 저장한다.
        assertThat(count + 1).isEqualTo(count2)
        // count + 1을 하지 않을 시 fail 발생
    }


    @Test
    @DisplayName("현재 나의 위치를 통한 시설 조회")
    fun getCurrentLocation(){
        // given
        val currentLocation = FloorsCurrentLocationRequestDto(0.0, 0.0, -10.0)

        // when
        val resultCurrentLocation = floorsService.getCurrentLocation(currentLocation)

        // then
        assertThat(resultCurrentLocation).isEqualTo("4층 대강의실 안")
    }

    @Test
    @DisplayName("시설 전달 시, 중앙 좌표 반환해주는 API")
    fun getMidpointFacility() {

        // given
        val facilityName = FacilityNameRequestDto("엘리베이터")
        val mayBeResult = listOf(32.0, 0.0, -9.0)


        // when
        val facilityMidPoint = facilityService.getEntrancePointFacility(facilityName)

        // then
        Assertions.assertEquals(mayBeResult, facilityMidPoint)
    }
}