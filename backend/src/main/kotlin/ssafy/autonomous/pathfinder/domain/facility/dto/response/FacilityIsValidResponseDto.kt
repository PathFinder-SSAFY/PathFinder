package ssafy.autonomous.pathfinder.domain.facility.dto.response

data class FacilityIsValidResponseDto(
    val isValid : Boolean ?= false,
    val points: FacilityCoordinatesResponseDto? = null
)