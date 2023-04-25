package ssafy.autonomous.passfinder.facility.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Weight(

        val hardness: String, // 경도
        val denesly: String, // 밀집도

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "weight_id")
        val id: Int
)