package ssafy.autonomous.passfinder.facility.service

import ssafy.autonomous.passfinder.common.response.PassFinderResponseDto
import ssafy.autonomous.passfinder.facility.domain.Facility
import ssafy.autonomous.passfinder.facility.dto.request.FacilityTypesRequestDto

interface FacilityService {


    // 필터링 입력했을 때, 조회 수를 기준 우선순위로 출력
    fun facilityDynamic(facilityTypesRequest: FacilityTypesRequestDto): PassFinderResponseDto<List<Facility>>?

    // 조회한 시설 정보 얻기
    fun getFacilityTypes(facilitySearchRequest: FacilityTypesRequestDto): PassFinderResponseDto<Facility>?
}
