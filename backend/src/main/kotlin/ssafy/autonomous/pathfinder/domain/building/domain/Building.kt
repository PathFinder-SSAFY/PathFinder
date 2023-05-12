package ssafy.autonomous.pathfinder.domain.building.domain

import ssafy.autonomous.pathfinder.domain.administrator.domain.Administrator
import ssafy.autonomous.pathfinder.domain.facility.domain.Floors
import javax.persistence.*

@Entity
class Building(


    val buildingName: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "building_id")
    val id: Int,

    // 총 관리하는 곳와 빌딩은 1 : N
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "properties_id")
    var properties: Properties? = null,

    // 빌딩과 층 : 1 : N 다대일 관계
    @OneToMany(mappedBy = "building")
    var floors: List<Floors> = mutableListOf(),

    // administrator와 1:1 관계
    @OneToOne
    @JoinColumn(name = "administrator_id")
    var administrator: Administrator? = null

)