//package ssafy.autonomous.pathfinder.domain.jwt
//
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.security.core.userdetails.User
//import org.springframework.stereotype.Component
//
//
//@Component
//class JwtTokenProvider(@Value("\${spring.jwt.secret}") secretKey: String?) {
//    private val key: Key
//
//    // application.yml 에 정의해놓은 jwt.secret 값을 가져와서 JWT 를 만들 때 사용하는 암호화 키값을 생성
//    init {
//        val keyBytes: ByteArray = Decoders.BASE64.decode(secretKey)
//        key = Keys.hmacShaKeyFor(keyBytes)
//    }
//
//    // 유저 정보를 넘겨받아서 Access Token 과 Refresh Token을 생성
//    fun generateTokenDto(authentication: Authentication): TokenDto {
//        // 권한들을 가져오기
//        val authorities: String = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","))
//        val now: Long = Date().getTime() // 현재 시간
//
//        // Access Token을 생성한다.
//        val accessTokenExpiresIn = Date(now + ACCESS_TOKEN_EXPIRE_TIME)
//        val accessToken: String = Jwts.builder()
//                .setSubject(authentication.getName()) // payload "sub": "name"
//                .claim(AUTHORITIES_KEY, authorities) // payload "auth": "ROLE_USER"
//                .setExpiration(accessTokenExpiresIn) // payload "exp": 1516239022 (예시)
//                .signWith(key, SignatureAlgorithm.HS512) // header "alg": "HS512"
//                .compact()
//
//
//        // Refresh Token 생성
//        val refreshToken: String = Jwts.builder()
//                .setExpiration(Date(now + REFRESH_TOKEN_EXPIRE_TIME))
//                .signWith(key, SignatureAlgorithm.HS512)
//                .compact()
//        return TokenDto.builder()
//                .grantType(BEARER_TYPE)
//                .accessToken(accessToken)
//                .accessTokenExpiresIn(accessTokenExpiresIn.getTime()) // 만료시간
//                .refreshToken(refreshToken)
//                .build()
//    }
//
//    // JWT 토큰을 복호화하여 토큰에 들어 있는 정보를 꺼낸다.
//    fun getAuthentication(accessToken: String): Authentication {
//        // 토큰 복호화
//        val claims: Claims = parseClaims(accessToken)
//        if (claims.get(AUTHORITIES_KEY) == null) {
//            throw RuntimeException("권한 정보가 없는 토큰입니다.")
//        }
//
//        // 클레임에서 권한 정보 가져오기
//        val authorities: Collection<GrantedAuthority?> = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
//                .map { SimpleGrantedAuthority() }
//                .collect(Collectors.toList())
//
//
//        // UserDetails 객체를 만들어서 Authentication 리턴 (권한 인증 리턴)
//        val userDetails: UserDetails = User(
//                claims.getSubject(), "", authorities
//        )
//        return UsernamePasswordAuthenticationToken(userDetails, "", authorities)
//    }
//
//    // parseClaims 메소드는 만료된 토큰이어도 정보를 꺼내기 위해서 따로 분리
//    private fun parseClaims(accessToken: String): Claims {
//        return try {
//            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody()
//        } catch (e: ExpiredJwtException) {
//            e.getClaims()
//        }
//    }
//
//    // 토큰 정보를 검증
//    fun validateToken(token: String?): Boolean {
//        try {
//            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
//            return true
//        } catch (e: io.jsonwebtoken.security.SecurityException) {
//            log.info("잘못된 JWT 서명입니다.")
//        } catch (e: MalformedJwtException) {
//            log.info("잘못된 JWT 서명입니다.")
//        } catch (e: ExpiredJwtException) {
//            log.info("만료된 JWT 토큰입니다.")
//        } catch (e: UnsupportedJwtException) {
//            log.info("지원되지 않는 JWT 토큰입니다.")
//        } catch (e: IllegalArgumentException) {
//            log.info("JWT 토큰이 잘못되었습니다.")
//        }
//        return false
//    }
//
//    companion object {
//        // 이 클래스에서
//        // - 유저 정보로 JWT 토큰을 만들거나 토큰을 바탕으로 유저 정보를 가져온다.
//        // - JWT 토큰에 관련된 암호화, 복호화, 검증 로직은 다 이곳에서 이루어진다.
//        // bean 직접 생성
//        private const val AUTHORITIES_KEY = "auth"
//        private const val BEARER_TYPE = "Bearer"
//        private const val ACCESS_TOKEN_EXPIRE_TIME = (1000 * 60 * 30 // 30분
//                ).toLong()
//        private const val REFRESH_TOKEN_EXPIRE_TIME = (1000 * 60 * 60 * 24 * 7 // 7일
//                ).toLong()
//    }
//}