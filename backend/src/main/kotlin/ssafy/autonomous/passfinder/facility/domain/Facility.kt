package ssafy.autonomous.passfinder.facility.domain

import javax.persistence.*

// 시설 (층, 화장실, 방)
@Entity
class Facility(

        val facilityName: String, // 시설 이름

        @Enumerated(EnumType.STRING)
        val facilityType: FacilityType, // 시설 타입
        val densityMax: Int, // 최대 밀집도

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "facility_id")
        val id: Long? = null,
) {

}