//package ssafy.autonomous.pathfinder.domain.administrator.service
//
//import org.springframework.security.core.authority.SimpleGrantedAuthority
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User
//
//import org.springframework.stereotype.Service
//import ssafy.autonomous.pathfinder.domain.administrator.domain.Administrator
//import ssafy.autonomous.pathfinder.domain.administrator.repository.AdministratorRepository
//import javax.servlet.http.HttpSession
//import org.springframework.security.oauth2.core.user.OAuth2User as OAuth2User1
//
//
////@Service
//class AdministratorServiceImpl : OAuth2UserService<OAuth2UserRequest, OAuth2User1>, AdministratorService*/ {
//    private val administratorRepository: AdministratorRepository? = null
//    private val httpSession: HttpSession? = null
//
////    @Override
////    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User1 {
////        return null?
////    }
////
////    // 새로운 관리자 저장
////    override fun saveAdministrator(Administrator: Administrator){
////
////        // user 정보 조회
////        val existingAdministrator  = administratorRepository?.findByAdministrator(Administrator.email)?.orElseThrow { Exception() }
////
////        if (existingAdministrator != null) {
////            // 이미 등록된 사용자인 경우, 사용자의 정보 업데이트
////            existingAdministrator.building = Administrator.building
////            administratorRepository?.save(existingAdministrator)
////        } else {
////            // 새로운 사용자인 경우, 사용자를 등록한다.
////            administratorRepository?.save(Administrator)
////        }
////    }
//
////    private fun saveOrUpdate(attributes: OAuthAttributes): User {
////        val user: User = administratorRepository.findByAdministrator(attributes.getEmail())
////            .map { entity -> entity.update(attributes.getName(), attributes.getPicture()) }
////            .orElse(attributes.toEntity())
////        return administratorRepository?.save(user)
////    }
//}
