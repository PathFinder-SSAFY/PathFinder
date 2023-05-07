package ssafy.autonomous.pathfinder.domain.auth.service

import ssafy.autonomous.pathfinder.domain.auth.dto.request.TokenRequestDto
import ssafy.autonomous.pathfinder.domain.auth.dto.response.TokenResponseDto

interface AuthService {
    fun oAuthLogin(tokenRequestDto : TokenRequestDto) : TokenResponseDto
}