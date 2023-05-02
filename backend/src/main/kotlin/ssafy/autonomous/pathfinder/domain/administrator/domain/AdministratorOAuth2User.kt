package ssafy.autonomous.pathfinder.domain.administrator.domain

import ssafy.autonomous.pathfinder.domain.Building.domain.Building
import ssafy.autonomous.pathfinder.domain.administrator.domain.Role
import javax.persistence.*
import javax.validation.constraints.Email

@Entity
class AdministratorOAuth2User(


        @Email
        val email:String,
        val password: String,


        @Enumerated(EnumType.STRING)
        val role: Role?= Role.ROLE_USER, // default 관리자

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "administrator_id")
        val id: Long ?= null,


        @OneToOne(mappedBy = "administratorOAuth2User")
        var building: Building?= null

)