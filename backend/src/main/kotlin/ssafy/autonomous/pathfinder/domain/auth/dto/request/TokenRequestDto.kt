package ssafy.autonomous.pathfinder.domain.auth.dto.request

import io.swagger.annotations.ApiModelProperty

data class TokenRequestDto(
    @ApiModelProperty(example = "naver code(token)을 입력") private val accessToken: String
){
    fun getAccessToken(): String {
        return this.accessToken
    }
}