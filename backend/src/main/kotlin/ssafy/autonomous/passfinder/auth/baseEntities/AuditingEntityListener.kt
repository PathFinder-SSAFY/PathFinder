package ssafy.autonomous.passfinder.auth.baseEntities

import java.time.Instant
import javax.persistence.PrePersist
import javax.persistence.PreUpdate

class AuditingEntityListener {

    // 새로 등록
    @PrePersist
    fun prePersist (`object`: Any){
        if(`object` is BaseEntity){
            val entity = `object` as BaseEntity
            entity.createdAt = Instant.now().epochSecond
            entity.updateAt = Instant.now().epochSecond
        }
    }
    
    // 업데이트
    @PreUpdate
    fun preUpdate(`object`: Any) {
        if (`object` is BaseEntity) {
            val entity = `object` as BaseEntity
            entity.updateAt = Instant.now().epochSecond
        }
    }
}