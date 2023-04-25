package ssafy.autonomous.passfinder.facility.domain

import javax.persistence.*

@Entity
class Direction(

        val up: Boolean,
        val down: Boolean,
        val left: Boolean,
        val right: Boolean,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name="direction_id")
        val id: Int
)