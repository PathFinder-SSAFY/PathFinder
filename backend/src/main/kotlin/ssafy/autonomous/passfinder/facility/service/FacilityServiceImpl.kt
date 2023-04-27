package ssafy.autonomous.passfinder.facility.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import ssafy.autonomous.passfinder.common.response.PassFinderResponseDto
import ssafy.autonomous.passfinder.facility.dto.request.FacilityTypesRequestDto
import ssafy.autonomous.passfinder.facility.repository.FacilityRepository

@Service
class FacilityServiceImpl(
        private val facilityRepository: FacilityRepository
) : FacilityService {

    // 필터링 검색
    override fun getFacilityTypes(facilityTypesRequest: FacilityTypesRequestDto): PassFinderResponseDto<Unit>?{
        val inputFacilityType = facilityTypesRequest.filteringSearch
        
        // Repository -> Containing 사용
        val resultFacilityTypes: Unit = facilityRepository.findAllByFacilityTypeContainingOrderByHitCountDesc(inputFacilityType)

        // passFinder 응답 결과 (status, message, data)
        val passFinderResponseDto = PassFinderResponseDto<Unit>(HttpStatus.OK, "true", resultFacilityTypes)
        return PassFinderResponseDto.of(passFinderResponseDto)
    }
}