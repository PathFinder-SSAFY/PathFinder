package ssafy.autonomous.pathfinder.domain.floors.repository

import org.springframework.data.jpa.repository.JpaRepository
import ssafy.autonomous.pathfinder.domain.floors.domain.Beacon

interface BeaconRepository : JpaRepository<Beacon, Int> {
}