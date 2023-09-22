package mpdev.springboot.aoc2018.solutions.day18

import mpdev.springboot.aoc2018.utils.Grid
import mpdev.springboot.aoc2018.utils.Point

class Woods(input: List<String>) {

    var grid = Grid(input, Plot.mapper, border = 0)

    fun executeStateTransition() {
        val newGridData = mutableMapOf<Point,Plot>()
        val (minX,maxX,minY,maxY) = grid.getMinMaxXY()
        (minX..maxX).forEach { x ->
            (minY..maxY).forEach { y ->
                val point = Point(x,y)
                val (woods, lumber) = adjacentCount(point)
                when (grid.getDataPoint(point)) {
                    Plot.WOOD ->
                        if (lumber >= 3)
                            newGridData[point] = Plot.LUMBER
                        else
                            newGridData[point] = Plot.WOOD
                    Plot.LUMBER -> if (lumber >= 1 && woods >= 1)
                                        newGridData[point] = Plot.LUMBER
                    else -> if (woods >= 3)
                                newGridData[point] = Plot.WOOD
                }
            }
        }
        grid = Grid(newGridData, Plot.mapper, border = 0)
    }

    fun adjacentCount(point: Point): Pair<Int,Int> {
        var countWoods = 0
        var countLumber = 0
        point.adjacent().forEach { p ->
           val data = grid.getDataPoint(p)
            when (data) {
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