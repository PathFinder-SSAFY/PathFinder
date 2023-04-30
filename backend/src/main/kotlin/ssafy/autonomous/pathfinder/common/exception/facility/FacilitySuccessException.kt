package ssafy.autonomous.pathfinder.common.exception.facility

import ssafy.autonomous.pathfinder.common.CustomException
import ssafy.autonomous.pathfinder.common.ErrorCode


// 성공했을 때, 예외처리
class FacilitySuccessException private constructor(errorCode: ErrorCode) : CustomException(errorCode){

    companion object {
        val CODE = FacilityErrorCode.SUCCESS_FILTERING
    }

    constructor() : this(CODE)
}