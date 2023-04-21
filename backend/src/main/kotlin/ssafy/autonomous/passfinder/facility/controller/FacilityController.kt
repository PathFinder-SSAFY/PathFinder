package ssafy.autonomous.passfinder.facility.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import ssafy.autonomous.passfinder.facility.service.FacilityService


@Controller
@RequestMapping("/facility")
class FacilityController(
        private val facilityService : FacilityService
) {


    @GetMapping
    fun getFacility
}