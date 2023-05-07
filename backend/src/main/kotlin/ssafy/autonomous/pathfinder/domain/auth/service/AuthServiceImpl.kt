package ssafy.autonomous.pathfinder.domain.auth.service

import org.springframework.stereotype.Service
import ssafy.autonomous.pathfinder.domain.auth.dto.request.TokenRequestDto
import ssafy.autonomous.pathfinder.domain.auth.dto.response.TokenResponseDto
import ssafy.autonomous.pathfinder.domain.auth.oauth.NaverOAuth
import ssafy.autonomous.pathfinder.domain.auth.security.JwtTokenProvider

@Service
class AuthServiceImpl(
    private val jwtTokenProvider: JwtTokenProvider,
    private val naverOAuth: NaverOAuth
) : AuthService{
    override fun oAuthLogin(tokenRequestDto: TokenRequestDto): TokenResponseDto {
        naverOAuth.requestAccessToken(tokenRequestDto)

        return TokenResponseDto.Builder().build()
    }


}