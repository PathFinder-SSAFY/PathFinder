package ssafy.autonomous.pathfinder.domain.administrator.domain

import ssafy.autonomous.pathfinder.domain.building.Building
import javax.persistence.*
import javax.validation.constraints.Email

@Entity
class Administrator(


    @Email
    var email: String,

    var naverId: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    var authority: Authority,


    // 데이터베이스에 새로운 엔티티가 저장되면 JPA가 자동으로 식별자 값을 할당하게 된다. (그러므로 기본 값을 0으로 설정해도 된다.)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "administrator_id")
    val id: Int = 0,

    @OneToOne(mappedBy = "Administrator")
    var building: Building? = null

)



