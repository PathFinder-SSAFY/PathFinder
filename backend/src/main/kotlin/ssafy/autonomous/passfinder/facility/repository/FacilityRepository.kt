package ssafy.autonomous.passfinder.facility.repository

import org.springframework.stereotype.Repository
import ssafy.autonomous.passfinder.facility.domain.Facility
import java.util.*
import javax.persistence.EntityManager


// EntityManager와 JpaRepository 통합해서 사용
@Repository
class FacilityRepository(
        private val em: EntityManager,
        private val facilityJpaRepository: FacilityJpaRepository
) {

    // 1. EntityManger (JpaRepository X)
    // - patch, delete
    fun updateFacility(facility: Facility): Facility{
        return em.merge(facility)
    }


    // 2. JpaRepository
    // - post, get
    fun findAllByFacilityNameContainingOrderByHitCountDesc(facilityName: String) : List<Facility>{
        return facilityJpaRepository.findAllByFacilityNameContainingOrderByHitCountDesc(facilityName)
    }

    fun findByFacilityName(facilityName: String): Optional<Facility>{
        return facilityJpaRepository.findByFacilityName(facilityName)
    }

}