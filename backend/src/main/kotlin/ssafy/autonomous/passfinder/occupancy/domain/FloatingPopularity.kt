package ssafy.autonomous.passfinder.occupancy.domain

import java.util.Date
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id


@Entity
class FloatingPopularity(

        val time: Date,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int
)