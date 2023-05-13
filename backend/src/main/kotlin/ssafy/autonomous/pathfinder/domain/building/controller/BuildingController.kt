package ssafy.autonomous.pathfinder.domain.building.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ssafy.autonomous.pathfinder.domain.building.dto.request.BuildingNfcRequestDto
import ssafy.autonomous.pathfinder.domain.building.service.BuildingService
import ssafy.autonomous.pathfinder.global.common.response.ApiResponse

@Api(tags = ["건물 컨트롤러"])
@RestController
class BuildingController(
    private val buildingService: BuildingService
) {

    @ApiOperation(value = "초기 값, nfc")
    @PostMapping("/building/nfc")
    fun getBuildingNfc(@RequestBody buildingNfcRequestDto: BuildingNfcRequestDto): ResponseEntity<ApiResponse>{
        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse(
                data = buildingService.getBuildingNfc()
            )
        )
    }
}