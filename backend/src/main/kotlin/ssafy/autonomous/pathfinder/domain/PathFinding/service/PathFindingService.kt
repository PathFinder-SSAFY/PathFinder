package ssafy.autonomous.pathfinder.domain.PathFinding.service

import ssafy.autonomous.pathfinder.domain.PathFinding.dto.Node

interface PathFindingService {
    fun findPath(start: Node, goal: Node, obstacles: List<Node>): List<Node>?

    fun findHelp(start: Node, help: Int, obstacles: List<Node>): List<Node>?
}