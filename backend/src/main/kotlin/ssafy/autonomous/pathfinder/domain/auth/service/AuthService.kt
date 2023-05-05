package ssafy.autonomous.pathfinder.domain.auth.service

import ssafy.autonomous.pathfinder.domain.auth.dto.request.TokenRequestDto

interface AuthService {
    fun getAccessToken(tokenRequestDto : TokenRequestDto) : TokenRequestDto
}