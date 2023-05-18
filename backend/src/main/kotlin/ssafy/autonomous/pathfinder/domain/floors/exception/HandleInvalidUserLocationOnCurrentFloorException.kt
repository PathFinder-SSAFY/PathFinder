package ssafy.autonomous.pathfinder.domain.floors.exception

import ssafy.autonomous.pathfinder.global.common.CustomException
import ssafy.autonomous.pathfinder.global.common.ErrorCode

class HandleInvalidUserLocationOnCurrentFloorException private constructor(errorCode: ErrorCode) : CustomException(errorCode) {

    companion object{
        val CODE = FloorsErrorCode.OUT_OF_BOUNDS_COORDINATES_ON_CURRENT_FLOOR
    }

    constructor(): this(CODE)
}