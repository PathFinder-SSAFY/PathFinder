package ssafy.autonomous.pathfinder.domain.oauth.filter

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import ssafy.autonomous.pathfinder.domain.oauth.AuthToken
import ssafy.autonomous.pathfinder.domain.oauth.AuthTokenProvider
import ssafy.autonomous.pathfinder.global.util.HeaderUtil
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class TokenAuthenticationFilter : OncePerRequestFilter() {
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