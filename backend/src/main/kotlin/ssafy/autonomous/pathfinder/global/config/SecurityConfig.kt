package ssafy.autonomous.pathfinder.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import ssafy.autonomous.pathfinder.domain.auth.filter.TokenAuthenticationFilter
import ssafy.autonomous.pathfinder.domain.auth.handler.OAuth2AuthenticationFailureHandler
import ssafy.autonomous.pathfinder.domain.auth.handler.OAuth2AuthenticationSuccessHandler
import ssafy.autonomous.pathfinder.domain.auth.repository.HttpCookieOAuth2AuthorizationRequestRepository
import ssafy.autonomous.pathfinder.domain.auth.service.CustomOAuth2UserService
import ssafy.autonomous.pathfinder.domain.auth.service.CustomUserDetailsService

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val tokenAuthenticationFilter: TokenAuthenticationFilter,
    private val customUserDetailsService: CustomUserDetailsService,
    private val customOAuth2UserService: CustomOAuth2UserService, // 성공, 실패, Cookie 처리까지
    private val oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler,
    private val oAuth2AuthenticationFailureHandler: OAuth2AuthenticationFailureHandler,
    private val cookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository

) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        // /admin/** : admin 경로 접근할 때, 로그인한 사용자만 접근 가능하다. (이외 주소는 경로 접근할 때 로그인 없이 접근가능)
        // CSRF 설정
        http.authorizeHttpRequests()
            .antMatchers("/admin/**").authenticated()
            .anyRequest().permitAll()

        http
            .oauth2Login()
            .authorizationEndpoint()
            .authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository)
            .and()
            .userInfoEndpoint().userService(customOAuth2UserService)
            .and()
            .successHandler(oAuth2AuthenticationSuccessHandler)
            .failureHandler(oAuth2AuthenticationFailureHandler)
        http.addFilterBefore(
            tokenAuthenticationFilter,
            UsernamePasswordAuthenticationFilter::class.java
        )

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }


    fun authenticationManager(auth : AuthenticationManagerBuilder){
        auth.userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder())
    }



}