package ssafy.autonomous.passfinder.common.exception.facility

import org.springframework.http.HttpStatus
import ssafy.autonomous.passfinder.common.ErrorCode

enum class FacilityErrorCode(override val status: HttpStatus, override val message: String) : ErrorCode {

    // 성공했을 때
    SUCCESS_FILTERING(HttpStatus.OK, "필터링 검색 성공 하였습니다.")

    // 실패했을 때

}