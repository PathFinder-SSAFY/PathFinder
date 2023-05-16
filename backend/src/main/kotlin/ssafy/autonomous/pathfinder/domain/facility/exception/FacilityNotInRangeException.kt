package ssafy.autonomous.pathfinder.domain.facility.exception

import ssafy.autonomous.pathfinder.global.common.CustomException
import ssafy.autonomous.pathfinder.global.common.ErrorCode

class FacilityNotInRangeException private constructor(errorCode: ErrorCode): CustomException(errorCode) {

    companion object{
        val CODE = FacilityErrorCode.FACILITY_NOT_IN_RANGE
    }

    constructor(): this(CODE)

}