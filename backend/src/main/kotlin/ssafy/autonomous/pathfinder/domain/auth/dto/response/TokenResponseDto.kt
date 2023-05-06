package ssafy.autonomous.pathfinder.domain.auth.dto.response


class TokenResponseDto(
        private val accessToken: String,
        private val refreshToken: String,
        private val accessTokenExpiresIn: Long // 액세스 토큰 만료기간
) {

        data class Builder(
                var accessToken: String = "",
                var refreshToken: String = "",
                var accessTokenExpiresIn: Long = 0
        ) {
                fun accessToken(accessToken: String) = apply { this.accessToken = accessToken }
                fun refreshToken(refreshToken: String) = apply { this.refreshToken = refreshToken }
                fun accessTokenExpiresIn(accessTokenExpiresIn: Long) = apply { this.accessTokenExpiresIn = accessTokenExpiresIn }
                fun build() = TokenResponseDto(accessToken, refreshToken, accessTokenExpiresIn)
        }
}