package ssafy.autonomous.pathfinder.domain.auth.handler

import mu.KotlinLogging
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import ssafy.autonomous.pathfinder.domain.auth.repository.HttpCookieOAuth2AuthorizationRequestRepository
import ssafy.autonomous.pathfinder.global.util.CookieUtils
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OAuth2AuthenticationFailureHandler(
    private val cookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository
) : SimpleUrlAuthenticationFailureHandler() {

    private val logger = KotlinLogging.logger{}

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        var targetUrl = CookieUtils.getCookie(
            request = request,
            name = cookieOAuth2AuthorizationRequestRepository.redirectUriParamCookieName
        )?.value ?: "/"

        logger.error("exception message {}" + exception.message)

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl).build().toUriString()

        cookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)

        redirectStrategy.sendRedirect(request, response, targetUrl)
    }
}