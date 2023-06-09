package ssafy.autonomous.pathfinder.domain.floors.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ssafy.autonomous.pathfinder.domain.floors.dto.request.FloorsCurrentLocationRequestDto
import ssafy.autonomous.pathfinder.domain.floors.dto.request.FloorsCurrentLocationUpdateRequestDto
import ssafy.autonomous.pathfinder.domain.floors.service.FloorsService
import ssafy.autonomous.pathfinder.global.common.response.ApiResponse
import javax.websocket.server.PathParam

/*
*
* 4.
*
* */
@Api(tags = ["층 컨트롤러"])
@RestController
class FloorsController(
    private val floorsService: FloorsService
) {


    // 4-1 현재 나의 위치 조회
    @ApiOperation(value = "현재 위치 조회 API")
    @ApiImplicitParams(
        ApiImplicitParam(name = "x", value = "0 < x < 65.xx", dataTypeClass = Double::class),
        ApiImplicitParam(name = "y", value = "0.0 입력 필수(4층만)", dataTypeClass = Double::class),
        ApiImplicitParam(name = "z", value = "-12.xx < z < 0", dataTypeClass = Double::class)
    )
    @PostMapping("/floors/curloc")
    fun getCurrentLocation(@RequestBody floorsCurrentLocationRequestDto: FloorsCurrentLocationRequestDto): ResponseEntity<ApiResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse(
                responseData =floorsService.getCurrentLocation(floorsCurrentLocationRequestDto)
            )
        )
    }


    // 4-2 벽 0.1m 간력 점들을 반환하는 함수
    @ApiOperation(value = "벽 0.1m 기준 모든 점들을 List로 얻기")
    @ApiImplicitParam(name = "WallPosition")
    @GetMapping("/floors/wallposition")
    fun getWallPosition() : ResponseEntity<ApiResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse(
                responseData =floorsService.getWallPositions()
            )
        )
    }

    // 4-2-1 벽(장애물) 사각지대 위치 전달
    @ApiOperation(value = "벽 0.1m 기준 모든 점들을 List로 얻기")
    @ApiImplicitParam(name = "WallBlindSpots")
    @GetMapping("/floors/wallblindspots")
    fun getWallBlindSpots() : ResponseEntity<ApiResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse(
                responseData =floorsService.getWallBlindSpots()
            )
        )
    }

    // 4-3. 현재 시설 위치 조회 후, 확인 클릭시 실행된다.
    @ApiOperation(value = "고객 위치 업데이트 (현재 위치 조회할 때 확인 버튼 클릭시 실행)")
    @ApiImplicitParams(
        ApiImplicitParam(name = "x", value = "0 < x < 65.xx", dataTypeClass = Double::class),
        ApiImplicitParam(name = "y", value = "0.0 입력 필수(4층만)", dataTypeClass = Double::class),
        ApiImplicitParam(name = "z", value = "-12.xx < z < 0", dataTypeClass = Double::class),
        ApiImplicitParam(name = "facilityName", value = "시설 이름을 입력하세요", dataTypeClass = String::class)
    )
    @PatchMapping("/floors/update/customer/location/{id}")
    fun updateCustomerLocation(@PathVariable id : String, @RequestBody floorsCurrentLocationUpdateRequestDto: FloorsCurrentLocationUpdateRequestDto) : ResponseEntity<ApiResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse(
                responseData = floorsService.updateCustomerLocation(id, floorsCurrentLocationUpdateRequestDto)
            )
        )
    }

}