package ssafy.autonomous.passfinder.facility.controller

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import ssafy.autonomous.passfinder.common.response.PassFinderResponseDto
import ssafy.autonomous.passfinder.facility.dto.request.FacilityTypesRequestDto
import ssafy.autonomous.passfinder.facility.dto.response.FacilityTypesResponseDto
import ssafy.autonomous.passfinder.facility.service.FacilityService

// 20 ~ 40
@Controller
//@RequestMapping("/facility") : 조회할 때, RequestMapping으로 찾기는 어려움
class FacilityController(
        private val facilityService : FacilityService
) {


    
    // 21 필터링 검색
    // - 첫 실행 시, 조회 횟수가 많은 순으로
    // - 사용자가 입력한 형식에 맞게 호출 (우선순위 : 조회 횟수가 많은 순으로)
    @GetMapping("/facility/search")
    fun getFacilityTypes(@RequestBody facilityTypesRequest: FacilityTypesRequestDto ): ResponseEntity<PassFinderResponseDto<Unit>> {
        // service에서 Repository 호출
        val facilityTypeList = facilityService.getFacilityTypes(facilityTypesRequest)
        return ResponseEntity.ok(facilityTypeList)
    }
//    @GetMapping
//    fun getFacility
}