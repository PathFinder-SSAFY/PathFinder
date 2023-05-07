package ssafy.autonomous.pathfinder.domain.auth.oauth

import mu.KotlinLogging
import net.minidev.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import ssafy.autonomous.pathfinder.domain.administrator.domain.Administrator
import ssafy.autonomous.pathfinder.domain.administrator.dto.AdministratorInfoDto
import ssafy.autonomous.pathfinder.domain.auth.dto.request.TokenRequestDto
import ssafy.autonomous.pathfinder.domain.auth.dto.response.TokenResponseDto


@Component
class NaverOAuthImpl(
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
) : NaverOAuth {

    private val NAVER_REQUEST : String = "https://openapi.naver.com/v1/nid/me"

    private val logger = KotlinLogging.logger{}

    override fun requestAccessToken(tokenRequestDto: TokenRequestDto): AdministratorInfoDto {

        val response: ResponseEntity<JSONObject> = getHttpNaverRequest(tokenRequestDto)
        if(response.statusCode == HttpStatus.OK){

            val body: JSONObject? = response.body
            val naver: Any? = body?.get("response")
            // id, email 발급
            // id : primarykey
            // email : 이메일
            val naverId = (naver as LinkedHashMap<*, *>)["id"].toString()
            val email = (naver as LinkedHashMap<*, *>)["email"].toString()

            logger.info("email : $email + id : $naverId")
            return AdministratorInfoDto(
                naverId = naverId,
                email =  email
            )
        }
        return null!!
    }

    fun getHttpNaverRequest(tokenRequestDto: TokenRequestDto): ResponseEntity<JSONObject> {
        val accessToken = tokenRequestDto.getAccessToken()

        val restTemplate = RestTemplate()
//        restTemplate.requestFactory = HttpComponentsClientHttpRequestFactory()

        // HttpHeader 오브젝트 생성
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_JSON
        httpHeaders.setBearerAuth(accessToken)

        // Http 요청
        return restTemplate.exchange(
            NAVER_REQUEST,
            HttpMethod.POST,
            HttpEntity<Any>(httpHeaders),
            JSONObject::class.java
        )
    }
}