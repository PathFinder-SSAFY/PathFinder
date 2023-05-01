package ssafy.autonomous.pathfinder.domain.administrator.service

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.DefaultOAuth2User

import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import ssafy.autonomous.pathfinder.Building.domain.AdministratorOAuth2User
import ssafy.autonomous.pathfinder.domain.administrator.repository.AdministratorOAuth2UserRepository
import javax.servlet.http.HttpSession


@Service
class AdministratorOAuth2UserServiceImpl : OAuth2UserService<OAuth2UserRequest, OAuth2User>, AdministratorOAuth2UserService {
    private val administratorRepository: AdministratorOAuth2UserRepository? = null
    private val httpSession: HttpSession? = null


    @Override
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
//        val delegate: OAuth2UserService<*, *> = DefaultOAuth2UserService()
//        val oAuth2User = delegate.loadUser(userRequest as Nothing?)
//        val registrationId = userRequest.clientRegistration.registrationId
//        val userNameAttributeName = userRequest.clientRegistration.providerDetails
//            .userInfoEndpoint.userNameAttributeName
//        val attributes: OAuthAttributes =
//            OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.attributes)
//        val user: User = saveOrUpdate(attributes)
//        httpSession!!.setAttribute("user", SessionUser(user))
//        return DefaultOAuth2User(
//            setOf(SimpleGrantedAuthority(user.getRoleKey())),
//            attributes.getAttributes(),
//            attributes.getNameAttributeKey()
//        )
    }

    // 새로운 관리자 저장
    override fun saveAdministrator(administratorOAuth2User: AdministratorOAuth2User){

        // user 정보 조회
        val existingAdministrator  = administratorRepository?.findByAdministratorOAuth2UserEmail(administratorOAuth2User.email)?.orElseThrow { Exception() }

        if (existingAdministrator != null) {
            // 이미 등록된 사용자인 경우, 사용자의 정보 업데이트
            existingAdministrator.building = administratorOAuth2User.building
            administratorRepository?.save(existingAdministrator)
        } else {
            // 새로운 사용자인 경우, 사용자를 등록한다.
            administratorRepository?.save(administratorOAuth2User)
        }
    }

//    private fun saveOrUpdate(attributes: OAuthAttributes): User {
//        val user: User = administratorRepository.findByAdministratorOAuth2UserEmail(attributes.getEmail())
//            .map { entity -> entity.update(attributes.getName(), attributes.getPicture()) }
//            .orElse(attributes.toEntity())
//        return administratorRepository?.save(user)
//    }
}
