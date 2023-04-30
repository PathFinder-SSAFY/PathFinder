package ssafy.autonomous.pathfinder.administrator.domain

import javax.persistence.*

@Entity
class Properties(

        val field: Int,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name ="properties_id")
        val id: Long ?= null,

        // 다대일, 다 : Building, 일 : Properties
        // 연관관계 주인 Building
        // 일대다 관계
        @OneToMany(mappedBy = "properties")
        val buildings: List<Building> = mutableListOf()

)

// 참고 : https://www.notion.so/2-Kotlin-JPA-3-a3cbd536a1a343ed8d85b3f16164f30b