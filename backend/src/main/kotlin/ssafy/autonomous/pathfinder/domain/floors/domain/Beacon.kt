package ssafy.autonomous.pathfinder.domain.floors.domain

import javax.persistence.*

@Entity
class Beacon(

    val beaconX : Double,
    val beaconY : Double,
    val beaconZ : Double,

    @Id
    @Column(name = "beacon_id")
    val id: Int,

    // 층과 beacon 1 : N
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floors_id")
    private val floors: Floors? = null,
)