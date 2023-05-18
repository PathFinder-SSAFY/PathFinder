package ssafy.autonomous.pathfinder.domain.auth.service


import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ssafy.autonomous.pathfinder.domain.auth.dto.request.TokenRequestDto
import ssafy.autonomous.pathfinder.domain.auth.dto.response.TokenResponseDto


@SpringBootTest
class AuthServiceImplTest @Autowired constructor(
    private val authService: AuthService
){
    @Test
    @DisplayName("네이버 소셜 로그인")
    fun testNaverSocialLogin(){
        // given : 주어진 상황
        val tokenRequestDto = TokenRequestDto("AAAANhHNyLmSBGSi-zCq83RT85TBWs_dChJsyNHRG-3TE6i1fVN7XSf_UzzG_l5vp6g182HCn9V2sxzpYsLyZ9gFuTc")

        // when : 행동 또는 동작
        val tokenResponseDto =  authService.oAuthLogin(tokenRequestDto)

        // then : 예상 결과 또는 검증
        assertThat(tokenResponseDto.accessToken).isEqualTo("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsa2MyNjNAbmF2ZXIuY29tIiwiYXV0aCI6IlJPTEVfQURNSU4iLCJpYXQiOjE2ODQwNDYzNjksImV4cCI6MTY4NDA0ODE2OX0.2lQ__8MJ8PDs7qI0joyeWj0F2J2alB4ctTn8U_b7_XysdEs2s4CQdHsBuZ3JdpEOrGlx4Kvft0yE8YEPJA-7-g")
        assertThat(tokenResponseDto.refreshToken).isEqualTo("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsa2MyNjNAbmF2ZXIuY29tIiwiZXhwIjoxNjg0NjUxMTY5fQ.pnadvA_OB4E2MQSASIplo7vySeKK15XPqmuiuCYOnMEbip77NNE8HrKf_OrHecru0u0NYn9RIkhiZfb5tjGlgA")
        assertThat(tokenResponseDto.accessTokenExpiresIn).isEqualTo(1684048169457)

        // or

        assertThat(tokenResponseDto)
            .isEqualToComparingFieldByField(TokenResponseDto("" +
                    "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsa2MyNjNAbmF2ZXIuY29tIiwiYXV0aCI6IlJPTEVfQURNSU4iLCJpYXQiOjE2ODQwNDYzNjksImV4cCI6MTY4NDA0ODE2OX0.2lQ__8MJ8PDs7qI0joyeWj0F2J2alB4ctTn8U_b7_XysdEs2s4CQdHsBuZ3JdpEOrGlx4Kvft0yE8YEPJA-7-g\"", "" +
                    "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsa2MyNjNAbmF2ZXIuY29tIiwiZXhwIjoxNjg0NjUxMTY5fQ.pnadvA_OB4E2MQSASIplo7vySeKK15XPqmuiuCYOnMEbip77NNE8HrKf_OrHecru0u0NYn9RIkhiZfb5tjGlgA",
                1684048169457
            ))
    }
}