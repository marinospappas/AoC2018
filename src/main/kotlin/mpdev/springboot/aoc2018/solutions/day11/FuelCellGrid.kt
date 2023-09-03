package mpdev.springboot.aoc2018.solutions.day11

import mpdev.springboot.aoc2018.utils.AocException
import java.awt.Point

class FuelCellGrid(input: List<String>) {

    companion object {
        const val GRID_SIZE = 300
    }

    val serialNumber: Int
    val grid = mutableMapOf<Point,Int>()

    init {
        serialNumber = input[0].toInt()
        updatePowerLevels()
    }

    fun findHighestPowerSubGrid(size: Int = 3) =
        grid.entries.groupingBy { it.key }
            .aggregate { _, _: Int?, e, _ -> getSubGridSum(e.key, size) }
            .entries.maxBy { it.value }

    fun findAnyHighestPowerSubGrid(): Triple<Point,Int,Int> {
        val maxPwrList = mutableListOf<Triple<Point,Int,Int>>()
        for (size in (1 .. GRID_SIZE)) {
            val (coord, pwr) = findHighestPowerSubGrid(size)
            // stop if the maxPwer decreases twice in a row
            if (maxPwrList.size > 1 &&
                pwr < maxPwrList.last().third && maxPwrList.last().third < maxPwrList[maxPwrList.lastIndex-1].third)
                break
            maxPwrList.add(Triple(coord, size, pwr))
        }
        return maxPwrList.maxBy { it.third }
    }

    private fun getSubGridSum(p: Point, size: Int = 3) : Int {
        var total = 0
        (0 until size).forEach { dy ->
            (0 until size).forEach { dx ->
                total += getPowerLevel(Point(p.x + dx, p.y + dy))
            }
        }
        return total
    }

    private fun updatePowerLevels() {
        (0 until GRID_SIZE).forEach { y ->
            (0 until GRID_SIZE).forEach { x ->
                grid[Point(x,y)] = getPowerLevel(Point(x,y))
            }
        }
    }

    fun getPowerLevel(p: Point) : Int {
    /*
    Find the fuel cell's rack ID, which is its X coordinate plus 10.
    Begin with a power level of the rack ID times the Y coordinate.
    Increase the power level by the value of the grid serial number (your puzzle input).
    Set the power level to itself multiplied by the rack ID.
    Keep only the hundreds digit of the power level (so 12345 becomes 3; numbers with no hundreds digit become 0).
    Subtract 5 from the power level.
    */
        val rackId = p.x + 10
        var level = rackId * p.y + serialNumber
        level *= rackId
        return (level % 1000) / 100 - 5
    }

    fun printGrid() {
        printSubgrid(Point(0,0), GRID_SIZE)
    }

    fun printSubgrid(p: Point, size: Int = 3) {
        val x0 = if (p.x == 0) 0 else p.x - 1
        val y0 = if (p.y == 0) 0 else p.y - 1
        val x1 = if (p.x+size > GRID_SIZE) GRID_SIZE-1 else p.x + size
        val y1 = if (p.y+size > GRID_SIZE) GRID_SIZE-1 else p.y + size
        (y0 .. y1).forEach { y ->
            (x0 .. x1).forEach { x ->
                if (x < GRID_SIZE && y < GRID_SIZE) {
                    val value = grid[Point(x,y)] ?: throw AocException("no value for point $x,$y")
                    print(if (value >= 0) " " else "")
                    print("$value  ")
                }
            }
            println()
        }
    }
}