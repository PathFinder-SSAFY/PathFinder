package ssafy.autonomous.pathfinder.domain.administrator.repository

import org.springframework.data.jpa.repository.JpaRepository
import ssafy.autonomous.pathfinder.Building.domain.AdministratorOAuth2User
import java.util.*


interface AdministratorOAuth2UserRepository : JpaRepository<AdministratorOAuth2User, Long> {
    fun findByEmail(email : String) : Optional<AdministratorOAuth2User>

}