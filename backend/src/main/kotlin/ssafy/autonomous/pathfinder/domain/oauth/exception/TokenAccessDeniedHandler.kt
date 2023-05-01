package ssafy.autonomous.pathfinder.domain.oauth.exception

import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.web.servlet.HandlerExceptionResolver
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class TokenAccessDeniedHandler(
        private val handlerExceptionResolver: HandlerExceptionResolver? = null
) : AccessDeniedHandler {

    override fun handle(request: HttpServletRequest?, response: HttpServletResponse?, accessDeniedException: AccessDeniedException?) {
        handlerExceptionResolver?.resolveException(request!!, response!!, null, accessDeniedException!!);
    }

}