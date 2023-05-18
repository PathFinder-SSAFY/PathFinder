package ssafy.autonomous.pathfinder.domain.facility.service

import ssafy.autonomous.pathfinder.domain.facility.domain.Facility
import ssafy.autonomous.pathfinder.domain.facility.dto.request.FacilityNameRequestDto
import ssafy.autonomous.pathfinder.domain.facility.dto.request.FacilityTypesRequestDto
import ssafy.autonomous.pathfinder.domain.facility.dto.response.FacilityCoordinatesResponseDto
import ssafy.autonomous.pathfinder.domain.facility.dto.response.FacilityIsValidResponseDto
import ssafy.autonomous.pathfinder.domain.facility.dto.response.FacilityMidPointResponseDto
import ssafy.autonomous.pathfinder.domain.floors.domain.RoomEntrance
import ssafy.autonomous.pathfinder.domain.floors.dto.request.FloorsCurrentLocationRequestDto

interface FacilityService {

    // 3-1 필터링 입력했을 때, 조회 수를 기준 우선순위로 출력
    fun facilityDynamic(facilityTypesRequest: FacilityTypesRequestDto): List<String>

    // 3-2 조회한 시설 정보 얻기
    fun getFacilityTypes(facilitySearchRequest: FacilityTypesRequestDto): Facility

    // 3-3 필터링 검색에서 검색어를 입력했을 때, 유효한 검색어(시설 이름)인지 판별하는 API
    fun isValidFacilityValue(facilityTypesRequest: FacilityTypesRequestDto): FacilityIsValidResponseDto

    // 3-4 시설 중앙 좌표 (aStart 알고리즘에 필요한 입구 좌표 구하기)
    fun getEntrancePointFacility(facilityNameRequestDto: FacilityNameRequestDto): FacilityMidPointResponseDto

    // 3-5 시설이름 입력했을 때, 시설 Entity 조회
    fun getFacilityByFacilityName(facilityNameRequestDto: FacilityNameRequestDto): Facility

    // 3-6 시설 입구에 있는지 확인
    fun findWithinRange(floorsCurrentLocationRequestDto: FloorsCurrentLocationRequestDto, roomEntrance: RoomEntrance): Boolean

    // 3-7 시설 입구 앞인지 확인
    fun isNearFacilityEntrance(floorsCurrentLocationRequestDto: FloorsCurrentLocationRequestDto, roomEntrance: RoomEntrance): Boolean

    // 3-8 시설 안인지 확인
    fun isInsideFacility(floorsCurrentLocationRequestDto: FloorsCurrentLocationRequestDto, facility: Facility): Boolean

    // 3-9 시설 이름 및 시설 안인지 확인
    fun isInsideFacilityCheck(floorsCurrentLocationRequestDto: FloorsCurrentLocationRequestDto, facilityName: String): Boolean

}
