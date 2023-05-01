package ssafy.autonomous.pathfinder.global.common.error

import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import ssafy.autonomous.pathfinder.global.common.CustomException
import ssafy.autonomous.pathfinder.global.common.error.dto.ErrorResponse

@RestControllerAdvice
class GlobalExceptionHandler {


    // KotlinLogging : 주석처리
    private val logger = KotlinLogging.logger {}

    // (1) 사용자 정의 예외처리
    @ExceptionHandler(value = [CustomException::class])
    protected fun handleCustomException(e: CustomException): ResponseEntity<ErrorResponse?>?{
        return ErrorResponse.toResponseEntity(e.getErrorCode())
    }

    // (2) 검증 예외처리 (사용자 입력 실수 등)
    @ExceptionHandler(value = [BindException::class])
    protected fun handleBindException(e: BindException): ResponseEntity<ErrorResponse?>? {
        return ErrorResponse.toResponseEntity(e.bindingResult)
    }
}