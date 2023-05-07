package ssafy.autonomous.pathfinder.domain.administrator.repository

import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin
import org.springframework.data.jpa.repository.JpaRepository
import ssafy.autonomous.pathfinder.domain.administrator.domain.Administrator


interface AdministratorRepository : JpaRepository<Administrator, Long> {
    fun findByEmail(email : String) : Administrator

    fun existsAdministratorByEmail(email: String): Boolean

}