package ssafy.autonomous.passfinder.facility.domain

import ssafy.autonomous.passfinder.occupancy.domain.FloatingPopularity
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

        // 층과 시설 1 : N
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "floors_id")
        val floors: Floors? = null,


        // 시설과 각 방의 기능 1 : N
        @OneToMany(mappedBy = "facility")
        var floatingPopularity: List<FloatingPopularity> = mutableListOf()

)