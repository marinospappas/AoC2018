package mpdev.springboot.aoc2018.solutions.day06

import mpdev.springboot.aoc2018.utils.AocException
import mpdev.springboot.aoc2018.utils.Grid
import mpdev.springboot.aoc2018.utils.manhattan
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.awt.Point

class Coordinates(input: List<String>) {

    companion object {
        const val DATA_VALUE = -1
        val mapper = mapOf('#' to DATA_VALUE)
    }

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    var grid = Grid(input.toSet(), mapper)
    private val datapoints = grid.getDataPoints().keys.toList()
    val coordIndex = datapoints.groupingBy { it }.aggregate { key, _: Int?, _, _ ->  datapoints.indexOf(key)  }
    var reqDistance = 10_000

    fun findClosestPointsForEachCoord() {
        val (minX, maxX, minY, maxY) = grid.getMinMaxXY()
        (minX..maxX).forEach { i ->
            (minY..maxY).forEach { j ->
                val thisPoint = Point(i, j)
                var minDistance = Int.MAX_VALUE
                var minDistPoint = Point(-1, -1)
                var includeThis = false
                for (p in datapoints) {
                    val thisDistance = thisPoint.manhattan(p)
                    if (thisDistance < minDistance) {
                        includeThis = true
                        minDistance = thisDistance
                        minDistPoint = p
                    } else if (thisDistance == minDistance)
                        includeThis = false
                }
                if (includeThis && minDistance > 0)
                    grid.setDataPoint(thisPoint, coordIndex[minDistPoint]
                                ?: throw AocException("could not get index for point $minDistPoint"))
            }
        }
    }

    fun getNonInfiniteAreas(): List<Int> {
        val (minX, maxX, minY, maxY) = grid.getMinMaxXY()
        val infiniteValues = grid.getDataPoints()
            .filter { it.key.x == minX || it.key.x == maxX || it.key.y == minY || it.key.y == maxY }
            .map { it.value }
            .toSet()
        log.info("infinite values {}", infiniteValues)
        return grid.getDataPoints()
            .filter { it.value != DATA_VALUE }
            .filter { !infiniteValues.contains(it.value) }
            .values
            .groupBy { it }
            .map { it.value.size }
    }

    fun getAreaWithinReqDistance(): Set<Point> {
        val result = mutableSetOf<Point>()
        val (minX, maxX, minY, maxY) = grid.getMinMaxXY()
        (minX..maxX).forEach { i ->
            (minY..maxY).forEach { j ->
                val thisPoint = Point(i, j)
                if (sumOfDistances(thisPoint) < reqDistance)
                    result.add(thisPoint)
            }
        }
        return result
    }

    private fun sumOfDistances(p: Point) = datapoints.sumOf { it.manhattan(p) }

}