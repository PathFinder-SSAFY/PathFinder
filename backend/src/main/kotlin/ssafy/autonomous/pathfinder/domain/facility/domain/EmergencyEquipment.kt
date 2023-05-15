package ssafy.autonomous.pathfinder.domain.facility.domain

import ssafy.autonomous.pathfinder.domain.floors.domain.Floors
import javax.persistence.*

@Entity
class EmergencyEquipment(
    @Column(name = "emergency_name")
    private val emergencyName: String, // 장비 이름

    @Column(name = "emergency_type")
    private val emergencyType: Int, // 장비 타입

    @Column(name = "emergency_x")
    private val emergencyX: Double,

    @Column(name = "emergency_y")
    private val emergencyY: Double,

    @Column(name = "emergency_z")
    private val emergencyZ: Double,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emergency_id")
    val id: Int? = null,

    // 층과 시설 1 : N
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floors_id")
    private val floors: Floors? = null
) {
    fun getEmergencyX(): Double = this.emergencyX
    fun getEmergencyY(): Double = this.emergencyY
    fun getEmergencyZ(): Double = this.emergencyZ
    fun getEmergencyName(): String = this.emergencyName
}