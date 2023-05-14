package ssafy.autonomous.pathfinder.domain.auth.oauth

import mu.KotlinLogging
import net.minidev.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import ssafy.autonomous.pathfinder.domain.administrator.dto.AdministratorInfoDto
import ssafy.autonomous.pathfinder.domain.auth.dto.request.TokenRequestDto


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
        // 네이버 소셜 로그인 회원 검증
//        val accessToken = tokenRequestDto.getAccessToken()
//        val restTemplate: RestTemplate = RestTemplate()
//        val params: MultiValueMap<String, String> = LinkedMultiValueMap()
//        params.add("client_id", NAVER_CLIENT_ID)
//        params.add("client_secret", NAVER_CLIENT_SECRET)
//        params.add("grant_type", NAVER_GRANT_TYPE)
//        params.add("code", accessToken)
//        params.add("state", "c2238709-bee1-4c08-8c44-381ee9c5fe67")
//        val headers = HttpHeaders()
//        val request: HttpEntity<MultiValueMap<String, String>> = HttpEntity(params, headers)
//        logger.info("client id : $NAVER_CLIENT_ID  +   client_secret : $NAVER_CLIENT_SECRET + grant_type : $NAVER_GRANT_TYPE")
//
//        val response: ResponseEntity<JSONObject> = restTemplate.postForEntity<JSONObject>(
//            NAVER_TOKEN_URI, request,
//            JSONObject::class.java
//        )
//
//        if(response.statusCode == HttpStatus.OK){
//            logger.info("body : $response.body" )
//            logger.info("성공하였습니다.")
//        }


        val response: ResponseEntity<JSONObject> = getHttpNaverRequest(tokenRequestDto)
        if(response.statusCode == HttpStatus.OK){

            val body: JSONObject? = response.body
            val naver: Any? = body?.get("response")
            // id, email 발급
            // id : primarykey
            // email : 이메일
            val naverId = (naver as LinkedHashMap<*, *>)["id"].toString()
            val email = (naver as LinkedHashMap<*, *>)["email"].toString()

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
        restTemplate.requestFactory = HttpComponentsClientHttpRequestFactory()

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