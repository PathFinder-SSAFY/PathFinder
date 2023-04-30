package ssafy.autonomous.pathfinder.facility.service

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import ssafy.autonomous.pathfinder.common.exception.facility.FacilityNotFoundException
import ssafy.autonomous.pathfinder.common.response.PathFinderResponseDto
import ssafy.autonomous.pathfinder.facility.domain.Facility
import ssafy.autonomous.pathfinder.facility.dto.request.FacilityTypesRequestDto
import ssafy.autonomous.pathfinder.facility.repository.FacilityJpaRepository
import ssafy.autonomous.pathfinder.facility.repository.FacilityQuerydslRepository

@SpringBootTest
class FacilityServiceImplTest @Autowired constructor(
        private val facilityService: FacilityService,
        private val facilityJpaRepository: FacilityJpaRepository,
        private val facilityQuerydslRepository: FacilityQuerydslRepository
){

    // 테스트에서 repository 사용 후, 삭제
    @AfterEach
    fun clean() {
        facilityJpaRepository.deleteAll()
    }


    @Test
    @DisplayName("시설 타입 조회")
    fun  facilityDynamic(){
        // given
        val request = FacilityTypesRequestDto("CLASSROOM401")

        // when
        val passfinderResponse = facilityService.facilityDynamic(request)
        val filterSearch: List<Facility> ?= passfinderResponse?.data

        // then
        assertThat(passfinderResponse?.status).isEqualTo(HttpStatus.OK)
        assertThat(filterSearch?.first(){ x -> x.getFacilityType() == "CLASSROOM401"})

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
        val resultFacilityTypes: Facility = facilityJpaRepository.findByFacilityName(inputFacilityType).orElseThrow{ FacilityNotFoundException() }

        resultFacilityTypes.plusHitCount()
        val resultFacility = facilityQuerydslRepository.updateFacility(resultFacilityTypes)

        // then
        assertThat(resultFacilityTypes.getHisCount()).isEqualTo(resultFacility.getHisCount())

    }
}