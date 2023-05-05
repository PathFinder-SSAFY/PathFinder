package ssafy.autonomous.pathfinder.domain.auth.exception

import org.springframework.http.HttpStatus
import ssafy.autonomous.pathfinder.global.common.ErrorCode

enum class AuthErrorCode(override val status: HttpStatus, override var message: String): ErrorCode {

    // 인증 결과, 관리자를 찾지 못했을 경우
    AUTH_NOT_FOUND(HttpStatus.NOT_FOUND, "관리자를 찾지 못했습니다."),

    // OAuth2 로그인할 수 없는 상태, 인증 오류
    OAUTH2_AUTHENTICATION_PROCESSING_EXCEPTION(HttpStatus.UNAUTHORIZED, "OAuth2 인증 오류가 발생했습니다."),

    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다. 클라이언트는 새로운 토큰을 요청하거나 다시 로그인을 시도해야 합니다.")
}