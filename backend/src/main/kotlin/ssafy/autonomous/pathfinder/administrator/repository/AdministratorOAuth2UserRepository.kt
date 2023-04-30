package ssafy.autonomous.pathfinder.administrator.repository

import org.springframework.data.jpa.repository.JpaRepository
import ssafy.autonomous.pathfinder.Building.domain.AdministratorOAuth2User
import ssafy.autonomous.pathfinder.facility.domain.Facility
import java.util.*


interface AdministratorOAuth2UserRepository : JpaRepository<AdministratorOAuth2User, Long> {
    fun findByAdministratorOAuth2UserEmail(email : String) : Optional<AdministratorOAuth2User>
}