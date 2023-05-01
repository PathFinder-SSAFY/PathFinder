package ssafy.autonomous.pathfinder.global.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ssafy.autonomous.pathfinder.domain.oauth.AuthTokenProvider


@Configuration
class JwtConfig {
    @Value("\${jwt.secret}")
    private val secret: String? = null
    @Bean
    fun jwtProvider(): AuthTokenProvider {
        return AuthTokenProvider(secret!!)
    }
}
