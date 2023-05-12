package ssafy.autonomous.pathfinder.domain.building.repository

import org.springframework.data.jpa.repository.JpaRepository
import ssafy.autonomous.pathfinder.domain.building.domain.Building

interface BuildingRepository : JpaRepository<Building, Int> {
}