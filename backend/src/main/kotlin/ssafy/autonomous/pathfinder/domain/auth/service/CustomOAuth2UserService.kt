package ssafy.autonomous.pathfinder.domain.auth.service

import mu.KotlinLogging
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import ssafy.autonomous.pathfinder.domain.administrator.domain.AdministratorOAuth2User
import ssafy.autonomous.pathfinder.domain.administrator.domain.Role
import ssafy.autonomous.pathfinder.domain.administrator.repository.AdministratorOAuth2UserRepository
import ssafy.autonomous.pathfinder.domain.auth.domain.AdministratorPrincipal
import ssafy.autonomous.pathfinder.domain.auth.domain.oauth.AuthProvider
import ssafy.autonomous.pathfinder.domain.auth.domain.oauth.OAuth2UserInfo
import ssafy.autonomous.pathfinder.domain.auth.domain.oauth.OAuth2UserInfoFactory
import ssafy.autonomous.pathfinder.domain.auth.exception.OAuth2AuthenticationProcessingException
import java.lang.Exception
import javax.security.sasl.AuthenticationException

@Service
class CustomOAuth2UserService(
    private val administratorOAuth2UserRepository: AdministratorOAuth2UserRepository
) : DefaultOAuth2UserService() {

    private val logger = KotlinLogging.logger {}

    override fun loadUser(oAuth2UserRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(oAuth2UserRequest)
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User)
        } catch (e: AuthenticationException){
            throw e
        } catch (e: Exception){
            throw InternalAuthenticationServiceException(e.message, e.cause)
        }
    }


    private fun processOAuth2User(oAuth2UserRequest: OAuth2UserRequest, oAuth2User: OAuth2User): OAuth2User {
        val oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
            oAuth2UserRequest.clientRegistration.registrationId,
            oAuth2User.attributes
        )

        if (oAuth2UserInfo.getEmail().isBlank()) {
            throw OAuth2AuthenticationProcessingException("OAuath2로부터 Email을 받아오지 못했습니다.")
        }

        var administrator = administratorOAuth2UserRepository.findByEmail(oAuth2UserInfo.getEmail())
        if (administrator != null) {
            if (administrator.authProvider != AuthProvider.of(oAuth2UserRequest.clientRegistration.registrationId)) {
                throw OAuth2AuthenticationProcessingException("${administrator.authProvider} 계정이 존재합니다.")
            }
            administrator = updateExistingAdministrator(administrator, oAuth2UserInfo)
        } else {
            administrator = registerNewAdministrator(oAuth2UserRequest, oAuth2UserInfo)
        }

        return AdministratorPrincipal.of(administrator, oAuth2User.attributes)
    }

    private fun updateExistingAdministrator(
        existingAdministrator: AdministratorOAuth2User,
        oAuth2UserInfo: OAuth2UserInfo
    ): AdministratorOAuth2User {
        existingAdministrator.email = oAuth2UserInfo.getEmail()
        return existingAdministrator
    }

    private fun registerNewAdministrator(
        oAuth2UserRequest: OAuth2UserRequest,
        oAuth2UserInfo: OAuth2UserInfo
    ): AdministratorOAuth2User {
        val administrator = AdministratorOAuth2User(
            email = oAuth2UserInfo.getEmail(),
            password = oAuth2UserInfo.getOAuthId(),
            role = Role.ROLE_USER,
            authProvider = AuthProvider.of(oAuth2UserRequest.clientRegistration.registrationId)
        )
        return administratorOAuth2UserRepository.save(administrator)
    }
}