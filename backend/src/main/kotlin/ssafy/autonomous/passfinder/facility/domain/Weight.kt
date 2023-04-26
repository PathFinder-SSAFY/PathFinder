package ssafy.autonomous.passfinder.facility.domain

import javax.persistence.*

@Entity
class Weight(

        val hardness: String, // 경도
        val denesly: String, // 밀집도

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "weight_id")
        val id: Int,
        
        // 일대일
        @OneToOne
        @JoinColumn(name = "room_map_id")
        val roomMap: RoomMap ?= null
)