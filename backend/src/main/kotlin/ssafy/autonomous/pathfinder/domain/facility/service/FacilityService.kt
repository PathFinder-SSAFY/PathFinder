package ssafy.autonomous.pathfinder.domain.facility.service

import ssafy.autonomous.pathfinder.domain.facility.domain.Facility
import ssafy.autonomous.pathfinder.domain.facility.dto.request.FacilityTypesRequestDto
import ssafy.autonomous.pathfinder.domain.facility.dto.response.FacilityIsValidResponseDto

interface FacilityService {



    // 3-1 필터링 입력했을 때, 조회 수를 기준 우선순위로 출력
    fun facilityDynamic(facilityTypesRequest: FacilityTypesRequestDto): List<String>

    // 3-2 조회한 시설 정보 얻기
    fun getFacilityTypes(facilitySearchRequest: FacilityTypesRequestDto): Facility

    // 3-3 필터링 검색에서 검색어를 입력했을 때, 유효한 검색어(시설 이름)인지 판별하는 API
    fun isValidFacilityValue(facilityTypesRequest: FacilityTypesRequestDto): FacilityIsValidResponseDto

}
