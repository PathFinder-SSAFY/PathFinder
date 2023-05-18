package ssafy.autonomous.pathfinder.domain.facility.dto.request

import io.swagger.annotations.ApiModelProperty

data class FacilityNameRequestDto(
    @ApiModelProperty(example = "4층 대강의실") val facilityName: String
)