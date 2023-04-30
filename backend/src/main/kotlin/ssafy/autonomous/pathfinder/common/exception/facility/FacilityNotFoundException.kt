package ssafy.autonomous.pathfinder.common.exception.facility

import ssafy.autonomous.pathfinder.common.CustomException
import ssafy.autonomous.pathfinder.common.ErrorCode

class FacilityNotFoundException private constructor(errorCode: ErrorCode) : CustomException(errorCode) {

    companion object{
        val CODE = FacilityErrorCode.FACILITY_NOT_FOUND
    }

    constructor(): this(CODE)
}