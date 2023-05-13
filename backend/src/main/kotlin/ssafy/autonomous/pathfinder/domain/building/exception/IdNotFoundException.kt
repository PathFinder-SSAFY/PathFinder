package ssafy.autonomous.pathfinder.domain.building.exception

import ssafy.autonomous.pathfinder.global.common.CustomException
import ssafy.autonomous.pathfinder.global.common.ErrorCode

class IdNotFoundException private constructor(errorCode: ErrorCode) : CustomException(errorCode) {

    companion object{
        val CODE = BuildingErrorCode.ID_NOT_FOUND
    }

    constructor(): this(CODE)
}