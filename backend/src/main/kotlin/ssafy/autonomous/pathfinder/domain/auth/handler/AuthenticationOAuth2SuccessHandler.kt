package ssafy.autonomous.pathfinder.domain.auth.handler


import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import ssafy.autonomous.pathfinder.domain.administrator.domain.AdministratorOAuth2User
import ssafy.autonomous.pathfinder.domain.auth.repository.HttpCookieOAuth2AuthorizationRequestRepository
import ssafy.autonomous.pathfinder.domain.auth.security.JwtTokenProvider
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OAuth2AuthenticationSuccessHandler(
        private val tokenProvider: JwtTokenProvider,
        private val cookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository,
        @Value("\${security.authorized-redirect-uri:}")
        private val authorizedRedirectUri: String

) : SimpleUrlAuthenticationSuccessHandler() {


    private val logger = KotlinLogging.logger{}
    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse,
                                         authentication: Authentication) {
        val targetUrl = determineTargetUrl(request, response, authentication)

        if(response.isCommitted){
            logger.debug("Response has already been committed. Unable to redirect to $targetUrl")
            return
        }

        clearAuthenticationAttributes(request, response)

    }

    fun clearAuthenticationAttributes(request: HttpServletRequest, response: HttpServletResponse){
        super.clearAuthenticationAttributes(request)
        cookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
    }

    override fun determineTargetUrl(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication?): String {
        return super.determineTargetUrl(request, response, authentication)
    }


}
