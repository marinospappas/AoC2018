package mpdev.springboot.aoc2018.solutions.day20

import mpdev.springboot.aoc2018.utils.Graph
import mpdev.springboot.aoc2018.utils.GraphNode
import mpdev.springboot.aoc2018.utils.Point
import java.util.Stack

class Maze(input: List<String>) {

    val directions = input.first().substring(1, input.first().lastIndex)
    val start = Point(0,0)
    val graph = Graph<Point>().also { it.addNode(start) }

    fun buildGraphFromDirections() {
        var current = graph[start]
        val stack = Stack<Point>()
        directions.toCharArray().forEach { c ->
            if (Instruction.allValues.contains(c)) {
                val nextNodeId = Instruction.valueOf(c.toString()).move(current.nodeId)
                if (graph.getOrNull(nextNodeId) == null)
                    graph.addNode(nextNodeId)
                graph.connectBothWays(current.nodeId, nextNodeId)
            }
            else when (c) {
                '(' -> stack.push(current.nodeId)
                '|' -> current = GraphNode(stack.pop())
                ')' -> {}
            }
        }

    }
}

enum class Instruction(val move: (Point) -> Point) {
    N({p -> north(p)}),
    S({p -> south(p)}),
    E({p -> east(p)}),
    W({p -> west(p)});
    companion object {
        val allValues = values().map{ it.name.first() }
    }
}

private fun north(p: Point) = Point(p.x,p.y+1)
private fun south(p: Point) = Point(p.x,p.y-1)
private fun east(p: Point) = Point(p.x+1,p.y)
private fun west(p: Point) = Point(p.x-1,p.y)
