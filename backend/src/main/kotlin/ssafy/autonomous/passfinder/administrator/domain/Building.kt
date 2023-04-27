package ssafy.autonomous.passfinder.administrator.domain

import ssafy.autonomous.passfinder.facility.domain.Floors
import javax.persistence.*

@Entity
class Building(


        val buildingName: String,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name="building_id")
        val id : Int,

        // 총 관리하는 곳와 빌딩은 1 : N
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "properties_id")
        var properties: Properties? = null,

        // 빌딩과 층 : 1 : N 다대일 관계
        @OneToMany(mappedBy = "building")
        var floors: List<Floors> = mutableListOf(),

        // manager와 1:1 관계
        @OneToOne
        @JoinColumn(name = "manager_id")
        var manager: Manager ?= null

)