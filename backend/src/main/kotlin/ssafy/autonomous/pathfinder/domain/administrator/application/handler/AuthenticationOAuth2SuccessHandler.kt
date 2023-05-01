//package ssafy.autonomous.pathfinder.domain.administrator.application.handler
//
//
//import org.springframework.security.core.Authentication
//import org.springframework.security.oauth2.core.user.OAuth2User
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
//import org.springframework.stereotype.Component
//import ssafy.autonomous.pathfinder.Building.domain.AdministratorOAuth2User
//import ssafy.autonomous.pathfinder.domain.administrator.service.AdministratorOAuth2UserServiceImpl
//import javax.servlet.http.HttpServletRequest
//import javax.servlet.http.HttpServletResponse
//
//@Component
//class OAuth2AuthenticationSuccessHandler(
//        private var administratorService : AdministratorOAuth2UserServiceImpl,
//) : SimpleUrlAuthenticationSuccessHandler() {
//
//    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse,
//                                         authentication: Authentication) {
//        // 로그인한 사용자 정보 가져오기
//        val oauth2User = authentication.principal as OAuth2User
//
//        // oauth2User로 부터 email을 가져온다.
//        val email = oauth2User.attributes["email"] as String
//
//        // 관리자 계정에 사용자 정보 저장
//        val administrator = AdministratorOAuth2User(email)
//        administratorService.saveAdministrator(administrator)
//
//        // JWT 토큰을 생성한다.
////        val token = jwtTokenProvider.createToken(email)
//
//        // JWT 토큰을 HTTP 응답에 추가한다.
//        response.contentType = "application/json"
//        response.characterEncoding = "UTF-8"
//        response.writer.write("{\"token\": \"$token\"}")
//    }
//}
