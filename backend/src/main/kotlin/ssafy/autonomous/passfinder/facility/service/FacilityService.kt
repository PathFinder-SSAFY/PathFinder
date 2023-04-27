package ssafy.autonomous.passfinder.facility.service

import ssafy.autonomous.passfinder.common.response.PassFinderResponseDto
import ssafy.autonomous.passfinder.facility.dto.request.FacilityTypesRequestDto
import ssafy.autonomous.passfinder.facility.dto.response.FacilityTypesResponseDto

interface FacilityService {
    // service 메서드 선언
    fun getFacilityTypes(facilityTypesRequest: FacilityTypesRequestDto): PassFinderResponseDto<List<String>>?
}