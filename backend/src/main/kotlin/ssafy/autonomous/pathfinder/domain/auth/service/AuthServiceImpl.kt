package ssafy.autonomous.pathfinder.domain.auth.service

import com.nimbusds.jose.proc.SecurityContext
import mu.KotlinLogging
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import ssafy.autonomous.pathfinder.domain.administrator.domain.Administrator
import ssafy.autonomous.pathfinder.domain.administrator.domain.Authority
import ssafy.autonomous.pathfinder.domain.administrator.dto.AdministratorInfoDto
import ssafy.autonomous.pathfinder.domain.administrator.repository.AdministratorRepository
import ssafy.autonomous.pathfinder.domain.auth.dto.request.TokenRequestDto
import ssafy.autonomous.pathfinder.domain.auth.dto.response.TokenResponseDto
import ssafy.autonomous.pathfinder.domain.auth.oauth.NaverOAuth
import ssafy.autonomous.pathfinder.domain.auth.security.AdministratorPrincipal
import ssafy.autonomous.pathfinder.domain.auth.security.JwtTokenProvider

@Service
class AuthServiceImpl(
    private val jwtTokenProvider: JwtTokenProvider,
    private val naverOAuth: NaverOAuth,
    private val administratorRepository: AdministratorRepository
) : AuthService{

    private val logger = KotlinLogging.logger{}

    /*
    * 소셜 로그인
    * @param
    * tokenRequestDto : code
    * */
    override fun oAuthLogin(tokenRequestDto: TokenRequestDto): TokenResponseDto {
        // (1) naver code를 전달하여, 사용자 정보 email 발급
        val administratorInfoDto : AdministratorInfoDto = naverOAuth.requestAccessToken(tokenRequestDto)

        // (2) db를 조회하여, 존재하지 않는다면 회원가입
        val administrator: Administrator = saveIfNotDuplicatedEmail(administratorInfoDto)

        // (3) login을 진행한다.
        val authentication: Authentication = setAuthenticationAfterLogin(administrator)

        // (4)


        return TokenResponseDto.Builder().build()
    }

    /*
    * 네이버 소셜 로그인할 때, 발급 받은 id를 통해 db에 존재하는지 확인한 후, 회원가입 시킨다.
    * */
    fun saveIfNotDuplicatedEmail(administratorInfoDto: AdministratorInfoDto) : Administrator{
        val email = administratorInfoDto.email

        // 만약, email이 DB에 조회되지 않는다면 비회원이다.
        if(!administratorRepository.existsAdministratorByEmail(email)){
            val newAdministrator : Administrator = Administrator(
                email = administratorInfoDto.email,
                naverId = administratorInfoDto.naverId,
                authority = Authority.ROLE_ADMIN
            )
            administratorRepository.save(newAdministrator)

            logger.info("회원가입이 완료됐습니다.")
        }

        // email 기준 관리자를 찾는다.
        return administratorRepository.findByEmail(email)
    }

    /*
    * 메서드 설명 : 소셜 로그인 후, Jwt 생성하는 함수
    * @param : 관리자
    * */
    fun setAuthenticationAfterLogin(administrator: Administrator): Authentication{
        val email = administrator.email
        val authority = administrator.authority

        val userDetails : UserDetails = AdministratorPrincipal(email, authority)
        val authentication: Authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        SecurityContextHolder.getContext().authentication = authentication

        return authentication
    }


    /*
    * 메서드 설명 : 인증 정보를 입력받아 토큰을 생성하는 작업을 수행하는 함수
    *
    * */
    fun generateTokenFromCredentials(authentication: Authentication) : TokenResponseDto {
        val administratorPrincipal : AdministratorPrincipal
    }

}