package ssafy.autonomous.pathfinder.domain.building.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ssafy.autonomous.pathfinder.domain.building.dto.request.BuildingNfcRequestDto
import ssafy.autonomous.pathfinder.domain.building.dto.response.BuildingNfcResponseDto
import ssafy.autonomous.pathfinder.domain.building.service.BuildingService
import ssafy.autonomous.pathfinder.global.common.response.ApiResponse

/*
*
* 2.
* */
@Api(tags = ["건물 컨트롤러"])
@RestController
class BuildingController(
    private val buildingService: BuildingService
) {

    /*
    *
    * 2-1. 건물
    * - beacon 좌표, 층 이미지, 층
    * */
    @ApiOperation(value = "초기 값, nfc")
    @PostMapping("/building/nfc")
    fun getBuildingNfc(@RequestBody buildingNfcRequestDto: BuildingNfcRequestDto): ResponseEntity<BuildingNfcResponseDto>{
        return ResponseEntity.status(HttpStatus.OK).body(buildingService.getBuildingNfc(buildingNfcRequestDto))
    }
}