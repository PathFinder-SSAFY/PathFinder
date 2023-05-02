package ssafy.autonomous.pathfinder.domain.administrator.domain

import ssafy.autonomous.pathfinder.domain.Building.domain.Building
import ssafy.autonomous.pathfinder.domain.administrator.domain.Role
import ssafy.autonomous.pathfinder.domain.auth.domain.oauth.AuthProvider
import javax.persistence.*
import javax.validation.constraints.Email

@Entity
class AdministratorOAuth2User(


        @Email
        var email:String,
        var password: String,


        @Enumerated(EnumType.STRING)
        @Column(name = "role")
        var role: Role = Role.ROLE_USER, // default 관리자

        @Enumerated(EnumType.STRING)
        @Column(name = "auth_provider")
        var authProvider: AuthProvider,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "administrator_id")
        var id: Long ?= null,


        @OneToOne(mappedBy = "administratorOAuth2User")
        var building: Building?= null

)