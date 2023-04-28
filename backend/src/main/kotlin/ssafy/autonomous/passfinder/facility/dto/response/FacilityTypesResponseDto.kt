package ssafy.autonomous.passfinder.facility.dto.response



data class FacilitySearchResponseDto(
        val facilityName: String, // 시설 이름
        val facilityType: String,
        val densityMax: Int, // 최대 밀집도
        val hitCount: Int, // 시설 조회 횟수
)