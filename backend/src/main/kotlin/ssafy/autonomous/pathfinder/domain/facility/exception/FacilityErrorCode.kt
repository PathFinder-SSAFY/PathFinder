package ssafy.autonomous.pathfinder.domain.facility.exception

import org.springframework.http.HttpStatus
import ssafy.autonomous.pathfinder.global.common.ErrorCode

enum class FacilityErrorCode(override val status: HttpStatus, override val message: String) : ErrorCode {

    // 성공했을 때
    SUCCESS_FILTERING(HttpStatus.OK, "필터링 검색 성공 하였습니다."),

    // 입력한 시설을 찾지 못했다.
    FACILITY_NOT_FOUND(HttpStatus.NOT_FOUND,"시설이 존재하지 않습니다.")

}