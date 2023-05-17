package ssafy.autonomous.pathfinder.domain.pathfinding.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ssafy.autonomous.pathfinder.domain.pathfinding.dto.Node
import com.google.gson.Gson
import mu.KotlinLogging
import org.junit.jupiter.api.Assertions

@SpringBootTest
class PathFindingServiceImplTest @Autowired constructor(
    private val pathFindingService: PathFindingService
) {

    private val logger = KotlinLogging.logger{}


    @Test
    @DisplayName("A star 경로 탐색")
    fun A_star를_이용해_경로_탐색() {
        // given
        val start: Node = Node(1.0, 0.0, -2.25)
        val goal: Node = Node(3.00, 0.0, -3.0)

        val result = listOf(
            Node(1.0, 0.0, -2.25),
            Node(1.25, 0.0, -2.25),
            Node(1.5, 0.0, -2.25),
            Node(1.75, 0.0, -2.25),
            Node(2.0, 0.0, -2.25),
            Node(2.25, 0.0, -2.25),
            Node(2.5, 0.0, -2.25),
            Node(2.75, 0.0, -2.25),
            Node(3.0, 0.0, -2.25),
            Node(3.0, 0.0, -2.5),
            Node(3.0, 0.0, -2.75),
            Node(3.0, 0.0, -3.0)
        )

        // when
        val result2 = pathFindingService.findPath(start, goal)

        // then
        Assertions.assertEquals(result, result2)
    }
}