package ssafy.autonomous.pathfinder.domain.pathfinding.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ssafy.autonomous.pathfinder.domain.pathfinding.dto.*
import ssafy.autonomous.pathfinder.domain.pathfinding.service.PathFindingService

/*
*
* 6.
*
* */
@RestController
@RequestMapping("/findPath")
class PathFindingController(private val pathFindingService: PathFindingService) {

    @PostMapping("/protoType")
    fun findPath(@RequestBody request: PathFindingRequest): List<Node>? {
        val path = pathFindingService.findPath(request.start, request.goal)
        return path
    }

    @PostMapping("/findHelp")
    fun findHelp(@RequestBody request: PathFindingHelp): Node? {
        val path = pathFindingService.findHelp(request.start, request.help)
        return path
    }

    @PostMapping("/protoType3")
    fun findPath2(@RequestBody request: PathFindingRequest): List<Step>? {
        val step = pathFindingService.findPath2(request.start, request.goal)
        return step
    }
    @GetMapping
    fun test(): ResponseEntity<Void>{
        return ResponseEntity.ok().build()
    }

    @PostMapping
    fun pathFind(@RequestBody request: PathFindingRequest): PathFindDTO? {
        val path = pathFindingService.pathFind(request.start, request.goal)
        return path
    }
}
