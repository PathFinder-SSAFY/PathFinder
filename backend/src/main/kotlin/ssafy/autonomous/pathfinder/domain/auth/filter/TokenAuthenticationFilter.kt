package ssafy.autonomous.pathfinder.domain.auth.filter

import mu.KotlinLogging
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ssafy.autonomous.pathfinder.domain.auth.security.JwtTokenProvider
import ssafy.autonomous.pathfinder.domain.auth.service.CustomUserDetailsService
import ssafy.autonomous.pathfinder.global.util.HeaderUtil
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class TokenAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val customUserDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {

    val logger = KotlinLogging.logger{}


    override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            filterChain: FilterChain) {
        try {
            val jwt : String?= HeaderUtil().getAccessToken(request)
            if(!jwt.isNullOrBlank() && jwtTokenProvider.validateToken(jwt)){
                val userId = jwtTokenProvider.getUserIdFromToken(jwt) // userId 발급
                val userDetails = customUserDetailsService.loadAdministratorById(userId)
                val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)

                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                /*
                WebAuthenticationDetailsSource()
                - Spring Security에서 제공하는 클래스로, 인증 요청(request)에 대한 세부 정보(WebAuthenticationDetails)를 생성하는 데 사용된다.
                - 이 세부 정보는 인증 요청이 발생한 클라이언트의 IP 주소, 사용된 브라우저 정보, 요청 시각 등의 정보를 포함할 수 있다.
                buildDetails() 메소드를 호출 할 시
                - 현재의 HTTP 요청(request)를 기반으로 WebAuthenticationDetails 객체가 생성된다.
                */
                SecurityContextHolder.getContext().authentication = authentication

                /*
                SecurityContextHolder.getContext().authentication
                - 현재 인증된 사용자의 인증 정보(Authentication 객체)를 SecurityContextHolder 객체에 저장하는 코드
                - 이렇게 저장된 인증 정보는 다른 곳에서 필요할 때 언제든지 사용할 수 있다.
                */
            }



        }catch (e : Exception){
            logger.error("security에 사용자 인증 정보를 넣을 수 없습니다.", e)
        }

        filterChain.doFilter(request, response)

    }
}