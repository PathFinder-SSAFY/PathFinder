package ssafy.autonomous.pathfinder.domain.floors.service

import ssafy.autonomous.pathfinder.domain.floors.domain.Beacon

interface FloorsService {
    
    
    // 비콘 db에서 조회
    fun getBeaconList() : List<Beacon>
}