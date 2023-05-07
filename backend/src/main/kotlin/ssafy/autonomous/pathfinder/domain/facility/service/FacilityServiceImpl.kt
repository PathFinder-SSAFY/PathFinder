package ssafy.autonomous.pathfinder.domain.facility.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import ssafy.autonomous.pathfinder.domain.facility.domain.Facility
import ssafy.autonomous.pathfinder.domain.facility.dto.request.FacilityTypesRequestDto
import ssafy.autonomous.pathfinder.domain.facility.exception.FacilityNotFoundException
import ssafy.autonomous.pathfinder.domain.facility.repository.FacilityJpaRepository
import ssafy.autonomous.pathfinder.domain.facility.repository.FacilityQuerydslRepository
import javax.transaction.Transactional

@Service
class FacilityServiceImpl(
        private val facilityJpaRepository: FacilityJpaRepository,
        private val facilityQuerydslRepository: FacilityQuerydslRepository
) : FacilityService {

    private val logger = KotlinLogging.logger {}

    // 필터링 입력할 때마다 리스트로 출력
    override fun facilityDynamic(facilityTypesRequest: FacilityTypesRequestDto): List<Facility> {
        val inputFacilityType = facilityTypesRequest.filteringSearch

        // Repository -> Containing 사용
//        val resultFacilityTypes: List<Facility> = facilityRepository.findAllByFacilityTypeLikeOrderByHitCountDesc("%$inputFacilityType%")
        return getFacilityTypesDynamic(inputFacilityType)
    }


    // 입력한 문자열을 기반으로 방 이름 리스트를 가져온다.
    fun getFacilityTypesDynamic(inputFacilityType: String): List<Facility> {
        return facilityJpaRepository.findAllByFacilityNameContainingOrderByHitCountDesc(inputFacilityType)
    }


    // 필터링에 입력 후, 검색 버튼 클릭
    @Transactional
    override fun getFacilityTypes(facilitySearchRequest: FacilityTypesRequestDto): Facility {
        val inputFacilityType = facilitySearchRequest.filteringSearch
//        logger.info("msg : 시설 종류 :  $inputFacilityType")
        val curFacility: Facility = facilityJpaRepository.findByFacilityName(inputFacilityType).orElseThrow { FacilityNotFoundException() }

        // (1) 검색 되었을 때 횟수 1증가
        curFacility.plusHitCount()

        // (2) 검색 되었을 때 횟수 1증가 (EntityManager - merge)
        facilityQuerydslRepository.updateFacility(curFacility)

        return curFacility
    }
}

