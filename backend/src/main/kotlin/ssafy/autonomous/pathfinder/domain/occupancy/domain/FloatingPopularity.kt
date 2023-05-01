package ssafy.autonomous.pathfinder.domain.occupancy.domain

import ssafy.autonomous.pathfinder.facility.domain.Facility
import java.util.Date
import javax.persistence.*


@Entity
class FloatingPopularity(

        val time: Date,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "floating_id" )
        val id: Int,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "facility_id")
        var facility: Facility ?= null

)