package ssafy.autonomous.passfinder.common.response

import org.springframework.http.HttpStatus
import ssafy.autonomous.passfinder.facility.domain.Facility

data class PassFinderResponseDto<T>(
        val status: HttpStatus?, // 상태
        val message: String? = null, // 메세지
        val data: T? // 전달되는 데이터
){

    companion object{


        // (1) 시설 타입이 어떤 것인지 반환해주는 Dto
        fun of(passFinderResponseDto: PassFinderResponseDto<List<Facility>>): PassFinderResponseDto<List<Facility>> {
            return PassFinderResponseDto(
                    status = passFinderResponseDto.status,
                    message = passFinderResponseDto.message,
                    data = passFinderResponseDto.data
            )
        }
        
        // (2) 성공함을 알려주는 Dto
        fun of2(passFinderResponseDto: PassFinderResponseDto<Facility>): PassFinderResponseDto<Facility>{
            return PassFinderResponseDto(
                    status = passFinderResponseDto.status,
                    message = passFinderResponseDto.message,
                    data = passFinderResponseDto.data
            )
        }


        // (2)
//        fun of(passFinderResponseDto: PassFinderResponseDto<FacilityTypesRequestDto>): PassFinderResponseDto<FacilityTypesRequestDto> {
//            return PassFinderResponseDto(
//                    status = passFinderResponseDto.status,
//                    message = passFinderResponseDto.message,
//                    data = passFinderResponseDto.data
//            )
//
//        }
    }

}