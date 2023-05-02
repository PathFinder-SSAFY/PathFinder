package ssafy.autonomous.pathfinder.domain.auth.filter

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import ssafy.autonomous.pathfinder.domain.auth.AuthToken
import ssafy.autonomous.pathfinder.domain.auth.AuthTokenProvider
import ssafy.autonomous.pathfinder.domain.auth.security.JwtTokenProvider
import ssafy.autonomous.pathfinder.global.util.HeaderUtil
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class TokenAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {
    private val tokenProvider: AuthTokenProvider? = null


    override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            filterChain: FilterChain) {
        val tokenStr: String? = HeaderUtil().getAccessToken(request)
        val token: AuthToken? = tokenProvider?.convertAuthToken(tokenStr)
        if (token!!.validate()) {
            val authentication = tokenProvider!!.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }
}