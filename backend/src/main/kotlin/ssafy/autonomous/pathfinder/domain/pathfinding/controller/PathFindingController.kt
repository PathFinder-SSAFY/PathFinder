package ssafy.autonomous.pathfinder.domain.pathfinding.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ssafy.autonomous.pathfinder.domain.pathfinding.dto.Node
import ssafy.autonomous.pathfinder.domain.pathfinding.dto.PathFindingHelp
import ssafy.autonomous.pathfinder.domain.pathfinding.dto.PathFindingRequest
import ssafy.autonomous.pathfinder.domain.pathfinding.service.PathFindingService


@RestController
@RequestMapping("/findPath")
class PathFindingController(private val pathFindingService: PathFindingService) {

    @PostMapping("/protoType")
    fun findPath(@RequestBody request: PathFindingRequest): List<Node>? {
        val path = pathFindingService.findPath(request.start, request.goal, request.obstacles)
        return path
    }

    @PostMapping("/protoType2")
    fun findHelp(@RequestBody request: PathFindingHelp): List<Node>? {
        val path = pathFindingService.findHelp(request.start, request.help, request.obstacles)
        return path
    }

    @GetMapping
    fun test(): ResponseEntity<Void>{
        return ResponseEntity.ok().build()
    }

}
