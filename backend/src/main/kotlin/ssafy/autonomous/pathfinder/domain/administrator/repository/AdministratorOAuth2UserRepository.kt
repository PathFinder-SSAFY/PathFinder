package ssafy.autonomous.pathfinder.domain.administrator.repository

import org.springframework.data.jpa.repository.JpaRepository
import ssafy.autonomous.pathfinder.Building.domain.AdministratorOAuth2User
import ssafy.autonomous.pathfinder.domain.facility.domain.Facility
import java.util.*


interface AdministratorOAuth2UserRepository : JpaRepository<AdministratorOAuth2User, Long> {
    fun findByAdministratorOAuth2UserEmail(email : String) : Optional<AdministratorOAuth2User>

}