package ssafy.autonomous.pathfinder.global.common

import org.springframework.http.HttpStatus

interface ErrorCode {
    val status: HttpStatus
    val message: String
}