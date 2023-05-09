package ssafy.autonomous.pathfinder.domain.pathfinding.dto

import kotlin.math.abs

data class Node(val x: Double, val y: Double, val z: Double) {
    fun distance(other: Node): Double {
        val dx = abs(x - other.x)
        val dz = abs(z - other.z)
        return dx + dz
    }
    fun highDistance(other: Node): Double {
        return abs(y - other.y)
    }
}