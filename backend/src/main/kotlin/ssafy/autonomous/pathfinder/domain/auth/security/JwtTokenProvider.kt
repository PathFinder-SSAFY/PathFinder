package ssafy.autonomous.pathfinder.domain.auth.security

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import ssafy.autonomous.pathfinder.domain.auth.dto.response.TokenResponseDto
import java.security.Key
import java.util.*
import java.util.stream.Collectors


@Component
class JwtTokenProvider(
    @Value("\${security.jwt.secret}")
    private val secretKey: String
) {
    private val key: Key

    // application.yml 에 정의해놓은 jwt.secret 값을 가져와서 JWT 를 만들 때 사용하는 암호화 키값을 생성
    init {
        val keyBytes: ByteArray = Decoders.BASE64.decode(secretKey)
        key = Keys.hmacShaKeyFor(keyBytes)
    }

    private val logger = KotlinLogging.logger {}

    companion object {
        private const val AUTHORITIES_KEY = "auth"
        private const val BEARER_TYPE = "Bearer "
        private const val ACCESS_TOKEN_EXPIRE_TIME: Long = 1000L * 60 * 30 // 30분
        private const val REFRESH_TOKEN_EXPIRE_TIME: Long = 1000L * 60 * 60 * 24 * 7 // 7일
    }

    // 이메일을 넘겨받아서 Access Token 과 Refresh Token을 생성
    fun createToken(email: String, authentication: Authentication): TokenResponseDto {

        // 권한들을 가져오기
        val authorities: String = authentication.authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","))

        val now: Long = Date().time // 현재 시간
        val accessTokenExpiresIn = Date(now + ACCESS_TOKEN_EXPIRE_TIME)


        /*
        * accessToken
        * @param userObjectId : 회원 ObjectId
        * @param now : 현재 시간
        * @return : accessToken
        * */

        /*
        * setSubject : JWT 토큰의 주제(Subject)를 나타내는 값
        * */

        val accessToken: String = Jwts.builder()
            .setSubject(email)
            .claim(AUTHORITIES_KEY, authorities)
            .setIssuedAt(Date(now))
            .setExpiration(accessTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()

        /*
        * refreshToken
        * @return 리프래쉬 토큰
        * */
        val refreshToken: String = Jwts.builder()
            .setSubject(email)
            .setExpiration(Date(now + REFRESH_TOKEN_EXPIRE_TIME))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()


        return TokenResponseDto(
            accessToken = accessToken,
            refreshToken = refreshToken,
            accessTokenExpiresIn = accessTokenExpiresIn.time
        )
    }


    fun reissueAccessToken(email: String?, authentication: Authentication): TokenResponseDto {
        val date = Date()
        val accessExpires = 30 * 60 * 1000L // 30minutes
        val now: Long = Date().time // 현재 시간

        val authorities: String = authentication.authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","))



        val accessToken: String = Jwts.builder()
            .setSubject(email)
            .claim(AUTHORITIES_KEY, authorities)
            .setIssuedAt(Date(now))
            .setExpiration(Date(date.time + ACCESS_TOKEN_EXPIRE_TIME))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()


        return TokenResponseDto.Builder().accessToken(accessToken).build()
    }


    // 토큰으로부터 email을 추출한다.
    fun getEmailFromToken(accessToken: String) : String{
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).body.subject
    }

    // token을 통해 이메일을 얻는다.
//    fun getEmailFromBearerToken(bearerToken: String): String? {
//        if (bearerToken.startsWith(BEARER_TYPE)) {
//            val token: String = bearerToken.substring(BEARER_TYPE.length)
//            return decodeToken(token).getSubject()
//        }
//        throw RuntimeException()
//    }
//
//
//    fun decodeToken(token: String?): DecodedJWT? {
//        return try {
//
//
//            // 토큰 검증
//            jwtVerifier.verify(token)
//        } catch (e: JWTVerificationException) {
//            throw e
//        }
//    }


    // JWT 토큰을 복호화하여 토큰에 들어 있는 정보를 꺼낸다.
    fun getAuthentication(token: String?): Authentication {
        // 토큰 복호화
        val claims: Claims = parseClaims(token)

        if (claims[AUTHORITIES_KEY] == null) {
            throw RuntimeException("권한 정보가 없는 토큰입니다.")
        }

        // 사용자 권한을 설정
        val authorities: Collection<GrantedAuthority?> = claims[AUTHORITIES_KEY].toString()
            .split(",")
            .map { role -> SimpleGrantedAuthority(role) }


        // UserDetails 객체를 만들어서 Authentication 리턴 (권한 인증 리턴)
        val principal = User(claims.subject, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }


    /*
    * 토큰 parsing
    * return : 파싱한 토큰
    * */
    // parseClaims 메소드는 만료된 토큰이어도 정보를 꺼내기 위해서 따로 분리
    private fun parseClaims(token: String?): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token).body
    }


    fun validateToken(token: String?): Boolean{
        return this.getTokenClaims(token) != null
    }

    /*
    * 주어진 JWT 토큰의 클레임(Claims) 정보를 파싱하여 반환하는 함수
    * - 클레임은 JWT 페이로드에 포함된 정보를 나타낸다.
    * - 예를 들어 토큰의 발급자, 만료 시간, 사용자 식별 정보 등이 클레임으로 포함될 수 있다.
    * @Param : token
    *
    * */
    fun getTokenClaims(token: String?): Claims? {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
        } catch (e: io.jsonwebtoken.security.SecurityException) {
            logger.info("잘못된 JWT 서명입니다..")
        } catch (e: MalformedJwtException) {
            logger.info("잘못된 JWT 토큰입니다.")
        } catch (e: ExpiredJwtException) {
            logger.info("만료된 JWT 토큰입니다.")
        } catch (e: UnsupportedJwtException) {
            logger.info("지원되지 않는 JWT 토큰입니다.")
        } catch (e: IllegalArgumentException) {
            logger.info("JWT 토큰이 잘못되었습니다.")
        }
        return null
    }

    // accessToken 남은 유효시간
    fun getExpiration(accessToken: String): Long {
        val expiration: Date = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(accessToken)
            .body
            .expiration

        // 현재 시간
        val now = Date().time
        return expiration.time - now
    }
}