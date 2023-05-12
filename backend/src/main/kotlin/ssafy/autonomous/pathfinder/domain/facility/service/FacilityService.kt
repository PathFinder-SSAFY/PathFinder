package ssafy.autonomous.pathfinder.domain.facility.service

import ssafy.autonomous.pathfinder.domain.facility.domain.Facility
import ssafy.autonomous.pathfinder.domain.facility.dto.request.FacilityTypesRequestDto

interface FacilityService {



    // 3-1 필터링 입력했을 때, 조회 수를 기준 우선순위로 출력
    fun facilityDynamic(facilityTypesRequest: FacilityTypesRequestDto): List<String>

    // 3-2 조회한 시설 정보 얻기
    fun getFacilityTypes(facilitySearchRequest: FacilityTypesRequestDto): Facility


}
