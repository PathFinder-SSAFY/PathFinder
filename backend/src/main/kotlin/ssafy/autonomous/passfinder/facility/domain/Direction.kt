package ssafy.autonomous.passfinder.facility.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Direction(

        val up: Boolean,
        val down: Boolean,
        val left: Boolean,
        val right: Boolean,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int
)