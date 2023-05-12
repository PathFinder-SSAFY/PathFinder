package ssafy.autonomous.pathfinder.domain.facility.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import ssafy.autonomous.pathfinder.domain.facility.dto.request.FacilityCurrentLocationRequestDto
import ssafy.autonomous.pathfinder.domain.facility.dto.request.FacilityTypesRequestDto
import ssafy.autonomous.pathfinder.domain.facility.service.FacilityService
import ssafy.autonomous.pathfinder.global.common.response.ApiResponse

// 20 ~ 40
@Api(tags = ["필터링 검색 컨트롤러"])
@Controller
//@RequestMapping("/facility") : 조회할 때, RequestMapping으로 찾기는 어려움
class FacilityController(
    private val facilityService: FacilityService
) {

    // 3-1 필터링 입력했을 때, 조회 수를 기준 우선순위로 출력
    // 설명 : 필터링 검색 입력했을 때, 관련 시설 출력 (우선순위 : 조회 횟수)
    // - 첫 실행 시, 조회 횟수가 많은 순으로
    // - 사용자가 입력한 형식에 맞게 호출 (우선순위 : 조회 횟수가 많은 순으로)
    @ApiOperation(value = "필터링 검색 입력했을 때, 관련 시설 출력 (우선순위 : 조회 횟수)")
    @ApiImplicitParam(name = "id", value = "유저 아이디(고유 식별 번호)", dataTypeClass = FacilityTypesRequestDto::class)
    @PostMapping("/facility/dynamic")
    fun facilityDynamic(@RequestBody facilityTypesRequest: FacilityTypesRequestDto): ResponseEntity<ApiResponse> {
        // service에서 Repository 호출
        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse(
                data = facilityService.facilityDynamic(facilityTypesRequest)
            )
        )
    }

    // 3-2 조회한 시설 정보 얻기
    // 설명 : 시설 조회 클릭 시, 조회 횟수 1씩 증가 시키기
    // - 필터링에 입력 후, 검색 버튼 클릭
    @ApiOperation(value = "게시글 조회 클릭 (조회 횟수 1씩 증가 시키기)")
    @ApiImplicitParam(name = "filteringSearch", value = "시설 이름 맞춰서 입력", dataTypeClass = FacilityTypesRequestDto::class)
    @PostMapping("/facility/search")
    fun getFacilityTypes(@RequestBody facilitySearchRequest: FacilityTypesRequestDto): ResponseEntity<ApiResponse> {

        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse(
                data = facilityService.getFacilityTypes(facilitySearchRequest)
            )
        )
    }



    // 3-3 현재 나의 위치 조회
    @ApiOperation(value = "현재 위치 조회 API")
    @ApiImplicitParams(
        ApiImplicitParam(name = "x", value = "0 < x < 65.xx", dataType = "double"),
        ApiImplicitParam(name = "y", value = "0.0 입력 필수(4층만)", dataType = "double"),
        ApiImplicitParam(name = "z", value = "-12.xx < z < 0", dataType = "double")

    )

    @PostMapping("/facility/curloc")
    fun getCurrentLocation(@RequestBody facilityCurrentLocationRequestDto: FacilityCurrentLocationRequestDto): ResponseEntity<ApiResponse>{
        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse(
                data = facilityService.getCurrentLocation(facilityCurrentLocationRequestDto)
            )
        )
    }


    // 3-4 벽 0.1m 간력 점들을 반환하는 함수
    @ApiOperation(value = "벽 0.1m 기준 모든 점들을 List로 얻기")
    @ApiImplicitParam(name = "WallPosition")
    @GetMapping("/facility/wallposition")
    fun getWallPosition() : ResponseEntity<ApiResponse>{
        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse(
                data = facilityService.getWallPositions()
            )
        )
    }

    // 3-4-1 벽(장애물) 사각지대 위치 전달
    @ApiOperation(value = "벽 0.1m 기준 모든 점들을 List로 얻기")
    @ApiImplicitParam(name = "WallBlindSpots")
    @GetMapping("/facility/wallblindspots")
    fun getWallBlindSpots() : ResponseEntity<ApiResponse>{
        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse(
                data = facilityService.getWallBlindSpots()
            )
        )
    }

}