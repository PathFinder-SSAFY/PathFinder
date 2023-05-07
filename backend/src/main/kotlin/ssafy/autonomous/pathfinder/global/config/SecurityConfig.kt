package ssafy.autonomous.pathfinder.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import ssafy.autonomous.pathfinder.domain.auth.filter.TokenAuthenticationFilter
import ssafy.autonomous.pathfinder.domain.auth.security.JwtTokenProvider

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Throws(Exception::class)
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        // /admin/** : admin 경로 접근할 때, 로그인한 사용자만 접근 가능하다. (이외 주소는 경로 접근할 때 로그인 없이 접근가능)
        // CSRF 설정
        return http
            .httpBasic().disable()
                .csrf().disable()
                .cors()
            .and()
                .exceptionHandling()
                .authenticationEntryPoint(CustomAuthenticationEntryPoint())
            .and()
                .authorizeHttpRequests()
                .antMatchers("/admin/**").authenticated() // 인증된 사용자만 /admin/** 로 접근 가능
                .anyRequest().permitAll()
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 서버와 클라이언트 간의 요청-응답에서 세션을 사용하지 않겠다는 의미
            .and()
                .addFilterBefore(TokenAuthenticationFilter(jwtTokenProvider),
                    UsernamePasswordAuthenticationFilter::class.java)
                .build()
    }

}