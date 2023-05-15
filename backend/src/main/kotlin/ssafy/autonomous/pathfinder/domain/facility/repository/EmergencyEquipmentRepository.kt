package ssafy.autonomous.pathfinder.domain.facility.repository

import org.springframework.data.jpa.repository.JpaRepository
import ssafy.autonomous.pathfinder.domain.facility.domain.EmergencyEquipment



interface EmergencyEquipmentRepository: JpaRepository<EmergencyEquipment, Int> {
    fun findByEmergencyType(emergencyType: Int): List<EmergencyEquipment>
}
