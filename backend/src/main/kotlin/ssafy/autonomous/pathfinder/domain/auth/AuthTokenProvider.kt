//package ssafy.autonomous.pathfinder.domain.auth
//
//import io.jsonwebtoken.security.Keys
//import mu.KotlinLogging
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
//import org.springframework.security.core.Authentication
//import org.springframework.security.core.GrantedAuthority
//import org.springframework.security.core.authority.SimpleGrantedAuthority
//import org.springframework.security.core.userdetails.User
//
//import java.security.Key
//import java.util.*
//import java.util.stream.Collectors
//
//
//class AuthTokenProvider(secret: String) {
//    private val key: Key
//    private val logger = KotlinLogging.logger{}
//
//    init {
//        key = Keys.hmacShaKeyFor(secret.toByteArray())
//    }
//
//    fun createAuthToken(id: String?, expiry: Date?): AuthToken {
//        return AuthToken(id!!, expiry!!, key)
//    }
//
//    fun createAuthToken(id: String?, role: String?, expiry: Date?): AuthToken {
//        return AuthToken(id!!, role!!, expiry!!, key)
//    }
//
//    fun convertAuthToken(token: String?): AuthToken {
//        return AuthToken(token!!, key)
//    }
//
//    fun getAuthentication(authToken: AuthToken): Authentication {
//        return if (authToken.validate()) {
//            val claims = authToken.tokenClaims
//            val authorities: Collection<GrantedAuthority?> = Arrays.stream(arrayOf(claims!![AUTHORITIES_KEY].toString()))
//                    .map { role: String? -> SimpleGrantedAuthority(role) }
//                    .collect(Collectors.toList())
//            logger.debug("claims subject := [{}]", claims.subject)
//            val principal = User(claims.subject, "", authorities)
//            UsernamePasswordAuthenticationToken(principal, authToken, authorities)
//        } else {
//            throw TokenValidFailedException()
//        }
//    }
//
//    companion object {
//        private const val AUTHORITIES_KEY = "role"
//    }
//}
