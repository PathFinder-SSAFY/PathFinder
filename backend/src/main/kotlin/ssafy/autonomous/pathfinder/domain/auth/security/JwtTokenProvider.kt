package ssafy.autonomous.pathfinder.domain.auth.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import ssafy.autonomous.pathfinder.domain.auth.domain.AdministratorPrincipal
import java.lang.Exception
import java.nio.file.attribute.UserPrincipal
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${security.jwt.secret}")
    val secretKey: String,

    @Value("\${security.jwt.token.expire-length}")
    val validityInMilliseconds: Long,

) {

    // token 생성
    fun createToken(authentication: Authentication?): String {
        return Jwts.builder().let {
            val now = Date()
            val administratorPrincipal = authentication?.principal as AdministratorPrincipal

            it.setClaims(
                Jwts.claims()
                    .setSubject(administratorPrincipal.getId().toString())
                    .also{ claims -> claims["role"] = administratorPrincipal.authorities.first() }
            )
                .setIssuedAt(now)
                .setExpiration(Date(now.time + validityInMilliseconds))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact()
        }
    }

    // token으로 부터 user Id 발급
    fun getUserIdFromToken(token: String?): Long {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .body.subject
            .toLong()
    }


    // token 검증
    fun validateToken(token: String?): Boolean {
        return try {
            Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .let { !it.body.expiration.before(Date())}
        } catch ( e: Exception){
            false
        }
    }

}