package ssafy.autonomous.pathfinder.Building.domain

import javax.persistence.*

@Entity
class AdministratorOAuth2User(


        val administratorOAuth2UserEmail:String,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "administrator_id")
        val id: Long ?= null,

        @OneToOne(mappedBy = "administratorOAuth2User")
        var building: Building
)