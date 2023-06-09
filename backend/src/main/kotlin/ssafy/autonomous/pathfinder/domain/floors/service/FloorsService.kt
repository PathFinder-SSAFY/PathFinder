package ssafy.autonomous.pathfinder.domain.floors.service

import ssafy.autonomous.pathfinder.domain.facility.dto.response.WallBlindSpotsResponseDto
import ssafy.autonomous.pathfinder.domain.floors.domain.Beacon
import ssafy.autonomous.pathfinder.domain.floors.dto.request.FloorsCurrentLocationRequestDto
import ssafy.autonomous.pathfinder.domain.floors.dto.request.FloorsCurrentLocationUpdateRequestDto

// 4
interface FloorsService {
    // 4-1 현재 나의 위치 조회
    fun getCurrentLocation(floorsCurrentLocationRequestDto: FloorsCurrentLocationRequestDto): String

    // 4-2 벽 0.1m 간력 점들을 반환하는 함수
    fun getWallPositions(): List<Pair<Double, Double>>

    // 4-2-1 벽(장애물) 사각지대 위치 전달
    fun getWallBlindSpots(): List<WallBlindSpotsResponseDto>

    // 4-3 고객 위치 업데이트 (현재 위치 조회할 때 확인 버튼 클릭시 실행)
    fun updateCustomerLocation(id : String, floorsCurrentLocationUpdateRequestDto: FloorsCurrentLocationUpdateRequestDto): String

    // 비콘 db에서 조회
    fun getBeaconList() : List<Beacon>
}