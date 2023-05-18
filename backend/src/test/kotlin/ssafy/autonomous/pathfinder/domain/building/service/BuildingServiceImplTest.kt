package ssafy.autonomous.pathfinder.domain.building.service

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ssafy.autonomous.pathfinder.domain.building.dto.request.BuildingNfcRequestDto
import ssafy.autonomous.pathfinder.domain.building.exception.IdNotFoundException

@SpringBootTest
class BuildingServiceImplTest @Autowired constructor(
    private var buildingService : BuildingService,
){

    private val logger = KotlinLogging.logger{}


    @Throws(IdNotFoundException::class)
    @Test
    @DisplayName("getBuildingNfc는 BuildingNfcResponseDto를 반환해야 한다.")
    fun getBuildingNfc(){
        // given
        val requestDto = BuildingNfcRequestDto("1")

        /*
        * id 값을 입력한다. (1을 제외한 나머지는 막혀있다.)
        * */

        // when
        val responseDto = buildingService.getBuildingNfc(requestDto)


        // then
        Assertions.assertNotNull(responseDto)
        Assertions.assertEquals(listOf("3F"), responseDto.floorsNumber)
    }
}