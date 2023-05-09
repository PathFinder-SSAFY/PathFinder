package ssafy.autonomous.pathfinder.domain.PathFinding.dto

import kotlin.math.abs
import kotlin.math.sqrt

data class Node(val x: Double, val y: Double) {
    fun distance(other: Node): Double {
        val dx = abs(x - other.x)
        val dy = abs(y - other.y)
        return dx + dy
    }
}