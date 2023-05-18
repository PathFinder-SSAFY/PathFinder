package ssafy.autonomous.pathfinder.domain.facility.domain

import ssafy.autonomous.pathfinder.domain.floors.domain.RoomMap
import javax.persistence.*

@Entity
class Direction(

        val upWard: Boolean ?= false,
        val downWard: Boolean ?= false,
        val leftWard: Boolean ?= false,
        val rightWard: Boolean ?= false,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name="direction_id")
        val id: Int,


        @OneToOne
        @JoinColumn(name="room_map_id")
        var roomMap: RoomMap?= null
)