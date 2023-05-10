package ssafy.autonomous.pathfinder.domain.facility.domain

import ssafy.autonomous.pathfinder.domain.occupancy.domain.FloatingPopularity
import javax.persistence.*

// 시설 (층, 화장실, 방)
@Entity
class Facility(

        private val facilityName: String, // 시설 이름

//        @Enumerated(EnumType.STRING)
//        val facilityType: FacilityType, // 시설 타입
        private val facilityType: String,
        private var densityMax: Int, // 최대 밀집도
        private var hitCount: Int, // 시설 조회 횟수

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "facility_id")
        val id: Int,

        // 층과 시설 1 : N
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "floors_id")
        private val floors: Floors? = null,


        // 시설과 각 방의 기능 1 : N
        @OneToMany(mappedBy = "facility")
        private var floatingPopularity: List<FloatingPopularity> = mutableListOf()

){
        fun plusHitCount(){
                this.hitCount += 1
        }

        fun getFacilityType(): String{
                return this.facilityType
        }


        fun getFacilityName(): String{
                return this.facilityName
        }

        fun getHisCount(): Int{
                return this.hitCount
        }
}