//package ssafy.autonomous.pathfinder.domain.auth.dto.request
//
//import ssafy.autonomous.pathfinder.domain.Building.domain.Building
//import ssafy.autonomous.pathfinder.domain.administrator.domain.AdministratorOAuth2User
//import ssafy.autonomous.pathfinder.domain.administrator.domain.Role
//import ssafy.autonomous.pathfinder.domain.auth.domain.oauth.AuthProvider
//import javax.persistence.*
//import javax.validation.constraints.Email
//import javax.validation.constraints.NotBlank
//
//data class SignUpRequest(
//
//    @NotBlank
//    @Email
//    var email:String,
//    var password: String,
//
//    @NotBlank
//    var role: String = Role.ROLE_USER.name, // default 관리자
//
//
//    var authProvider: String = AuthProvider.NAVER.name
//) {
//
//    fun toEntity(encodePw: String): AdministratorOAuth2User{
//        return AdministratorOAuth2User(
//            email = email,
//            password = encodePw,
//            role = Role.valueOf(role),
//            authProvider = AuthProvider.of(authProvider)
//        )
//    }
//}