package ssafy.autonomous.pathfinder.domain.auth.dto.response


data class TokenResponseDto(
        private val grantType: String, // 고객
        private val accessToken: String,
        private val refreshToken: String,
        private val accessTokenExpiresIn: Long // 액세스 토큰 만료기간
)