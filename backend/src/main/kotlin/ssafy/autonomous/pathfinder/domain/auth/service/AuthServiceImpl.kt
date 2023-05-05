package ssafy.autonomous.pathfinder.domain.auth.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import ssafy.autonomous.pathfinder.domain.auth.dto.request.TokenRequestDto
import ssafy.autonomous.pathfinder.domain.auth.security.JwtTokenProvider

class AuthServiceImpl(
    private val jwtTokenProvider: JwtTokenProvider,

    @Value("\${spring.security.oauth2.client.provider.naver.authorization_uri}")
    private val NAVER_LOGIN_URI: String,
    @Value("\${spring.security.oauth2.client.provider.naver.token_uri}")
    private val NAVER_TOKEN_URI: String,
    @Value("\${spring.security.oauth2.client.registration.naver.authorization-grant-type}")
    private val NAVER_GRANT_TYPE: String,
    @Value("\${spring.security.oauth2.client.registration.naver.clientId}")
    private val NAVER_CLIENT_ID: String,
    @Value("\${spring.security.oauth2.client.registration.naver.clientSecret}")
    private val NAVER_CLIENT_SECRET: String,
    @Value("\${spring.security.oauth2.client.registration.naver.redirectUri}")
    private val NAVER_REDIRECT_URI: String,
    @Value("\${spring.security.oauth2.client.registration.naver.scope}")
    private val NAVER_SCOPE: String
) : AuthService {


    override fun getAccessToken(tokenRequestDto: TokenRequestDto): TokenRequestDto {
        val params : MultiValueMap<String, String> = LinkedMultiValueMap()
        params.add("grant_type", NAVER_GRANT_TYPE)
        params.add("clientId", NAVER_CLIENT_ID)
        params.add("redirectUri", NAVER_REDIRECT_URI)
        params.add("response_type", "code")

        // 네이버 로그인에서 받아온 code로 토큰 발급


    }
}