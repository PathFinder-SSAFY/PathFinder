package ssafy.autonomous.pathfinder.domain.auth.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import ssafy.autonomous.pathfinder.domain.auth.dto.request.TokenRequestDto
import ssafy.autonomous.pathfinder.domain.auth.oauth.NaverOAuth
import ssafy.autonomous.pathfinder.domain.auth.security.JwtTokenProvider

class AuthServiceImpl(
    private val jwtTokenProvider: JwtTokenProvider,
    private val naverOAuth: NaverOAuth
) : AuthService {
    override fun oAuthLogin(tokenRequestDto: TokenRequestDto): TokenRequestDto {
        naverOAuth.requestAccessToken(tokenRequestDto)
    }


}