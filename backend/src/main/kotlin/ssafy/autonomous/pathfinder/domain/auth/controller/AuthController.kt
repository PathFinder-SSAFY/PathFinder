package ssafy.autonomous.pathfinder.domain.auth.controller

import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ssafy.autonomous.pathfinder.domain.auth.dto.request.TokenRequestDto
import ssafy.autonomous.pathfinder.domain.auth.service.AuthService
import ssafy.autonomous.pathfinder.global.common.ApiResponse

@RestController
class AuthController(
    private val authService : AuthService
) {

    private val logger = KotlinLogging.logger{}


    // (1) naverLogin
    @GetMapping("/naver/callback")
    fun naverLogin(@RequestBody tokenRequestDto : TokenRequestDto ) : ResponseEntity<ApiResponse>{
        logger.info("token : ${tokenRequestDto.getAccessToken()}")
        return ResponseEntity.ok(
            ApiResponse(
                data = authService.oAuthLogin(tokenRequestDto)
            )
        )

    }

    @GetMapping("/add/test")
    fun naverTest(@RequestBody tokenRequestDto: TokenRequestDto){
        logger.info("token : ${tokenRequestDto.getAccessToken()}")
    }
}