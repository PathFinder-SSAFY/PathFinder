package ssafy.autonomous.pathfinder.domain.PathFinding.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ssafy.autonomous.pathfinder.domain.PathFinding.dto.Node
import ssafy.autonomous.pathfinder.domain.PathFinding.dto.PathFindingRequest
import ssafy.autonomous.pathfinder.domain.PathFinding.service.PathFindingService


@RestController("/findPath")
class PathFindingController(private val pathFindingService: PathFindingService) {

    @PostMapping("/v1")
    fun findPath(@RequestBody request: PathFindingRequest): List<Node>? {
        val path = pathFindingService.findPath(request.start, request.goal, request.obstacles)
        return path
    }
}
