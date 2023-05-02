//package ssafy.autonomous.pathfinder.domain.administrator.domain
//
//import com.fasterxml.jackson.annotation.JsonIgnore
//import javax.persistence.*
//import javax.validation.constraints.NotNull
//import javax.validation.constraints.Size
//
//@Entity
//class UserRefreshToken(
//        @field:Size(max = 64) @field:NotNull @field:Column(name = "USER_ID", length = 64, unique = true) @param:NotNull @param:Size(max = 64) private val userId: String,
//        @field:Size(max = 256) @field:NotNull @field:Column(name = "REFRESH_TOKEN", length = 256) @param:NotNull @param:Size(max = 256) private val refreshToken: String
//) {
//    @JsonIgnore
//    @Id
//    @Column(name = "REFRESH_TOKEN_SEQ")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private val refreshTokenSeq: Long? = null
//}