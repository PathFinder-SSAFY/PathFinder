package ssafy.autonomous.pathfinder.domain.auth.oauth

import ssafy.autonomous.pathfinder.domain.administrator.dto.AdministratorInfoDto
import ssafy.autonomous.pathfinder.domain.auth.dto.request.TokenRequestDto

interface NaverOAuth {
    fun requestAccessToken(tokenRequestDto : TokenRequestDto) : AdministratorInfoDto
}