package mpdev.springboot.aoc2018.solutions.day18

import mpdev.springboot.aoc2018.utils.Grid
import mpdev.springboot.aoc2018.utils.Point

class Woods(input: List<String>, private val gridSize: Pair<Int,Int>) {

    var grid = Grid(input, Plot.mapper, border = 0, defaultSize = gridSize)
    private val stateCache = mutableMapOf<Map<Point,Plot>,Int>()

    fun executeStateTransition() {
        val newGridData = mutableMapOf<Point,Plot>()
        val (minX,maxX,minY,maxY) = grid.getMinMaxXY()
        (minX..maxX).forEach { x ->
            (minY..maxY).forEach { y ->
                val point = Point(x,y)
                val (woods, lumber) = adjacentCount(point)
                val newDataPoint = when(grid.getDataPoint(point)) {
                    Plot.WOOD -> if (lumber >= 3) Plot.LUMBER else Plot.WOOD
                    Plot.LUMBER -> if (lumber >= 1 && woods >= 1) Plot.LUMBER else null
                    else -> if (woods >= 3) Plot.WOOD else null
                }
                if (newDataPoint != null)
                    newGridData[point] = newDataPoint
            }
        }
        grid = Grid(newGridData, Plot.mapper, border = 0, defaultSize = gridSize)
    }

    fun findStatePeriod(): Pair<Int,Int> {
        stateCache[grid.getDataPoints()] = 0
        for(t in 1..10000) {
            executeStateTransition()
            val t0 = stateCache.getOrPut(grid.getDataPoints()){ t }
            if (t0 < t)     // found period
                return Pair(t-t0, t0)   // a * n + b
        }
        return Pair(-1,-1)   // no period found
    }

    private fun adjacentCount(point: Point): Pair<Int,Int> {
        var countWoods = 0
        var countLumber = 0
        point.adjacent().forEach { p ->
           when (grid.getDataPoint(p)) {
                Plot.WOOD -> ++countWoods
                Plot.LUMBER -> ++countLumber
                else -> {}
            }
        }
        return Pair(countWoods, countLumber)
    }

    fun print() {
        grid.print()
    }
}

enum class Plot(val value: Char) {
    WOOD('|'),
    LUMBER('#');
    companion object {
        val mapper: Map<Char,Plot> = values().associateBy { it.value }
    }
}