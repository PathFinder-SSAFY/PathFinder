package ssafy.autonomous.pathfinder.domain.oauth.handler

import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerExceptionResolver
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class TokenAccessDeniedHandler : AccessDeniedHandler {
    private val handlerExceptionResolver: HandlerExceptionResolver? = null

    override fun handle(request: HttpServletRequest?, response: HttpServletResponse?, accessDeniedException: org.springframework.security.access.AccessDeniedException?) {
        handlerExceptionResolver!!.resolveException(request!!, response!!, null, accessDeniedException!!)
    }
}