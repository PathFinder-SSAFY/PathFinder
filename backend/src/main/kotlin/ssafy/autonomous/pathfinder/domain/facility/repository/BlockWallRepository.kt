package ssafy.autonomous.pathfinder.domain.facility.repository

import org.springframework.data.jpa.repository.JpaRepository
import ssafy.autonomous.pathfinder.domain.facility.domain.BlockWall

interface BlockWallRepository : JpaRepository<BlockWall, Int>