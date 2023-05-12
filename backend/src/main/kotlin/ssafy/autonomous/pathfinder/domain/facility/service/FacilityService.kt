package ssafy.autonomous.pathfinder.domain.facility.service

import ssafy.autonomous.pathfinder.domain.facility.domain.BlockWall
import ssafy.autonomous.pathfinder.domain.facility.domain.Facility
import ssafy.autonomous.pathfinder.domain.facility.dto.request.FacilityCurrentLocationRequestDto
import ssafy.autonomous.pathfinder.domain.facility.dto.request.FacilityTypesRequestDto
import ssafy.autonomous.pathfinder.domain.facility.dto.response.WallBlindSpotsResponseDto
import java.util.*

interface FacilityService {



    // 3-1 필터링 입력했을 때, 조회 수를 기준 우선순위로 출력
    fun facilityDynamic(facilityTypesRequest: FacilityTypesRequestDto): List<String>

    // 3-2 조회한 시설 정보 얻기
    fun getFacilityTypes(facilitySearchRequest: FacilityTypesRequestDto): Facility

    // 3-3 현재 나의 위치 조회
    fun getCurrentLocation(facilityCurrentLocationRequestDto: FacilityCurrentLocationRequestDto): String

    // 3-4 벽 0.1m 간력 점들을 반환하는 함수
    fun getWallPositions(): List<Pair<Double, Double>>


    // 3-4-1 벽(장애물) 사각지대 위치 전달
    fun getWallBlindSpots(): List<WallBlindSpotsResponseDto>
}
