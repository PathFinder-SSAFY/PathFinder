package ssafy.autonomous.passfinder.auth.baseEntities

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(value=[AuditingEntityListener::class])
class BaseEntity(

        @Column(updatable = false)
        @CreatedDate
        var createdAt: Long,

        @LastModifiedBy
        var updateAt: Long
)