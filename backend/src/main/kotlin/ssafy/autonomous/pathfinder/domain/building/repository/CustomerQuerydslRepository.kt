package ssafy.autonomous.pathfinder.domain.building.repository

import org.springframework.stereotype.Component
import ssafy.autonomous.pathfinder.domain.building.domain.Customer
import ssafy.autonomous.pathfinder.global.config.QuerydslConfig
import javax.persistence.EntityManager

@Component
class CustomerQuerydslRepository(
    private val querydslConfig: QuerydslConfig
) {

    fun updateCustomer(customer: Customer){
        val em : EntityManager = querydslConfig.getEntityManager()

        em.merge(customer)

//        return customer
    }
}