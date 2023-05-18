package ssafy.autonomous.pathfinder.domain.facility.repository

import org.springframework.data.jpa.repository.JpaRepository
import ssafy.autonomous.pathfinder.domain.floors.domain.RoomEntrance

interface RoomEntranceRepository : JpaRepository<RoomEntrance, Int> {

}