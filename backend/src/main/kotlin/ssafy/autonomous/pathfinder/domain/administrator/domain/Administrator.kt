package ssafy.autonomous.pathfinder.domain.administrator.domain

import ssafy.autonomous.pathfinder.domain.building.Building
import ssafy.autonomous.pathfinder.domain.auth.domain.AuthProvider
import ssafy.autonomous.pathfinder.domain.auth.dto.response.TokenResponseDto
import javax.persistence.*
import javax.validation.constraints.Email

@Entity
class Administrator(


    @Email
    var email: String,

    var naverId: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "administrator_id")
    var id: Int,


    @OneToOne(mappedBy = "Administrator")
    var building: Building? = null

)
//{
//    data class Builder(
//        var email: String = "",
//        var naverId: String = ""
//    ) {
//        fun getEmail(email: String) = apply { this.email = email }
//        fun getNaverId(naverId: String) = apply { this.naverId = naverId }
//    }
//}



