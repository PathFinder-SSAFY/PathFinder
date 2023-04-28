package ssafy.autonomous.passfinder.facility.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import ssafy.autonomous.passfinder.facility.domain.Facility
import java.util.*
import javax.persistence.EntityManager



// 순수 JPA 리포지토리
// EntityManager와 JpaRepository 통합해서 사용

// (1) Querydsl 참고해서 정리하기
// - https://github.com/ToDoStudy/QueryDslStudy/blob/main/querydsl/src/main/java/QueryDSLStudy/user/repositoryuser/UserRepositoryInformationImpl.java
// (2) 노션 확인 후 수정하기
@Repository
class FacilityRepository : FacilityJpaRepository{

    private val em: EntityManager ?= null

    private var jpaQueryFactory: JPAQueryFactory = JPAQueryFactory(em) // JPAQueryFactory는 Querydsl 프레임워크를 사용하여 JPA 쿼리를 작성하고 실행하는 데 사용되는 팩토리 클래스


    // 1. EntityManger (JpaRepository X)
    // - patch, delete
    fun updateFacility(facility: Facility): Facility{
        return em!!.merge(facility)
    }

    override fun findAllByFacilityNameContainingOrderByHitCountDesc(facilityName: String): List<Facility> {
        TODO("Not yet implemented")
    }

//    override fun findAllByFacilityNameContainingOrderByHitCountDesc(facilityName: String): List<Facility> {
//        return em.createQuery(
//            "select 문처리 해야함"
//        )
//        return facilityJpaRepository!!.findAllByFacilityNameContainingOrderByHitCountDesc(facilityName)
//    }

    override fun findByFacilityName(facilityName: String): Optional<Facility> {
        return em?.createQuery(
            /* qlString = */
            "SELECT f" +
                    " FROM Facility f" +
                    " WHERE f.facilityName = :facilityName")
            ?.setParameter("facilityName",facilityName)
            ?.resultList?.stream()?.findFirst() as Optional<Facility>
    }


    // 2. JpaRepository
    // - post, get
//    fun findAllByFacilityNameContainingOrderByHitCountDesc(facilityName: String) : List<Facility>{
//        return facilityJpaRepository!!.findAllByFacilityNameContainingOrderByHitCountDesc(facilityName)
//    }
//
//    fun findByFacilityName(facilityName: String): Optional<Facility>{
//        return facilityJpaRepository.findByFacilityName(facilityName)
//    }

}