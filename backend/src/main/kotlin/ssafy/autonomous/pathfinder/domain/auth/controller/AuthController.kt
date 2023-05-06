package ssafy.autonomous.pathfinder.domain.auth.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ssafy.autonomous.pathfinder.domain.auth.dto.request.TokenRequestDto
import ssafy.autonomous.pathfinder.domain.auth.service.AuthService
import ssafy.autonomous.pathfinder.global.common.ApiResponse

@RestController
@RequestMapping("/admin")
class AuthController(
    private val authService : AuthService
) {

    // (1) naverLogin
    @GetMapping("/naver/callback")
    fun naverLogin(@RequestParam("token") tokenRequestDto : TokenRequestDto ) : ResponseEntity<ApiResponse>{
        val jwt : JwtToken = authService.oAuthLogin(tokenRequestDto)

    }
}