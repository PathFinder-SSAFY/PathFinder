package ssafy.autonomous.pathfinder.test.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Api(tags=["통신 테스트 컨트롤러"])
@Controller
class TestController(@Value("\${server.port}") private val port: String) {

    @GetMapping("/hello")
    fun hello(): ResponseEntity<Void>{
        return ResponseEntity.ok().build()
    }

    @GetMapping("/fail")
    fun badRequest(): ResponseEntity<Void> {
        return ResponseEntity.badRequest().build()
    }

    @GetMapping("/port")
    fun port(): String {
        return port
    }

}