package ssafy.autonomous.pathfinder.domain.building.repository

import org.springframework.data.jpa.repository.JpaRepository
import ssafy.autonomous.pathfinder.domain.building.domain.Customer

interface CustomerRepository : JpaRepository<Customer, Int>