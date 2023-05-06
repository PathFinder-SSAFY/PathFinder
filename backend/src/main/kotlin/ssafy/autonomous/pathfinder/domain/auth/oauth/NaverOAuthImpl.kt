package ssafy.autonomous.pathfinder.domain.auth.oauth

import net.minidev.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import ssafy.autonomous.pathfinder.domain.auth.dto.request.TokenRequestDto
import ssafy.autonomous.pathfinder.domain.auth.dto.response.TokenResponseDto

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
    private val NAVER_SCOPE: String,
    private val NAVER_REQUEST : String = "https://openapi.naver.com/v1/nid/me"
) {


//    override fun requestAccessToken(tokenRequestDto: TokenRequestDto): TokenResponseDto {
//
//        val response: ResponseEntity<String> = getHttpNaverRequest(tokenRequestDto)
//        val naverId: Long = response.getJSONObject("response").getString("id").toLong()
//
//        if(response.statusCode == HttpStatus.OK){
//            val body: JSONObject? = response.body
//            body?.get("id")
//            val naver: Any? = body?.get("response")
//            val naverId: String = body?.get("id").toString()
//            val email = (naver as LinkedHashMap<*, *>)["email"].toString()
//        }
////        response.stat
////        val jsonParser = JSONParser()
////        var jsonObject = jsonParser.parse(result)
////        val response = jsonObject["response"].toString()
////
////        jsonObject = jsonParser.parse(response)
////        email = jsonObject["email"] as String?
//
//
//
//    }

    fun getHttpNaverRequest(tokenRequestDto: TokenRequestDto): ResponseEntity<String> {
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
            String::class.java
        )
    }
}