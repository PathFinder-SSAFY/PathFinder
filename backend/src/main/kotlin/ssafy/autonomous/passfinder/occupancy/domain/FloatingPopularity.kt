package ssafy.autonomous.passfinder.occupancy.domain

import java.util.Date
import javax.persistence.*


@Entity
class FloatingPopularity(

        val time: Date,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "floating_id" )
        val id: Int


)