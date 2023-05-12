package ssafy.autonomous.pathfinder.domain.floors.repository

import org.springframework.data.jpa.repository.JpaRepository
import ssafy.autonomous.pathfinder.domain.facility.domain.Floors
import ssafy.autonomous.pathfinder.domain.floors.domain.Beacon

interface FloorsRepository : JpaRepository<Floors, Int>{

    fun findByFloorNumber(floorNumber: String?) : Floors
}