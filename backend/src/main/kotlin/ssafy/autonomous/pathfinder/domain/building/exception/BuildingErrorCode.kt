package ssafy.autonomous.pathfinder.domain.building.exception

import org.springframework.http.HttpStatus
import ssafy.autonomous.pathfinder.global.common.ErrorCode

enum class BuildingErrorCode(override val status: HttpStatus, override var message: String): ErrorCode {

    ID_NOT_FOUND(HttpStatus.BAD_REQUEST, "ID가 조회되지 않습니다.")
}