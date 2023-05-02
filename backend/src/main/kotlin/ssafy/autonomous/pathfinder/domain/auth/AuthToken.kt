//package ssafy.autonomous.pathfinder.domain.auth
//
//import io.jsonwebtoken.*
//import mu.KotlinLogging
//import java.security.Key
//import java.util.*
//
//
//
//
//class AuthToken {
//    private val token: String
//    private val key: Key
//
//    private val logger = KotlinLogging.logger{}
//
//    internal constructor(token: String, key: Key) {
//        this.key = key
//        this.token = token
//    }
//
//    internal constructor(id: String, expiry: Date, key: Key) {
//        this.key = key
//        token = createAuthToken(id, expiry)
//    }
//
//    internal constructor(id: String, role: String, expiry: Date, key: Key) {
//        this.key = key
//        token = createAuthToken(id, role, expiry)
//    }
//
//    private fun createAuthToken(id: String, expiry: Date): String {
//        return Jwts.builder()
//                .setSubject(id)
//                .signWith(key, SignatureAlgorithm.HS256)
//                .setExpiration(expiry)
//                .compact()
//    }
//
//    private fun createAuthToken(id: String, role: String, expiry: Date): String {
//        return Jwts.builder()
//                .setSubject(id)
//                .claim(AUTHORITIES_KEY, role)
//                .signWith(key, SignatureAlgorithm.HS256)
//                .setExpiration(expiry)
//                .compact()
//    }
//
//    fun validate(): Boolean {
//        return tokenClaims != null
//    }
//
//    val tokenClaims: Claims?
//        get() {
//            try {
//                return Jwts.parserBuilder()
//                        .setSigningKey(key)
//                        .build()
//                        .parseClaimsJws(token)
//                        .body
//            } catch (e: SecurityException) {
//                logger.info("Invalid JWT signature.")
//            } catch (e: MalformedJwtException) {
//                logger.info("Invalid JWT token.")
//            } catch (e: ExpiredJwtException) {
//                logger.info("Expired JWT token.")
//            } catch (e: UnsupportedJwtException) {
//                logger.info("Unsupported JWT token.")
//            } catch (e: IllegalArgumentException) {
//                logger.info("JWT token compact of handler are invalid.")
//            }
//            return null
//        }
//    val expiredTokenClaims: Claims?
//        get() {
//            try {
//                Jwts.parserBuilder()
//                        .setSigningKey(key)
//                        .build()
//                        .parseClaimsJws(token)
//                        .body
//            } catch (e: ExpiredJwtException) {
//                logger.info("Expired JWT token.")
//                return e.claims
//            }
//            return null
//        }
//
//    companion object {
//        private const val AUTHORITIES_KEY = "role"
//    }
//}
