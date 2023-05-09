package ssafy.autonomous.pathfinder.domain.PathFinding.dto

data class PathFindingRequest(val start: Node, val goal: Node, val obstacles: List<Node>)

