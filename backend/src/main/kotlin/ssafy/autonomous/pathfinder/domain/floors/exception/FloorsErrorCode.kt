package ssafy.autonomous.pathfinder.domain.floors.exception

import org.springframework.http.HttpStatus
import ssafy.autonomous.pathfinder.global.common.ErrorCode

enum class FloorsErrorCode(override val status : HttpStatus, override val message: String) : ErrorCode {

    BLOCK_WALL_BOUNDARY_ARRIVAL(HttpStatus.I_AM_A_TEAPOT, "현재 좌표는 벽의 범위에 포함되는 좌표입니다. 사용자 위치가 될 수 없습니다."),
    OUT_OF_BOUNDS_COORDINATES_ON_CURRENT_FLOOR(HttpStatus.UNPROCESSABLE_ENTITY, "층의 범위를 벗어난 좌표입니다. 사용자 위치가 될 수 없습니다.")

}