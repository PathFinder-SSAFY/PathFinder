package ssafy.autonomous.pathfinder.global.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import ssafy.autonomous.pathfinder.domain.auth.exception.TokenExpiredException
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {

    /*
        인증되지 않은 사용자가 보호된 EntryPoint에 접근했을 경우, 실행됨
    */
    @Throws(IOException::class)
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        response?.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        response?.contentType = MediaType.APPLICATION_JSON_VALUE
        response?.characterEncoding = "UTF-8"
        val objectMapper = ObjectMapper()
        objectMapper.writeValue(response?.outputStream, TokenExpiredException())
    }
}