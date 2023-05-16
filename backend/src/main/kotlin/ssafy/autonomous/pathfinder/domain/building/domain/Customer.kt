package ssafy.autonomous.pathfinder.domain.building.domain

import ssafy.autonomous.pathfinder.domain.facility.domain.Facility
import javax.persistence.*

@Entity
class Customer(

    private var currentLocationFacility: String?,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    val id:Int = 0
){

    // 캡슐화 지키기 위해 변수에 직접 접근 불가
    fun updateCurrentLocationFacility(facilityName: String){
        currentLocationFacility = facilityName
    }

    fun getCurrentLocationFacility(): String?{
        return currentLocationFacility
    }
}