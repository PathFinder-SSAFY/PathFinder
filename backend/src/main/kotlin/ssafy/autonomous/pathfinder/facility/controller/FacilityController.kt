package ssafy.autonomous.pathfinder.facility.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiModelProperty
import io.swagger.annotations.ApiOperation
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import ssafy.autonomous.pathfinder.common.response.PathFinderResponseDto
import ssafy.autonomous.pathfinder.facility.domain.Facility
import ssafy.autonomous.pathfinder.facility.dto.request.FacilityTypesRequestDto
import ssafy.autonomous.pathfinder.facility.service.FacilityService

// 20 ~ 40
@Api(tags = ["필터링 검색 컨트롤러"])
@Controller
//@RequestMapping("/facility") : 조회할 때, RequestMapping으로 찾기는 어려움
class FacilityController(
        private val facilityService : FacilityService
) {

    // 21.
    // 설명 : 필터링 검색 입력했을 때, 관련 시설 출력 (우선순위 : 조회 횟수)
    // - 첫 실행 시, 조회 횟수가 많은 순으로
    // - 사용자가 입력한 형식에 맞게 호출 (우선순위 : 조회 횟수가 많은 순으로)
    @ApiOperation(value = "필터링 검색 입력했을 때, 관련 시설 출력 (우선순위 : 조회 횟수)")
    @ApiImplicitParam(name = "filteringSearch", value = "필터링 실시간 입력 값", dataType = "String")
    @PostMapping("/facility/dynamic")
    fun facilityDynamic(@RequestBody facilityTypesRequest: FacilityTypesRequestDto ): ResponseEntity<PathFinderResponseDto<List<Facility>>> {
        // service에서 Repository 호출
        val facilityTypeList = facilityService.facilityDynamic(facilityTypesRequest)
        return ResponseEntity.ok(facilityTypeList)
    }

    // 22.
    // 설명 : 시설 조회 클릭 시, 조회 횟수 1씩 증가 시키기
    // - 필터링에 입력 후, 검색 버튼 클릭
    @ApiOperation(value = "게시글 조회 클릭 (조회 횟수 1씩 증가 시키기)")
    @ApiImplicitParam(name = "filteringSearch", value = "시설 이름 맞춰서 입력", dataType = "String")
    @GetMapping("/facility/search")
    fun getFacilityTypes(@RequestBody facilitySearchRequest: FacilityTypesRequestDto):ResponseEntity<PathFinderResponseDto<Facility>>{
        return ResponseEntity.ok(facilityService.getFacilityTypes(facilitySearchRequest))
    }
}