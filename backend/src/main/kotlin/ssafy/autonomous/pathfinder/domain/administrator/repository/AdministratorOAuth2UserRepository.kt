package ssafy.autonomous.pathfinder.domain.administrator.repository

import org.springframework.data.jpa.repository.JpaRepository
import ssafy.autonomous.pathfinder.domain.administrator.domain.AdministratorOAuth2User
import java.util.*


interface AdministratorOAuth2UserRepository : JpaRepository<AdministratorOAuth2User, Long> {
    fun findByEmail(email : String) : AdministratorOAuth2User

}