package ssafy.autonomous.pathfinder.test.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping

@Api(tags=["통신 테스트 컨트롤러"])
@Controller
class TestController {
    
    @PostMapping("/hello")
    fun hello(): ResponseEntity<Void>{
        return ResponseEntity.ok().build()
    }
}