package ssafy.autonomous.pathfinder.domain.building.service

import org.springframework.stereotype.Service
import ssafy.autonomous.pathfinder.domain.building.dto.response.BuildingNfcResponseDto
import ssafy.autonomous.pathfinder.domain.building.repository.BuildingRepository
import ssafy.autonomous.pathfinder.domain.floors.domain.Floors
import ssafy.autonomous.pathfinder.domain.floors.domain.Beacon
import ssafy.autonomous.pathfinder.domain.floors.repository.BeaconRepository
import ssafy.autonomous.pathfinder.domain.floors.repository.FloorsRepository

@Service
class BuildingServiceImpl(
    private val buildingRepository : BuildingRepository,
    private val floorsRepository: FloorsRepository,
    private val beaconRepository: BeaconRepository
) : BuildingService{

    override fun getBuildingNfc() : BuildingNfcResponseDto {
        // (1) beacon 전체 조회
        val beaconList = getAllBeacons()

        // (2) 층 이미지 List로 담아서 반환
        val floors = getFloors()
        val imageUrlList: List<String>  =  floors.map{
            it.mapImageUrl
        }.toList()
        
        // (3) 빌딩 층 List로 담아서 반환
        val floorsNumberList = floors.map{
            it.floorNumber + "F"
        }.toList()

        return BuildingNfcResponseDto(
            beaconList = beaconList,
            mapImageUrl = imageUrlList,
            floorsNumber = floorsNumberList
        )
    }


    fun getAllBeacons(): List<Beacon>{
        return beaconRepository.findAll()
    }

    fun getFloors(): List<Floors> {
        return floorsRepository.findAll()
    }

    fun getFloorsNumberList(floors: Floors) : List<String>{
        val floorsNumber = floors.floorNumber.toInt()
        val floorsNumberList : MutableList<String> = mutableListOf()

        for(floor in 1..floorsNumber){
            floorsNumberList.add("$floor floor")
        }
        return floorsNumberList
    }
}