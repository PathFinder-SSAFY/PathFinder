package ssafy.autonomous.pathfinder.domain.building.dto.response

import ssafy.autonomous.pathfinder.domain.floors.domain.Beacon

data class BuildingNfcResponseDto(
    val beaconList: List<Beacon>,
    val mapImageUrl: List<String>,
    val floorsNumber: List<String>,
    val customerId: Int
)