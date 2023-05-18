package ssafy.autonomous.pathfinder.domain.pathfinding.service

import ssafy.autonomous.pathfinder.domain.pathfinding.dto.*

interface PathFindingService {
    fun findPath(start: Node, goal: Node): List<Node>?
    fun findPathFacility(request: PathFindingFacility): PathFindDTO?
    fun findHelp(start: Node, help: Int): Node?

    fun findPath2(start: Node, goal: Node): List<Step>?

    fun pathFind(start: Node, goal: Node): PathFindDTO?
}