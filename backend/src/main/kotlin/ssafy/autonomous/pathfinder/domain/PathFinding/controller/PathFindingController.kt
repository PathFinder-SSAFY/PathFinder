package ssafy.autonomous.pathfinder.domain.PathFinding.controller

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ssafy.autonomous.pathfinder.domain.PathFinding.dto.Node
import ssafy.autonomous.pathfinder.domain.PathFinding.dto.PathFindingRequest
import ssafy.autonomous.pathfinder.domain.PathFinding.service.PathFindingService


@RestController
@RequestMapping("/findPath")
class PathFindingController(private val pathFindingService: PathFindingService) {

    @PostMapping("/protoType")
    fun findPath(@RequestBody request: PathFindingRequest): List<Node>? {
        val path = pathFindingService.findPath(request.start, request.goal, request.obstacles)
        return path
    }

    @GetMapping
    fun test(): ResponseEntity<Void>{
        return ResponseEntity.ok().build()
    }

}
