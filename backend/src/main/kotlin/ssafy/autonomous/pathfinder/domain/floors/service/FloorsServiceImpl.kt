package ssafy.autonomous.pathfinder.domain.floors.service

import ssafy.autonomous.pathfinder.domain.floors.domain.Beacon
import ssafy.autonomous.pathfinder.domain.floors.repository.BeaconRepository

class FloorsServiceImpl(
    private val beaconRepository: BeaconRepository
) : FloorsService {
    override fun getBeaconList(): List<Beacon> {
        return beaconRepository.findAll()
    }
}