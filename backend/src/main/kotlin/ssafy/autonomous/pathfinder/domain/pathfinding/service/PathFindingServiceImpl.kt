package ssafy.autonomous.pathfinder.domain.pathfinding.service

import org.springframework.stereotype.Service
import ssafy.autonomous.pathfinder.domain.facility.dto.response.WallBlindSpotsResponseDto
import ssafy.autonomous.pathfinder.domain.facility.repository.EmergencyEquipmentRepository
import ssafy.autonomous.pathfinder.domain.floors.service.FloorsService
import ssafy.autonomous.pathfinder.domain.pathfinding.dto.Node
import ssafy.autonomous.pathfinder.domain.pathfinding.dto.PathFindDTO
import ssafy.autonomous.pathfinder.domain.pathfinding.dto.Step
import java.lang.Math.abs

@Service
class PathFindingServiceImpl(
    val floorsService: FloorsService,
    val equipmentRepository: EmergencyEquipmentRepository
) : PathFindingService {

    // 경로 제공
    override fun findPath(start: Node, goal: Node): List<Node>? {
        return aStarAlgorithm(start, goal)
    }

    override fun findPath2(start: Node, goal: Node): List<Step>? {
        return reconstructPath(aStarAlgorithm(start, goal), start)
    }

    override fun findHelp(start: Node, help: Int): Node? {
        val helps = equipmentRepository.findByEmergencyType(help)
        var tmp_distance = Double.MAX_VALUE
        var helpNode: Node? = null
        var helpName: String? = null
        for (h in helps) {
            val tmpNode = Node(h.getEmergencyX(), h.getEmergencyY(), h.getEmergencyZ())
            val tmp_dis = abs(start.x - tmpNode.x) + abs(start.z - tmpNode.z)
            if (tmp_dis < tmp_distance) {
                tmp_distance = tmp_dis
                helpNode = tmpNode
                helpName = h.getEmergencyName()
            }
        }
        return helpNode
    }



    // 합친 Api (findPath, findPath2)
    override fun pathFind(start: Node, goal: Node): PathFindDTO? {
        return PathFindDTO(findPath2(start, goal), findPath(start, goal))
    }


    // 왼쪽 오른쪽 판별
    fun getRotation(prevDirection: Int, currentDirection: Int): Int {
        // 1: 북, 2: 동, 3: 남, 4: 서
        return when {
            prevDirection == 1 && currentDirection == 2 -> 5  // Turn 오른쪽
            prevDirection == 2 && currentDirection == 3 -> 5  // Turn 오른쪽
            prevDirection == 3 && currentDirection == 4 -> 5  // Turn 오른쪽
            prevDirection == 4 && currentDirection == 1 -> 5  // Turn 오른쪽
            else -> 6  // Turn 왼쪽
        }
    }


    // astar 알고리즘
    fun aStarAlgorithm(start: Node, goal: Node): List<Node>? {
        val openSet = mutableSetOf(start)
        val closedSet = mutableSetOf<Node>()
        val parentNode = mutableMapOf<Node, Node>()
        val gScore = mutableMapOf<Node, Double>().withDefault { Double.POSITIVE_INFINITY }
        val fScore = mutableMapOf<Node, Double>().withDefault { Double.POSITIVE_INFINITY }
        val obstacles = floorsService.getWallBlindSpots()
        gScore[start] = 0.0
        fScore[start] = start.distance(goal)

        while (openSet.isNotEmpty()) {
            val current = openSet.minByOrNull { fScore.getValue(it) } ?: return null
            if (current == goal) {
                return reconstructPath(parentNode, current)
            }

            openSet.remove(current)
            closedSet.add(current)

            val neighbors = getNeighbors(current, obstacles)
            for (neighbor in neighbors) {
                if (neighbor in closedSet) continue

                val tentativeGScore = gScore.getValue(current) + current.distance(neighbor)
                if (neighbor !in openSet) openSet.add(neighbor)

                if (tentativeGScore < gScore.getValue(neighbor)) {
                    parentNode[neighbor] = current
                    gScore[neighbor] = tentativeGScore
                    fScore[neighbor] = gScore.getValue(neighbor) + neighbor.distance(goal)
                }
            }
        }
        return null
    }

    // 인접 노드 생성
    fun getNeighbors(node: Node, o: List<WallBlindSpotsResponseDto?>): List<Node> {
        return listOf(
            Node(node.x + 0.25, node.y, node.z),
            Node(node.x - 0.25, node.y, node.z),
            Node(node.x, node.y, node.z + 0.25),
            Node(node.x, node.y, node.z - 0.25)
        ).filter { neighbor ->
            o.none {
                it?.leftUpX?.let { leftUpX -> leftUpX <= neighbor.x } ?: false &&
                        it?.rightDownX?.let { rightDownX -> rightDownX >= neighbor.x } ?: false &&
                        it?.rightDownZ?.let { rightDownZ -> rightDownZ <= neighbor.z } ?: false &&
                        it?.leftUpZ?.let { leftUpZ -> leftUpZ >= neighbor.z } ?: false } &&
                    neighbor.x > 0.0 && neighbor.x < 65.85 &&
                    neighbor.z > -12.95 && neighbor.z < 0.0
        }
    }

    // 경로 반환
    fun reconstructPath(cameFrom: Map<Node, Node>, current: Node): List<Node> {
        val path = mutableListOf<Node>()
        var currentVar = current
        while (currentVar in cameFrom) {
            path.add(currentVar)
            currentVar = cameFrom.getValue(currentVar)
        }
        path.add(currentVar)  // Add the start node
        return path.reversed()
    }

    // 방향 좌표 좌회전 우회전 그리기
    fun reconstructPath(path: List<Node>?, start:Node?): List<Step> {
        if (path == null || start == null) return emptyList()
        val steps = mutableListOf<Step>()
        var prevDirection = -1
        var prevDistance = 0.0

        for (i in 0 until path.size - 1) {
            val current = path[i]
            val next = path[i + 1]
            val distance = current.distance(next)
            val direction = when {
                current.z < next.z -> 1 // 북
                current.x < next.x -> 2 // 동
                current.z > next.z -> 3 // 남
                current.x > next.x -> 4 // 서
                else -> null!!
            }

            if (direction == prevDirection) {
                prevDistance += distance
            } else {
                if (prevDirection != -1) {
                    if (steps.size == 0){
                        steps.add(Step(start, prevDistance, prevDirection))
                        steps.add(Step(start, 0.0, getRotation(prevDirection, direction)))
                    } else {
                        steps.add(Step(path[i], prevDistance, prevDirection))
                        steps.add(Step(path[i], 0.0, getRotation(prevDirection, direction)))
                    }
                }
                prevDistance = distance
                prevDirection = direction
            }
        }

        if (prevDirection != -1) {
            val last = path.last()
            val (x, z) = when (prevDirection) {
                1 -> Pair(last.x, last.z - prevDistance)
                2 -> Pair(last.x - prevDistance, last.z)
                3 -> Pair(last.x, last.z + prevDistance)
                else -> Pair(last.x + prevDistance, last.z)
            }
            val lastNode = Node(x, 0.0, z)
            steps.add(Step(lastNode, prevDistance, prevDirection))
        }

        if (steps.size == 2){
            steps.removeAt(1)
        }
        return steps
    }

}


