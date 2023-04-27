package ssafy.autonomous.passfinder.facility.service

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import ssafy.autonomous.passfinder.common.response.PassFinderResponseDto
import ssafy.autonomous.passfinder.facility.dto.request.FacilityTypesRequestDto
import ssafy.autonomous.passfinder.facility.repository.FacilityRepository

@SpringBootTest
class FacilityServiceImplTest @Autowired constructor(
        private val facilityService: FacilityService,
        private val facilityRepository: FacilityRepository
){

    // 테스트에서 repository 사용 후, 삭제
    @AfterEach
    fun clean() {
        facilityRepository.deleteAll()
    }

    @Test
    fun  getFacilityTypes(){
        // given
        val request = FacilityTypesRequestDto("CLASSROOM401")

        // when
        val passfinderResponse = facilityService.getFacilityTypes(request)

        // then
        assertThat(passfinderResponse?.status).isEqualTo(HttpStatus.OK)
        assertThat(passfinderResponse?.data).contains("CLASSROOM401")

    }
}