package ssafy.autonomous.pathfinder.domain.facility.dto.request

import io.swagger.annotations.ApiModelProperty


data class FacilityCurrentLocationRequestDto(
    @ApiModelProperty(example = "23.45") val x: Double,
    @ApiModelProperty(example = "0.0") val y: Double,
    @ApiModelProperty(example = "-5.34") val z: Double
)