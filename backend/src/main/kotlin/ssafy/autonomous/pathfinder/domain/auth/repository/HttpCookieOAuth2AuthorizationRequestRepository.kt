//package ssafy.autonomous.pathfinder.domain.auth.repository
//
//import mu.KotlinLogging
//import org.slf4j.LoggerFactory
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
//import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
//import ssafy.autonomous.pathfinder.global.util.CookieUtils
//import javax.servlet.http.HttpServletRequest
//import javax.servlet.http.HttpServletResponse
//
//@Configuration
//class HttpCookieOAuth2AuthorizationRequestRepository
//    : AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
//
//    val oauth2AuthorizationRequestCookieName = "oauth2_auth_request"
//    val redirectUriParamCookieName = "redirect_uri"
//    val cookieExpireSeconds = 180
//
//    private val logger = KotlinLogging.logger{}
//
//    override fun loadAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest {
//        return CookieUtils.getCookie(request, oauth2AuthorizationRequestCookieName)
//                .let { CookieUtils.deserialize(it!!, OAuth2AuthorizationRequest::class.java)}
//    }
//    override fun saveAuthorizationRequest(
//            authorizationRequest: OAuth2AuthorizationRequest?,
//            request: HttpServletRequest,
//            response: HttpServletResponse
//    ) {
//        if(authorizationRequest == null){
//            CookieUtils.deleteCookie(request, response, oauth2AuthorizationRequestCookieName)
//            CookieUtils.deleteCookie(request,response, redirectUriParamCookieName)
//            return;
//        }
//
//        CookieUtils.addCookie(response, oauth2AuthorizationRequestCookieName,
//                CookieUtils.serialize(authorizationRequest), cookieExpireSeconds)
//
//        request.getParameter(redirectUriParamCookieName)?.let{
//            CookieUtils.addCookie(response, redirectUriParamCookieName, it, cookieExpireSeconds)
//        }
//
//    }
//
//    override fun removeAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest {
//        return loadAuthorizationRequest(request)
//    }
//
//    fun removeAuthorizationRequestCookies(request: HttpServletRequest, response: HttpServletResponse){
//        CookieUtils.deleteCookie(request, response, oauth2AuthorizationRequestCookieName)
//        CookieUtils.deleteCookie(request, response, redirectUriParamCookieName)
//    }
//
//}