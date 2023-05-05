package ssafy.autonomous.pathfinder.domain.auth.exception

import ssafy.autonomous.pathfinder.global.common.CustomException
import ssafy.autonomous.pathfinder.global.common.ErrorCode

class TokenExpiredException private constructor(errorCode: ErrorCode) : CustomException(errorCode) {

    companion object{
        val CODE = AuthErrorCode.TOKEN_EXPIRED
    }

    constructor() : this(CODE)
}