package ssafy.autonomous.pathfinder.domain.facility.domain

import ssafy.autonomous.pathfinder.domain.facility.dto.response.FacilitySearchNeedCoordinateDto

enum class FacilityCoordinate(val x: Double,val y: Double,val z:Double){
    CLASSROOM401(0.0,0.0,0.0),
    CLASSROOM402(0.0,0.0,0.0),
    CLASSROOM403(0.0,0.0,0.0),
    CLASSROOMBIG(7.26, 0.0, -6.86),
    RESTROOMMAN(25.23, 0.0, -12.99),
    RESTROOMWOMAN(38.45, 0.0, -12.99),
    BREAKROOM401(19.52, 0.0, -3.25),
    BREAKROOM402(24.71, 0.0, -3.25),
    BREAKROOM403(32.33, 0.0, -3.25),
    STAIRSLEFT(22.45, 0.0, -10.86),
    STAIRSRIGHT(0.0, 0.0, 0.0),
    ELEVATOR(31.92, 0.0, -12.99)
}