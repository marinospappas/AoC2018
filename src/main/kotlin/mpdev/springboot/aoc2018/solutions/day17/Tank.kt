package mpdev.springboot.aoc2018.solutions.day17

import mpdev.springboot.aoc2018.utils.AocException
import mpdev.springboot.aoc2018.utils.Grid
import mpdev.springboot.aoc2018.utils.Point

class Tank(input: List<String>) {

    val spring = Point(500,0)
    val grid: Grid<TankData> = processInput(input)

   /* fun fillTanks(start: Point) {
        var overflowPts: List<Point> = emptyList()
        while (overflowPts.isEmpty()) {
            overflowPts = waterDrops(start)
        }
        this.print(); readln()
        overflowPts.forEach { newStart -> if (newStart.x >= 0) fillTanks(newStart) }
    }*/

    fun fillTanks() {
        val queue = ArrayDeque<Point>()
        queue.add(spring)
        while (queue.isNotEmpty()) {
            var overflowPts = emptyList<Point>()
            val start = queue.removeFirst()
            while (overflowPts.isEmpty())
                overflowPts = waterDrops(start)
            queue.addAll(overflowPts.filter { it.x >= 0 })
        }
    }

    // follows water dropping, fills tank level and returns the overflow points(s) if tank full
    fun waterDrops(fromPoint: Point): List<Point> {
        val (_,_,_, maxY) = grid.getMinMaxXY()
        val gridData = grid.getDataPoints()
        var curPoint = fromPoint
        // if start point is underwater move it above water - to support tank inside tank
        while (gridData[curPoint] == TankData.WATER && curPoint.y > 0)
            curPoint = curPoint.above()
        val overflowPoints = mutableListOf<Point>()
        while (curPoint.y < maxY) {
            val nextDataPoint = gridData[curPoint.below()]
            if (nextDataPoint == null && curPoint.below().y < maxY) {
                grid.setDataPoint(curPoint.below(), TankData.DRIED)
                return emptyList()
            }
            if (gridData[curPoint] == null && nextDataPoint == TankData.DRIED) {
                grid.setDataPoint(curPoint, TankData.DRIED)     // water has reached here already - do nothing
                return emptyList()
            }
            if (nextDataPoint == TankData.WALL || nextDataPoint == TankData.WATER) {
                val (isWallLeft, leftPoint) = wallLeft(curPoint)
                val (isWallRight, rightPoint) = wallRight(curPoint)
                if (isWallLeft && isWallRight) {
                    // fill tank
                    fillLevel(leftPoint, rightPoint, TankData.WATER)
                    return emptyList()
                }
                else {
                    // overflow
                    grid.setDataPoint(curPoint, TankData.DRIED)
                    if (!isWallLeft) {
                        overflowPoints.add(leftPoint)
                        grid.setDataPoint(leftPoint, TankData.DRIED)
                    }
                    if (!isWallRight) {
                        overflowPoints.add(rightPoint)
                        grid.setDataPoint(rightPoint, TankData.DRIED)
                    }
                    fillLevel(leftPoint, rightPoint, TankData.DRIED)
                    return overflowPoints
                }
            }
            curPoint = curPoint.below()
        }
        return listOf(Point(-1,-1))
    }

    private fun fillLevel(left: Point, right: Point, fill: TankData) {
        (left.x .. right.x).forEach { x -> grid.setDataPoint(Point(x, left.y), fill) }
    }

    // if there is a wall to the left, return the point to the left where the water ends
    // otherwise return the point to the left where the overflow starts
    private fun wallLeft(point: Point): Pair<Boolean,Point> {
        val minX = grid.getMinMaxXY().x1
        val gridData = grid.getDataPoints()
        var curPoint = point
        while (curPoint.x >= minX) {
            if (gridData[curPoint.below()] != TankData.WALL && gridData[curPoint.below()] != TankData.WATER)
                return Pair(false, curPoint)
            if (gridData[curPoint.left()] == TankData.WALL)
                return Pair(true, curPoint)
            curPoint = curPoint.left()
        }
        return Pair(false,Point(-1,-1))
    }

    // if there is a wall to the right, return the point to the right where the water ends
    // otherwise return the point to the right where the overflow starts
    private fun wallRight(point: Point): Pair<Boolean,Point> {
        val maxX = grid.getMinMaxXY().x2
        val gridData = grid.getDataPoints()
        var curPoint = point
        while (curPoint.x <= maxX) {
            if (gridData[curPoint.below()] != TankData.WALL && gridData[curPoint.below()] != TankData.WATER)
                return Pair(false, curPoint)
            if (gridData[curPoint.right()] == TankData.WALL)
                return Pair(true, curPoint)
            curPoint = curPoint.right()
        }
        return Pair(false,Point(-1,-1))
    }

    fun print() {
        grid.print()
    }

    private fun Point.above() = Point(x,y-1)
    private fun Point.below() = Point(x,y+1)
    private fun Point.left() = Point(x-1,y)
    private fun Point.right() = Point(x+1,y)

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