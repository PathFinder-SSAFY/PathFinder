package ssafy.autonomous.pathfinder.global.common.response

import org.springframework.http.HttpStatus
import ssafy.autonomous.pathfinder.global.common.ResultType


data class ApiResponse(
    val result: ResultType = ResultType.SUCCESS,
    val statusCode: Int = HttpStatus.OK.value(),
    val message: String = "성공했습니다.",
    val data: Any?
)