package ssafy.autonomous.pathfinder.domain.floors.dto.request

import io.swagger.annotations.ApiModelProperty
import ssafy.autonomous.pathfinder.domain.facility.domain.Facility

data class FloorsCurrentLocationUpdateRequestDto(
    // x, y, z 좌표 + 시설 + UUID
    @ApiModelProperty(example = "10.45") val x: Double,
    @ApiModelProperty(example = "0.0") val y: Double,
    @ApiModelProperty(example = "-5.34") val z: Double,

    @ApiModelProperty(example = "4층 대강의실") val facilityName: String
)