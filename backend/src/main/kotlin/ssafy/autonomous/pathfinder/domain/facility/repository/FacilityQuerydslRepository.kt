package ssafy.autonomous.pathfinder.domain.facility.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import mu.KotlinLogging
import org.springframework.stereotype.Component
import ssafy.autonomous.pathfinder.global.config.QuerydslConfig
import ssafy.autonomous.pathfinder.domain.facility.domain.Facility
import javax.persistence.EntityManager

// querydsl 사용할 때, 이용한 repository
@Component
class FacilityQuerydslRepository(
        private val querydslConfig: QuerydslConfig
) {

    private val logger = KotlinLogging.logger {}


    // 22.
    // - 사용자가 검색했을 때, 목적지 + 1
    fun updateFacility(facility: Facility) : Facility{
        val em : EntityManager = querydslConfig.getEntityManager()

//        logger.info("시설이 업데이트 되었습니다.")

        // EntityManager를 통해 Entity update
        // - 사용한 이유 : 엔티티가 영속성 컨텍스트에서 관리되고 있지 않거나, 영속성 컨텍스트에서 분리되어 있다면 DB에 저장되지 않는다.
        em.merge(facility)

        return facility
    }
}