package ssafy.autonomous.pathfinder.domain.auth.filter

import mu.KotlinLogging
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ssafy.autonomous.pathfinder.domain.auth.security.JwtTokenProvider
import ssafy.autonomous.pathfinder.global.util.HeaderUtil
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class TokenAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
) : OncePerRequestFilter() {

    val logger = KotlinLogging.logger {}

    // Android에서 전달한 토큰, 유효성 검사
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val jwt: String? = HeaderUtil().getAccessToken(request)
            if (!jwt.isNullOrBlank() && jwtTokenProvider.validateToken(jwt)) {
                val authentication: Authentication = jwtTokenProvider.getAuthentication(jwt)
                SecurityContextHolder.getContext().authentication = authentication

                /*
                  SecurityContextHolder.getContext().authentication
                  - 현재 인증된 사용자의 인증 정보(Authentication 객체)를 SecurityContextHolder 객체에 저장하는 코드
                  - 이렇게 저장된 인증 정보는 다른 곳에서 필요할 때 언제든지 사용할 수 있다.
                */
            }
        } catch (e: Exception) {
            logger.error("security에 사용자 인증 정보를 넣을 수 없습니다. " + e.message)
        }
        filterChain.doFilter(request, response)

    }

}