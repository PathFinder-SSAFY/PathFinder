package ssafy.autonomous.pathfinder.domain.auth.oauth

import org.springframework.http.ResponseEntity
import ssafy.autonomous.pathfinder.domain.auth.dto.request.TokenRequestDto
import ssafy.autonomous.pathfinder.domain.auth.dto.response.TokenResponseDto

interface NaverOAuth {
    fun requestAccessToken(tokenRequestDto : TokenRequestDto) : TokenResponseDto
}