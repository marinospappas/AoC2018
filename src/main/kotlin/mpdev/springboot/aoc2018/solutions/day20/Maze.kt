package mpdev.springboot.aoc2018.solutions.day20

import mpdev.springboot.aoc2018.utils.Point
import kotlin.math.min
import mpdev.springboot.aoc2018.solutions.day20.Instruction.*
import mpdev.springboot.aoc2018.solutions.day20.Instruction.Companion.LEFT_PAREN
import mpdev.springboot.aoc2018.solutions.day20.Instruction.Companion.RIGHT_PAREN
import mpdev.springboot.aoc2018.solutions.day20.Instruction.Companion.VERT_BAR
import mpdev.springboot.aoc2018.utils.Bfs
import mpdev.springboot.aoc2018.utils.Graph
import java.util.Stack


class Maze(input: List<String>) {

    val directions = input.first().substring(1, input.first().lastIndex)
    val start = Point(0,0)
    val dataMap = mutableMapOf(start to 0)

    fun buildDataMapFromDirections() {
        var current = start
        //val queue = ArrayDeque<Point>()
        val stack = Stack<Point>()
        directions.toCharArray().forEach { c ->
            when (c.toString()) {
                LEFT_PAREN -> stack.push(current)
                RIGHT_PAREN -> current = stack.pop()
                VERT_BAR -> current = stack.peek()
                N.name, E.name, S.name, W.name -> {
                    val next = Instruction.valueOf(c.toString()).move(current)
                    dataMap[next] = min(dataMap[next] ?: (dataMap[current]!!+1), dataMap[current]!! + 1)
                    current = next
                }
            }
        }
    }

    /////////////// solution using Graph and BFS ///////////////////////

    val graph = Graph<Point>()
    private val bfs = Bfs<Point>()
    var distMap = mapOf<Point,Int>()

    fun buildGraphFromDirections() {
        var current = start
        val stack = Stack<Point>()
        graph.addNode(start)
        directions.toCharArray().forEach { c ->
            when (c.toString()) {
                LEFT_PAREN -> stack.push(current)
                RIGHT_PAREN -> current = stack.pop()
                VERT_BAR -> current = stack.peek()
                N.name, E.name, S.name, W.name -> {
                    val next = Instruction.valueOf(c.toString()).move(current)
                    graph.addNode(next)
                    graph.connect(current, next)
                    current = next
                }
            }
        }
    }

    fun calculateMinDistancesToAllNodes() {
        distMap = bfs.findMinDistanceOfAllNodesFromStart(graph[start])
    }

    fun graphToString() = bfs.graphToString(graph, graph[start])
}

enum class Instruction(val move: (Point) -> Point) {
    N({p -> north(p)}),
    S({p -> south(p)}),
    E({p -> east(p)}),
    W({p -> west(p)});
    companion object {
        const val LEFT_PAREN = "("
        const val RIGHT_PAREN = ")"
        const val VERT_BAR = "|"
    }
}

private fun north(p: Point) = Point(p.x,p.y+1)
private fun south(p: Point) = Point(p.x,p.y-1)
private fun east(p: Point) = Point(p.x+1,p.y)
private fun west(p: Point) = Point(p.x-1,p.y)
