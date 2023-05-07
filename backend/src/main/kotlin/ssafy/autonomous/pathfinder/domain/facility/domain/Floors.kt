package ssafy.autonomous.pathfinder.domain.facility.domain

import ssafy.autonomous.pathfinder.domain.building.Building
import javax.persistence.*

@Entity
class Floors(

    val floorNumber: String,
    val mapImageUrl: String,

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "floors_id")
        val id: Int,

        // 빌딩과 층은 다대일 단방향 => N : 1
    @ManyToOne(fetch=FetchType.LAZY, targetEntity = Building::class)
        @JoinColumn(name = "building_id")
        var building: Building? = null,

        // 층과 시설 : 1 : N
    @OneToMany(mappedBy = "floors")
        var facilities: List<Facility> = mutableListOf(),

        // 층과 방 시설 : 1 : N
        // mappedBy : 연관관계 주인에서 사용된 변수 (현재 RoomMaps의 floors가 외래 키 사용한 변수)
        // @ManyToOne(fetch = FetchType.LAZY)
        // @JoinColumn(name = "floors_id")
        // val floors: Floors
    @OneToMany(mappedBy = "floors")
        var roomMaps: List<RoomMap> = mutableListOf(),


        // Weight과 일대일
    @OneToOne(mappedBy = "roomMap")
        var weight: Weight ?= null
)