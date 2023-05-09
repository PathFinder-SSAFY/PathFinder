package ssafy.autonomous.pathfinder.domain.PathFinding.service

import org.springframework.stereotype.Service
import ssafy.autonomous.pathfinder.domain.PathFinding.dto.Node

@Service
class PathFindingServiceImpl : PathFindingService {
    override fun findPath(start: Node, goal: Node, obstacles: List<Node>): List<Node>? {
        return aStarAlgorithm(start, goal, obstacles)
    }
    override fun findHelp(start: Node, help: Int, obstacles: List<Node>): List<Node>? {
        val goal: Node = Node(1.0, 0.0,1.0)
        return aStarAlgorithm(start, goal, obstacles)
    }

    // 층 id 주면(혹은 고유값) 장애물 위치 불러오는 코드도 넣어야 함.

    fun aStarAlgorithm(start: Node, goal: Node, obstacles: List<Node>): List<Node>? {
        val openSet = mutableSetOf(start)
        val closedSet = mutableSetOf<Node>()
        val parentNode = mutableMapOf<Node, Node>()
        val gScore = mutableMapOf<Node, Double>().withDefault { Double.POSITIVE_INFINITY }
        val fScore = mutableMapOf<Node, Double>().withDefault { Double.POSITIVE_INFINITY }

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

    fun getNeighbors(node: Node, obstacles: List<Node>): List<Node> {
        return listOf(
                Node(node.x + 1.0, node.y, node.z),
                Node(node.x - 1.0, node.y, node.z),
                Node(node.x, node.y, node.z + 1.0),
                Node(node.x, node.y, node.z - 1.0),

        ).filter { neighbor ->
            obstacles.none { it.x == neighbor.x && it.z == neighbor.z }
        }
    }

    fun reconstructPath(cameFrom: Map<Node, Node>, current: Node): List<Node> {
        val path = mutableListOf<Node>()
        var currentVar = current
        while (currentVar in cameFrom) {
            path.add(currentVar)
            currentVar = cameFrom.getValue(currentVar)
        }
        return path.reversed()
    }

}

