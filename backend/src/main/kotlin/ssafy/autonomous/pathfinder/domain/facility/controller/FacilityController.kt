package ssafy.autonomous.pathfinder.domain.facility.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import ssafy.autonomous.pathfinder.domain.facility.dto.request.FacilityNameRequestDto
import ssafy.autonomous.pathfinder.domain.facility.dto.request.FacilityTypesRequestDto
import ssafy.autonomous.pathfinder.domain.facility.dto.response.FacilityIsValidResponseDto
import ssafy.autonomous.pathfinder.domain.facility.service.FacilityService
import ssafy.autonomous.pathfinder.global.common.response.ApiResponse

/*
*
* 3.
*
* */
@Api(tags = ["시설 컨트롤러"])
@Controller
//@RequestMapping("/facility") : 조회할 때, RequestMapping으로 찾기는 어려움
class FacilityController(
    private val facilityService: FacilityService
) {

    /*
     3-1 필터링 입력했을 때, 조회 수를 기준 우선순위로 출력
     설명 : 필터링 검색 입력했을 때, 관련 시설 출력 (우선순위 : 조회 횟수)
     - 첫 실행 시, 조회 횟수가 많은 순으로
     - 사용자가 입력한 형식에 맞게 호출 (우선순위 : 조회 횟수가 많은 순으로)
     */
    @ApiOperation(value = "필터링 검색 입력했을 때, 관련 시설 출력 (우선순위 : 조회 횟수)")
    @ApiImplicitParam(name = "id", value = "유저 아이디(고유 식별 번호)", dataTypeClass = FacilityTypesRequestDto::class)
    @PostMapping("/facility/dynamic")
    fun facilityDynamic(@RequestBody facilityTypesRequest: FacilityTypesRequestDto): ResponseEntity<ApiResponse> {
        // service에서 Repository 호출
        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse(
                responseData =facilityService.facilityDynamic(facilityTypesRequest)
            )
        )
    }

    /*
     3-2 조회한 시설 정보 얻기
     설명 : 시설 조회 클릭 시, 조회 횟수 1씩 증가 시키기
     - 필터링에 입력 후, 검색 버튼 클릭
     */
    @ApiOperation(value = "게시글 조회 클릭 (조회 횟수 1씩 증가 시키기)")
    @ApiImplicitParam(name = "filteringSearch", value = "시설 이름 맞춰서 입력", dataTypeClass = FacilityTypesRequestDto::class)
    @PostMapping("/facility/search")
    fun getFacilityTypes(@RequestBody facilityTypesRequest: FacilityTypesRequestDto): ResponseEntity<ApiResponse> {

        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse(
                responseData =facilityService.getFacilityTypes(facilityTypesRequest)
            )
        )
    }


    // 3-3 필터링 검색에서 검색어를 입력했을 때, 유효한 검색어(시설 이름)인지 판별하는 API
    @ApiOperation(value = "필터링 검색에서 검색어를 입력했을 때, 유효한 검색어(시설 이름)인지 판별하는 API")
    @ApiImplicitParam(name = "filteringSearch", value = "유효한 검색어인지 알고 싶은 값을 입력하세요.", dataTypeClass = FacilityTypesRequestDto::class)
    @PostMapping("/facility/improvements")
    fun isValidFacilityValue(@RequestBody facilityTypesRequest: FacilityTypesRequestDto): ResponseEntity<FacilityIsValidResponseDto>{
        return ResponseEntity.status(HttpStatus.OK).body(
            facilityService.isValidFacilityValue(facilityTypesRequest)
        )
    }


    // 3-4 시설 중앙 좌표
    @ApiOperation(value = "시설 전달 시, 시설 입구 좌표 반환해주는 API")
    @ApiImplicitParam(name= "filteringName", value = "시설을 입력해주세요.", dataTypeClass = String::class)
    @PostMapping("/facility/room/entrance")
    fun getEntrancePointFacility(@RequestBody facilityNameRequestDto: FacilityNameRequestDto) : ResponseEntity<ApiResponse>{
        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse(
                responseData  = facilityService.getEntrancePointFacility(facilityNameRequestDto)
            )
        )
    }
}