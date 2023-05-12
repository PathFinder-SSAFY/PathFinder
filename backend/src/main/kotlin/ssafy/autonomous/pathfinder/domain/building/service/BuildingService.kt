package ssafy.autonomous.pathfinder.domain.building.service

import org.springframework.stereotype.Service
import ssafy.autonomous.pathfinder.domain.building.dto.request.BuildingNfcRequestDto
import ssafy.autonomous.pathfinder.domain.building.dto.response.BuildingNfcResponseDto


interface BuildingService {

    // 2-1 초기 값, nfc
    fun getBuildingNfc() : BuildingNfcResponseDto
}