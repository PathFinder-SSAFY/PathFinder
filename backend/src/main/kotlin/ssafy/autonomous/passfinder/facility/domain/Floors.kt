package ssafy.autonomous.passfinder.facility.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Floors(

        val floorNumber: String,
        val mapImageUrl: String,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int
)