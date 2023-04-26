package ssafy.autonomous.passfinder.common.exception

import mu.KotlinLogging
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {


    // KotlinLogging : 주석처리
    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(value = )
}