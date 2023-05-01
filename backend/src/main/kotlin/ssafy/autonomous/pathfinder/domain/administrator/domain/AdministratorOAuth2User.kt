package ssafy.autonomous.pathfinder.Building.domain

import ssafy.autonomous.pathfinder.domain.Building.domain.Building
import javax.persistence.*

@Entity
class AdministratorOAuth2User(


        val email:String,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "administrator_id")
        val id: Long ?= null,


        @OneToOne(mappedBy = "administratorOAuth2User")
        var building: Building?= null

)