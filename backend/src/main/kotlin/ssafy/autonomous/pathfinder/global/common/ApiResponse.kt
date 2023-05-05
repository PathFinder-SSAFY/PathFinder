package ssafy.autonomous.pathfinder.global.common

import org.springframework.http.HttpStatus

data class ApiResponse(
    val result: ResultType = ResultType.SUCCESS,
    val statusCode: Int = HttpStatus.OK.value(),
    val data: Any?
)