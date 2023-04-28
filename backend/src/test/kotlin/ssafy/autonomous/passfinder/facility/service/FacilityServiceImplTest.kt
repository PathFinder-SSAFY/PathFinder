package ssafy.autonomous.passfinder.facility.service

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import ssafy.autonomous.passfinder.facility.domain.Facility
import ssafy.autonomous.passfinder.facility.dto.request.FacilityTypesRequestDto
import ssafy.autonomous.passfinder.facility.repository.FacilityJpaRepository

@SpringBootTest
class FacilityServiceImplTest @Autowired constructor(
        private val facilityService: FacilityService,
        private val facilityJpaRepository: FacilityJpaRepository
){

    // 테스트에서 repository 사용 후, 삭제
    @AfterEach
    fun clean() {
        facilityJpaRepository.deleteAll()
    }

    @Test
    fun  getFacilityTypes(){
        // given
        val request = FacilityTypesRequestDto("CLASSROOM401")

        // when
        val passfinderResponse = facilityService.facilityDynamic(request)
        val filterSearch: List<Facility> ?= passfinderResponse?.data

        // then
        assertThat(passfinderResponse?.status).isEqualTo(HttpStatus.OK)
        assertThat(filterSearch?.first(){ x -> x.getFacilityType() == "CLASSROOM401"})

    }
}