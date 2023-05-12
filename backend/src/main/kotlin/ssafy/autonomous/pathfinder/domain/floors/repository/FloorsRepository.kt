package ssafy.autonomous.pathfinder.domain.floors.repository

import org.springframework.data.jpa.repository.JpaRepository
import ssafy.autonomous.pathfinder.domain.floors.domain.Floors

interface FloorsRepository : JpaRepository<Floors, Int>{

    fun findByFloorNumber(floorNumber: String?) : Floors
}