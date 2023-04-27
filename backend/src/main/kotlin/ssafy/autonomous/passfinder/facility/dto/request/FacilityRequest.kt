package ssafy.autonomous.passfinder.facility.dto.request

import ssafy.autonomous.passfinder.facility.domain.FacilityType

data class FacilityRequest(
    val filter: String,
    val type: FacilityType
)