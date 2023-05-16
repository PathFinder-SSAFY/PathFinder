package ssafy.autonomous.pathfinder.domain.building.exception

import ssafy.autonomous.pathfinder.global.common.CustomException
import ssafy.autonomous.pathfinder.global.common.ErrorCode

class CustomerNotFoundException private constructor(errorCode: ErrorCode): CustomException(errorCode) {

    companion object{
        val CODE = BuildingErrorCode.CUSTOMER_ID_NOT_FOUND
    }

    constructor(): this(CODE)
}