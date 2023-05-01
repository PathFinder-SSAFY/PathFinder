//package ssafy.autonomous.pathfinder.global.config
//
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import ssafy.autonomous.pathfinder.domain.administrator.application.handler.OAuth2AuthenticationSuccessHandler
//import ssafy.autonomous.pathfinder.domain.administrator.service.AdministratorOAuth2UserServiceImpl
//
//@Configuration
//class SecurityConfig(
//        private val oAuth2UserService: AdministratorOAuth2UserServiceImpl,
//        private val authenticationSuccessHandler: OAuth2AuthenticationSuccessHandler,
//) {
//
//    @Bean
//    fun filterChain(http: HttpSecurity) {
//        // /admin/** : admin 경로 접근할 때, 로그인한 사용자만 접근 가능하다. (이외 주소는 경로 접근할 때 로그인 없이 접근가능)
//        // CSRF 설정
//        http.authorizeHttpRequests()
//                .antMatchers("/admin/**").authenticated()
//                .anyRequest().permitAll()
//                .and()
//                .oauth2Login()
//                .userInfoEndpoint().userService(oAuth2UserService)
//                .and()
//                .successHandler(authenticationSuccessHandler)
////                .failureHandler(authenticationoAuth2FailureHandler)
////        http.addFilterBefore(token)
//    }
//
//
//
//}