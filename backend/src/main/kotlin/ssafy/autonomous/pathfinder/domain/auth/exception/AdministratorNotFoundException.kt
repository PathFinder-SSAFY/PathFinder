package ssafy.autonomous.pathfinder.domain.auth.exception

import ssafy.autonomous.pathfinder.global.common.CustomException
import ssafy.autonomous.pathfinder.global.common.ErrorCode

class AdministratorNotFoundException private constructor(errorCode: ErrorCode) : CustomException(errorCode) {

    companion object{
        val CODE = AuthErrorCode.AUTH_NOT_FOUND
    }


    constructor(): this(CODE)
}