package ssafy.autonomous.pathfinder.domain.administrator.repository

import org.springframework.data.jpa.repository.JpaRepository
import ssafy.autonomous.pathfinder.domain.administrator.domain.Administrator
import java.util.*


interface AdministratorRepository : JpaRepository<Administrator, Long> {
    fun findByEmail(email : String) : Administrator

}