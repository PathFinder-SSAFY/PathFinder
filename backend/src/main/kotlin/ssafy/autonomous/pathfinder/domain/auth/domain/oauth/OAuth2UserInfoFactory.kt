package ssafy.autonomous.pathfinder.domain.auth.domain.oauth

import org.springframework.http.HttpStatus
import ssafy.autonomous.pathfinder.domain.auth.exception.OAuth2AuthenticationProcessingException
import ssafy.autonomous.pathfinder.global.common.ErrorCode


class OAuth2UserInfoFactory {
    companion object{

        fun getOAuth2UserInfo(registrationId: String, attributes: Map<String, Any>): OAuth2UserInfo {
            return when {
                AuthProvider.NAVER.equalWith(registrationId) -> {
                    NaverOAuth2UserInfo(attributes)
                }else -> {
                    throw OAuth2AuthenticationProcessingException("$registrationId 로그인은 지원하지 않습니다.")
                }
            }
        }
    }
}