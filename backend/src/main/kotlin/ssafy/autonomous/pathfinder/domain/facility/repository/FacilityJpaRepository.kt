package ssafy.autonomous.pathfinder.domain.facility.repository

import org.springframework.data.jpa.repository.JpaRepository
import ssafy.autonomous.pathfinder.domain.facility.domain.Facility
import java.util.*

//import ssafy.autonomous.pathfinder.facility.domain.FacilityType

interface FacilityJpaRepository : JpaRepository<Facility, Int>{
    
    // 시설 Like를 통해 조회, 조회 순으로 내림차순
    fun findAllByFacilityNameContainingOrderByHitCountDesc(facilityName: String) : List<Facility>

    // 시설 조회
    fun findByFacilityName(facilityName: String): Optional<Facility>
}