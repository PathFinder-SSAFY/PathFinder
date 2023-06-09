package ssafy.autonomous.pathfinder.domain.auth.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiOperation
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ssafy.autonomous.pathfinder.domain.auth.dto.request.TokenRequestDto
import ssafy.autonomous.pathfinder.domain.auth.dto.response.TokenResponseDto
import ssafy.autonomous.pathfinder.domain.auth.service.AuthService
import ssafy.autonomous.pathfinder.global.common.response.ApiResponse

/*
* 1.
*
* */
@Api(tags = ["Naver social 로그인"])
@RestController
class AuthController(
    private val authService : AuthService
) {

    private val logger = KotlinLogging.logger{}



    /*
    * 1-1. 네이버 소셜 로그인 api
    * */
    @ApiOperation(value = "네이버 소셜 로그인 후, 반환 받은 code를 넘겨주면 된다.")
    @ApiImplicitParam(name = "code", value = "소셜 로그인 후 받은 code(고유 식별 번호)", dataTypeClass = TokenRequestDto::class)
    @PostMapping("/naver/callback")
    fun naverLogin(@RequestBody tokenRequestDto : TokenRequestDto ) : ResponseEntity<ApiResponse>{
        val t : TokenResponseDto =authService.oAuthLogin(tokenRequestDto)

        return ResponseEntity.status(HttpStatus.OK).body(
            ApiResponse(
                responseData =authService.oAuthLogin(tokenRequestDto)
            )
        )

    }
}