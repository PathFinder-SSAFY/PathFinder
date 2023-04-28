package ssafy.autonomous.passfinder.facility.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import ssafy.autonomous.passfinder.facility.domain.Facility

// querydsl 사용할 때, 이용한 repository
@Component
class FacilityQuerydslRepository(
        private val queryFactory: JPAQueryFactory,
) {



    // 22.
    // - 사용자가 검색했을 때, 목적지 + 1
//    fun updateFacility(facility: Facility) : List<Facility>{
//        queryFactory
//                .update(facility)
//                .(facility.)
//                .where(facility.getFacilityType())
//                .execute()
//        val execute: Long = jpaQueryFactory
//                .update(user)
//                .set(userDetail.age, age)
//                .setNull(userDetail.address) // null로 업데이트
//                .where(user.id.eq(id))
//                .execute()
//    }
}