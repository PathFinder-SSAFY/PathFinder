package ssafy.autonomous.pathfinder.domain.floors.exception

import ssafy.autonomous.pathfinder.global.common.CustomException
import ssafy.autonomous.pathfinder.global.common.ErrorCode

class HandleInWallBoundaryException private constructor(errorCode: ErrorCode) : CustomException(errorCode) {

    companion object{
        val CODE = FloorsErrorCode.BLOCK_WALL_BOUNDARY_ARRIVAL
    }

    constructor(): this(CODE)
}