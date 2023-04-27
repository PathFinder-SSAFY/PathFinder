package ssafy.autonomous.passfinder.common


/*
    사용자 정의 예외처리
    404, 409와 같은 에러를 잡기 위해 필요한 클래스
*/
open class CustomException(private val errorCode: ErrorCode) : RuntimeException() {

    fun getErrorCode(): ErrorCode{
        return errorCode
    }

}