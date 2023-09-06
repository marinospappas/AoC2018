package mpdev.springboot.aoc2018.solutions.day11

import java.awt.Point
import kotlin.math.max

class FuelCellGrid(input: List<String>) {

    companion object {
        const val GRID_SIZE = 300
    }

    val serialNumber: Int
    val grid: Array<IntArray>
    val subGridSums: Array<IntArray>    // element x, y is sum of grid elements in sub-grid from 0,0 to x,y

    init {
        serialNumber = input[0].toInt()
        grid = Array(GRID_SIZE) { y -> IntArray(GRID_SIZE) { x -> getPowerLevel(x, y) } }
        updatePowerLevels()
        subGridSums = Array(GRID_SIZE) { y -> IntArray(GRID_SIZE) { x -> grid[y][x] } }
        calculateSubSums()
    }

    // part 1

    fun findHighestPower3x3SubGrid(): Pair<Point,Int> {
        val maxPowerMap = mutableMapOf<Point,Int>()
        (0 until GRID_SIZE-2).forEach { y ->
            (0 until GRID_SIZE-2).forEach { x ->
                maxPowerMap[Point(x,y)] = getSub3x3GridSum(x, y)
            }
        }
        return Pair(maxPowerMap.maxBy { e -> e.value }.key, maxPowerMap.values.max())
    }

    private fun getSub3x3GridSum(x: Int, y: Int) : Int {
        var total = 0
        (0 until 3).forEach { dy ->
            (0 until 3).forEach { dx ->
                total += getPowerLevel(x + dx, y + dy)
            }
        }
        return total
    }

    // part 2

    fun findAnyHighestPowerSubGrid(): Triple<Point,Int,Int> {
        val maxPwrList = mutableMapOf<Triple<Int,Int,Int>,Int>()  // key (x,y), size,  value pwr
        var subSum: Int
        var prevSubSum = 0
        (0 until GRID_SIZE).forEach { y ->
            (0 until GRID_SIZE).forEach { x ->
                sizeLoop@for (size in (1 .. GRID_SIZE - max(x,y))) {
                    subSum = subGridSums[y+size-1][x+size-1]       // sub sum from 0,0 to x+size-1,y+size-1
                    if (x > 0)
                        subSum -= subGridSums[y+size-1][x-1]       // remove sub sum to the left
                    if (y > 0)
                        subSum -= subGridSums[y-1][x+size-1]       // remove sub sum above
                    if (x > 0 && y > 0)
                        subSum += subGridSums[y-1][x-1]            // add sub sum from 0,0 to x-1,y-1 as it has been subtracted twice
                    maxPwrList[Triple(x,y,size)] = subSum
                    if (prevSubSum < 0 && subSum < prevSubSum)     // performance improvement to break the inner loop
                        break@sizeLoop                             // as the sums become negative after a certain size
                    prevSubSum = subSum
                }
            }
        }
        val maxEntry = maxPwrList.maxBy { it.value }
        return Triple(Point(maxEntry.key.first, maxEntry.key.second), maxEntry.key.third, maxEntry.value)
    }

    ////

    private fun updatePowerLevels() {
        (0 until GRID_SIZE).forEach { y ->
            (0 until GRID_SIZE).forEach { x ->
                grid[y][x] = getPowerLevel(x, y)
            }
        }
    }

    private fun calculateSubSums() {
        (1 until GRID_SIZE).forEach { row ->
            (0 until GRID_SIZE).forEach { i ->
                subGridSums[row][i] = subGridSums[row-1][i] + grid[row][i]
            }
        }
        (0 until GRID_SIZE).forEach { i ->
            (1 until GRID_SIZE).forEach { col ->
                subGridSums[i][col] += subGridSums[i][col-1]
            }
        }
    }

    fun getPowerLevel(x: Int, y: Int) : Int {
    /*
    Find the fuel cell's rack ID, which is its X coordinate plus 10.
    Begin with a power level of the rack ID times the Y coordinate.
    Increase the power level by the value of the grid serial number (your puzzle input).
    Set the power level to itself multiplied by the rack ID.
    Keep only the hundreds digit of the power level (so 12345 becomes 3; numbers with no hundreds digit become 0).
    Subtract 5 from the power level.
    */
        val rackId = x + 10
        var level = rackId * y + serialNumber
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
                    val value = grid[y][x]
                    print(if (value >= 0) " " else "")
                    print("$value  ")
                }
            }
            println()
        }
    }
}