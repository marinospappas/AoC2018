package mpdev.springboot.aoc2018.solutions.day17

import mpdev.springboot.aoc2018.utils.AocException
import mpdev.springboot.aoc2018.utils.Grid
import mpdev.springboot.aoc2018.utils.Point

class Tank(input: List<String>) {

    val spring = Point(500,0)
    val grid: Grid<TankData> = processInput(input)

    fun waterDrops(fromPoint: Point): Point {
        val (_,_,_, maxY) = grid.getMinMaxXY()
        val gridData = grid.getDataPoints()
        var y = fromPoint.y
        while (y < maxY) {
            val nextDataPoint = gridData[Point(fromPoint.x, ++y)]
            if (nextDataPoint == null || nextDataPoint == TankData.DRIED)
                grid.setDataPoint(Point(fromPoint.x, y), TankData.DRIED)
            else if (nextDataPoint == TankData.WALL) {
                grid.setDataPoint(Point(fromPoint.x, y-1), TankData.DRIED)
                return Point(fromPoint.x, y-1)
            }
        }
        return Point(-1,-1)
    }

    fun print() {
        grid.print()
    }

    fun processInput(input: List<String>): Grid<TankData> {
        val data = mutableMapOf<Point,TankData>()
        input.forEach { line ->
            try {
                if (line.startsWith('x')) {
                    // x=495, y=2..7
                    val match = Regex("""x=(\d+), y=(\d+)\.\.(\d+)""").find(line)
                    val (x, y1, y2) = match!!.destructured
                    (y1.toInt()..y2.toInt()).forEach { y -> data[Point(x.toInt(), y)] = TankData.WALL }
                } else if (line.startsWith('y')) {
                    // y=7, x=495..501
                    val match = Regex("""y=(\d+), x=(\d+)\.\.(\d+)""").find(line)
                    val (y, x1, x2) = match!!.destructured
                    (x1.toInt()..x2.toInt()).forEach { x -> data[Point(x, y.toInt())] = TankData.WALL }
                }
                else
                    throw RuntimeException()
            }
            catch (e: Exception) {
                throw AocException("invalid line: $line")
            }
        }
        data[spring] = TankData.SPRING
        return Grid(data, TankData.mapper)
    }
}

enum class TankData(val value: Char) {
    SPRING('+'),
    WALL('#'),
    WATER('~'),
    DRIED('|');
    companion object {
        val mapper: Map<Char,TankData> = values().associateBy { it.value }
    }
}