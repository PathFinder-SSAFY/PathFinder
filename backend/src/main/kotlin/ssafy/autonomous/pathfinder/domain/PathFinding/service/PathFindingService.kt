package ssafy.autonomous.pathfinder.domain.PathFinding.service

import ssafy.autonomous.pathfinder.domain.PathFinding.dto.NodeDto

interface PathFindingService {
    fun findPath(start: NodeDto, goal: NodeDto, obstacles: List<NodeDto>): List<NodeDto>?
}