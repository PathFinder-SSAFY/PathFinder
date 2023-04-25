package ssafy.autonomous.passfinder.administrator.domain

import javax.persistence.*

@Entity
class Properties(

        val field: Int,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name ="properties_id")
        val id: Long ?= null,

        @OneToMany(mappedBy="")
)

// 참고 : https://www.notion.so/2-Kotlin-JPA-3-a3cbd536a1a343ed8d85b3f16164f30b