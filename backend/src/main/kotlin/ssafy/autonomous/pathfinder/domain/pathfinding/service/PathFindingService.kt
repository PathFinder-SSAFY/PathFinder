package ssafy.autonomous.pathfinder.domain.pathfinding.service

import ssafy.autonomous.pathfinder.domain.pathfinding.dto.Node
import ssafy.autonomous.pathfinder.domain.pathfinding.dto.PathFindDTO
import ssafy.autonomous.pathfinder.domain.pathfinding.dto.Step

interface PathFindingService {
    fun findPath(start: Node, goal: Node): List<Node>?

    fun findHelp(start: Node, help: Int): List<Node>?

    fun findPath2(start: Node, goal: Node): List<Step>?

    fun pathFind(start: Node, goal: Node): PathFindDTO?
}