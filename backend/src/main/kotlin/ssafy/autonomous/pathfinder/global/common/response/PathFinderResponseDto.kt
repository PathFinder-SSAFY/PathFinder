package ssafy.autonomous.pathfinder.global.common.response

import org.springframework.http.HttpStatus
import ssafy.autonomous.pathfinder.domain.facility.domain.Facility

data class PathFinderResponseDto<T>(
        val status: HttpStatus?, // 상태
        val message: String? = null, // 메세지
        val data: T? // 전달되는 데이터
){

    companion object{


        // (1) 시설 타입이 어떤 것인지 반환해주는 Dto
        fun of(pathFinderResponseDto: PathFinderResponseDto<List<Facility>>): PathFinderResponseDto<List<Facility>> {
            return PathFinderResponseDto(
                    status = pathFinderResponseDto.status,
                    message = pathFinderResponseDto.message,
                    data = pathFinderResponseDto.data
            )
        }
        
        // (2) 성공함을 알려주는 Dto
        fun of2(pathFinderResponseDto: PathFinderResponseDto<Facility>): PathFinderResponseDto<Facility> {
            return PathFinderResponseDto(
                    status = pathFinderResponseDto.status,
                    message = pathFinderResponseDto.message,
                    data = pathFinderResponseDto.data
            )
        }


        // (2)
//        fun of(pathFinderResponseDto: pathFinderResponseDto<FacilityTypesRequestDto>): pathFinderResponseDto<FacilityTypesRequestDto> {
//            return pathFinderResponseDto(
//                    status = pathFinderResponseDto.status,
//                    message = pathFinderResponseDto.message,
//                    data = pathFinderResponseDto.data
//            )
//
//        }
    }

}