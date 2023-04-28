package ssafy.autonomous.passfinder.common.exception.facility

import ssafy.autonomous.passfinder.common.CustomException
import ssafy.autonomous.passfinder.common.ErrorCode

class FacilityNotFoundException private constructor(errorCode: ErrorCode) : CustomException(errorCode) {

    companion object{
        val CODE = FacilityErrorCode.FACILITY_NOT_FOUND
    }

    constructor(): this(CODE)
}