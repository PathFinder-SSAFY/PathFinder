package ssafy.autonomous.pathfinder.domain.building.service

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ssafy.autonomous.pathfinder.domain.building.dto.request.BuildingNfcRequestDto
import ssafy.autonomous.pathfinder.domain.building.dto.response.BuildingNfcResponseDto
import ssafy.autonomous.pathfinder.domain.building.repository.BuildingRepository
import java.util.*

@SpringBootTest
class BuildingServiceImplTest @Autowired constructor(
    private val buildingService : BuildingService,
    private val buildingRepository: BuildingRepository
){


    @AfterEach
    fun clean(){
        buildingRepository.deleteAll()
    }

    @Test
    @DisplayName("초기 값, nfc 조회")
    fun getBuildingNfc(){
        // given
        val request = BuildingNfcRequestDto("4")

        // when
        val buildingNfc:BuildingNfcResponseDto = buildingService.getBuildingNfc()
        val floorsNumber: List<String> = listOf("1F", "2F", "3F", "4F")

        // then
        Assertions.assertThat(buildingNfc.floorsNumber).containsExactlyElementsOf(floorsNumber)
    }
}