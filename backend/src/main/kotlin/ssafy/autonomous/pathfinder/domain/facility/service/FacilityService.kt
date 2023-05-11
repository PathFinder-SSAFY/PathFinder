package ssafy.autonomous.pathfinder.domain.facility.service

import ssafy.autonomous.pathfinder.domain.facility.domain.Facility
import ssafy.autonomous.pathfinder.domain.facility.domain.RoomEntrance
import ssafy.autonomous.pathfinder.domain.facility.dto.request.FacilityCurrentLocationDto
import ssafy.autonomous.pathfinder.domain.facility.dto.request.FacilityTypesRequestDto
import java.util.*

interface FacilityService {


    // 필터링 입력했을 때, 조회 수를 기준 우선순위로 출력
    fun facilityDynamic(facilityTypesRequest: FacilityTypesRequestDto): List<Facility>

    // 조회한 시설 정보 얻기
    fun getFacilityTypes(facilitySearchRequest: FacilityTypesRequestDto): Facility

    // 3-3
    fun getCurrentLocation(facilityCurrentLocationDto: FacilityCurrentLocationDto): String
}
