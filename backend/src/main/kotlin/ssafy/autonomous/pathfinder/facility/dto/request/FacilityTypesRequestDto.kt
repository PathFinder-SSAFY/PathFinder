package ssafy.autonomous.pathfinder.facility.dto.request

import io.swagger.annotations.ApiModelProperty

data class FacilityTypesRequestDto(
    @ApiModelProperty(example = "강의실 401") val filteringSearch: String
)