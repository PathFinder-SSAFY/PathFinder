package ssafy.autonomous.pathfinder.facility.service

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ssafy.autonomous.pathfinder.domain.facility.domain.Facility
import ssafy.autonomous.pathfinder.domain.facility.dto.request.FacilityTypesRequestDto
import ssafy.autonomous.pathfinder.domain.facility.exception.FacilityNotFoundException
import ssafy.autonomous.pathfinder.domain.facility.repository.FacilityQuerydslRepository
import ssafy.autonomous.pathfinder.domain.facility.repository.FacilityRepository
import ssafy.autonomous.pathfinder.domain.facility.service.FacilityService
import ssafy.autonomous.pathfinder.domain.floors.dto.request.FloorsCurrentLocationRequestDto
import ssafy.autonomous.pathfinder.domain.floors.service.FloorsService

@SpringBootTest
class FacilityServiceImplTest @Autowired constructor(
    private val floorsService: FloorsService,
    private val facilityService: FacilityService,
    private val facilityRepository: FacilityRepository,
    private val facilityQuerydslRepository: FacilityQuerydslRepository
){

    // 테스트에서 repository 사용 후, 삭제
    @AfterEach
    fun clean() {
        facilityRepository.deleteAll()
    }


    @Test
    @DisplayName("시설 타입 조회")
    fun  facilityDynamic(){
        // given

        val facilityList = mutableListOf<String>()
        facilityList.add(FacilityTypesRequestDto("CLASSROOM401").filteringSearch)
        facilityList.add(FacilityTypesRequestDto("CLASSROOM402").filteringSearch)
        facilityList.add(FacilityTypesRequestDto("AUDITORIUM101").filteringSearch)

        val request = FacilityTypesRequestDto("CLASSROOM401")

        // when
        val passfinderResponse = facilityService.facilityDynamic(request)
        val filterSearch: List<String> = passfinderResponse

        // then

        assertThat(filterSearch.isNotEmpty())
        assertThat(filterSearch.all { it == "강의실" })

    }


    // delete 문이 실행되서 FacilityNotFoundException
    // db 데이터 다 날라감
    // 실행시 오류 발생
    @Test
    @DisplayName("필터링 입력 결과")
    fun getFacilityTypes(){
        // given
        val inputFacilityType = FacilityTypesRequestDto("CLASSROOM401").filteringSearch

        // when
        val resultFacilityTypes: Facility = facilityRepository.findByFacilityName(inputFacilityType).orElseThrow{ FacilityNotFoundException() }

        resultFacilityTypes.plusHitCount()
        val resultFacility = facilityQuerydslRepository.updateFacility(resultFacilityTypes)

        // then
        assertThat(resultFacilityTypes.getHisCount()).isEqualTo(resultFacility.getHisCount())

    }


    @Test
    @DisplayName("현재 나의 위치를 통한 시설 조회")
    fun getCurrentLocation(){
        // given
        val currentLocation = FloorsCurrentLocationRequestDto(0.0, 0.0, -10.0)

        // when
        val resultCurrentLocation = floorsService.getCurrentLocation(currentLocation)

        // then
        assertThat(resultCurrentLocation).isEqualTo("4층 대강의실 안 입니다.")
    }
}