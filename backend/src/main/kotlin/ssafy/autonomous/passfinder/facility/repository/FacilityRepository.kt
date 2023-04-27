package ssafy.autonomous.passfinder.facility.repository

import org.springframework.data.jpa.repository.JpaRepository
import ssafy.autonomous.passfinder.facility.domain.Facility
import java.util.*

//import ssafy.autonomous.passfinder.facility.domain.FacilityType

interface FacilityRepository : JpaRepository<Facility, Int>{
    
    // 시설 Like를 통해 조회, 조회 순으로 내림차순
    // Optional<List<String>>
    fun findAllByFacilityTypeContainingOrderByHitCountDesc(facilityType: String): List<String>?
}