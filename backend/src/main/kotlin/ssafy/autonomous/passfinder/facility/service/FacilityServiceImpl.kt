package ssafy.autonomous.passfinder.facility.service

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import ssafy.autonomous.passfinder.common.exception.facility.FacilityNotFoundException
import ssafy.autonomous.passfinder.common.response.PassFinderResponseDto
import ssafy.autonomous.passfinder.facility.domain.Facility
import ssafy.autonomous.passfinder.facility.dto.request.FacilityTypesRequestDto
import ssafy.autonomous.passfinder.facility.repository.FacilityRepository
import javax.transaction.Transactional

@Service
class FacilityServiceImpl(
        private val facilityRepository: FacilityRepository
) : FacilityService {

    private val logger = KotlinLogging.logger {}

    // 필터링 입력할 때마다 리스트로 출력
    override fun facilityDynamic(facilityTypesRequest: FacilityTypesRequestDto): PassFinderResponseDto<List<Facility>>? {
        val inputFacilityType = facilityTypesRequest.filteringSearch

        // Repository -> Containing 사용
//        val resultFacilityTypes: List<Facility> = facilityRepository.findAllByFacilityTypeLikeOrderByHitCountDesc("%$inputFacilityType%")
        val resultFacilityTypes: List<Facility> = getFacilityTypes(inputFacilityType)

        // passFinder 응답 결과 (status, message, data)
        val passFinderResponseDto = PassFinderResponseDto<List<Facility>>(HttpStatus.OK, "true", resultFacilityTypes)
        return PassFinderResponseDto.of(passFinderResponseDto)
    }


    // 입력한 문자열을 기반으로 방 이름 리스트를 가져온다.
    fun getFacilityTypes(inputFacilityType: String): List<Facility> {
        return facilityRepository.findAllByFacilityNameContainingOrderByHitCountDesc(inputFacilityType)
    }


    // 필터링에 입력 후, 검색 버튼 클릭
    @Transactional
    override fun getFacilityTypes(facilitySearchRequest: FacilityTypesRequestDto): PassFinderResponseDto<Facility>? {
        val inputFacilityType = facilitySearchRequest.filteringSearch
        logger.info("msg : 시설 종류 :  $inputFacilityType")
        val curFacility: Facility = facilityRepository.findByFacilityName(inputFacilityType).orElseThrow { FacilityNotFoundException() }

        // (1) 검색 되었을 때 횟수 1증가
        curFacility.plusHitCount()

        // (2) 검색 되었을 때 횟수 1증가
        facilityRepository.updateFacility(curFacility)



        logger.info("msg : 업데이트 결과 : ${curFacility.getHisCount()}")


//        logger.info("msg : 업데이트 후, 결과 : ${curFacility.getFacilityType()}, hitCount : ${curFacility.plusHitCount()}")

        // update 저장후, 반환
        val passFinderResponseDto = PassFinderResponseDto<Facility>(
                HttpStatus.OK,
                "검색 후, 시설 조회 횟수 + 1, 시설 Entity 전달",
                curFacility
        )

        return PassFinderResponseDto.of2(passFinderResponseDto)
    }
}

