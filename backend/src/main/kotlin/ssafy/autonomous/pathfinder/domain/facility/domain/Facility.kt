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

        // 시설 사각형 좌표 (UpX, UpY), (DownX, UpY), (UpX, DownY), (DownX, DownY)
        private val facilityLeftUpX: Double ?= 0.0,
        private val facilityLeftUpY: Double ?= 0.0,
        private val facilityRightDownX: Double ?= 0.0,
        private val facilityRightDownY: Double ?= 0.0,




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
        private var floatingPopularity: List<FloatingPopularity> = mutableListOf(),

        // 방 하나에는 여러개의 입구가 있을 수 있다.
        @OneToMany(mappedBy = "facility")
        private val entrance: List<RoomEntrance> = mutableListOf()

){
        fun plusHitCount(){
                this.hitCount += 1
        }

        fun getFacilityType(): String{
                return this.facilityType
        }

        fun getFacilityLeftUpXY(): List<Double?> {
                return listOf(facilityLeftUpX, facilityLeftUpY)
        }

        fun getFacilityRightDownXY(): List<Double?> {
                return listOf(facilityRightDownX, facilityRightDownY)
        }


        fun getFacilityName(): String{
                return this.facilityName
        }

        fun getHisCount(): Int {
                return this.hitCount
        }

        fun getEntrance(): List<RoomEntrance>{
                return this.entrance
        }
}