package mpdev.springboot.aoc2018.solutions.day11

import java.awt.Point

class FuelCellGrid(input: List<String>) {

    companion object {
        const val GRID_SIZE = 300
    }

    val serialNumber: Int
    val grid: Array<IntArray>

    init {
        serialNumber = input[0].toInt()
        grid = Array(GRID_SIZE){ IntArray(GRID_SIZE){ 0 } }
        updatePowerLevels()
    }

    fun findHighestPowerSubGrid(): Pair<Point,Int> {
        val totalsMap = mutableMapOf<Point,Int>()
        (0 until GRID_SIZE).forEach { y ->
            (0 until GRID_SIZE).forEach { x ->
                totalsMap[Point(x,y)] = get3x3Sum(Point(x,y))
            }
        }
        return Pair(totalsMap.maxBy { entry -> entry.value }.key, totalsMap.values.max())
    }

    fun get3x3Sum(p: Point): Int {
        if (p.x >= GRID_SIZE-2 || p.y >= GRID_SIZE-2)
            return 0
        var total = 0
        (0 until 3).forEach { dy ->
            (0 until 3).forEach { dx ->
                total += getPowerLevel(Point(p.x + dx, p.y + dy))
            }
        }
        return total
    }

    fun updatePowerLevels() {
        (0 until GRID_SIZE).forEach { y ->
            (0 until GRID_SIZE).forEach { x ->
                grid[y][x] = getPowerLevel(Point(x,y))
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

    fun printSubgrid(p: Point) {
        (p.y-1 .. p.y+3).forEach { y ->
            (p.x-1 .. p.x+3).forEach { x ->
                if (x < GRID_SIZE && y < GRID_SIZE) {
                    val value = grid[y][x]
                    if (value >= 0)
                        print(" ")
                    print("$value  ")
                }
            }
            println()
        }
    }
}