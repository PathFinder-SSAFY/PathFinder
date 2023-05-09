package ssafy.autonomous.pathfinder.global.common.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import ssafy.autonomous.pathfinder.global.common.ErrorCode
import java.time.LocalDateTime

data class ErrorResponse(
    // 에러 발생시간, 상태, 메세지, 에러 로그
//    val timestamp: LocalDateTime = LocalDateTime.now(),
//    val status: Int,
    val data: String
) {

    companion object {

        fun toResponseEntity(errorCode: ErrorCode): ResponseEntity<ErrorResponse?> {
            return ResponseEntity.status(errorCode.status).body(ErrorResponse(errorCode))
        }

        fun toResponseEntity(result: BindingResult?): ResponseEntity<ErrorResponse?> {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result?.let { ErrorResponse(it) })
        }
    }

    // 생성자
    constructor(errorCode: ErrorCode) : this(
//        status = errorCode.status.value(),
        data = errorCode.message,
    )

    constructor(result: BindingResult) : this(
//        status = HttpStatus.BAD_REQUEST.value(),
        data = result.fieldErrors.map { error -> error.defaultMessage}.joinToString { ", " } // 에러를 map 형식으로 저장
    )
}