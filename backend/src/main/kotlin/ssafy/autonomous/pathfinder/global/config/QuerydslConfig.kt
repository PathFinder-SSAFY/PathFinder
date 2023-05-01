package ssafy.autonomous.pathfinder.global.config

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManager

@Configuration
class QuerydslConfig(
        private val em: EntityManager
) {

    // JPAQueryFactory를 Bean으로 등록
    @Bean
    fun jpaQueryFactory(): JPAQueryFactory{
        return JPAQueryFactory(em)
    }

    /*
    EntityManager를 반환한다.
     EntityManager의 merge와 같은 것을 사용하고 싶을 때 entityManager를 이용하면 된다.
     - 현 엔티티 변경사항을 DB에 저장한다.
     - getEntityManager() 메소드를 호출시 이미 존재하는 EntityManager 빈을 사용하게 된다.
     */
    @Bean
    fun getEntityManager(): EntityManager{
        return em;
    }


}