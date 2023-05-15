package ssafy.autonomous.pathfinder.domain.building.domain

import javax.persistence.*

@Entity
class Customer(

    val currentLocationFacility: String?,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    val id:Int = 0
)