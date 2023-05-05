//package ssafy.autonomous.pathfinder.domain.auth.exception
//
//import org.springframework.http.HttpStatus
//import ssafy.autonomous.pathfinder.global.common.CustomException
//import ssafy.autonomous.pathfinder.global.common.ErrorCode
//import javax.swing.text.html.HTML
//
//class OAuth2AuthenticationProcessingException private constructor(errorCode: ErrorCode) : CustomException(errorCode) {
//
//    companion object{
//        var CODE = AuthErrorCode.OAUTH2_AUTHENTICATION_PROCESSING_EXCEPTION
//    }
//
//    constructor() : this(CODE)
//    constructor(errorMessage: String): this(CODE){
//        CODE.message = errorMessage
//    }
//}