package ssafy.autonomous.passfinder.facility.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class RoomMap(

        
        // 위도, 경도
        val longitude: Double,
        val latitude: Double,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "room_map_id")
        val id: Int

)