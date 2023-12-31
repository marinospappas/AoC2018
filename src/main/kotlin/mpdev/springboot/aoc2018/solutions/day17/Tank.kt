package mpdev.springboot.aoc2018.solutions.day17

import mpdev.springboot.aoc2018.utils.AocException
import mpdev.springboot.aoc2018.utils.Grid
import mpdev.springboot.aoc2018.utils.Point

class Tank(input: List<String>) {

    val spring = Point(500,0)
    val grid: Grid<TankData> = processInput(input)
    val MINY: Int = grid.getDataPoints().filter { it.value == TankData.WALL }.keys.minOf { it.y }

    fun fillAllTanks() {
        val queue = ArrayList<Point>()
        queue.add(spring)
        while (queue.isNotEmpty()) {
            val overflowPts = waterFlow(queue.removeFirst())
            overflowPts.filter { it.x >= 0 }.forEach { if (!queue.contains(it)) queue.add(it) }
        }
    }

    fun waterFlow(fromPoint: Point): List<Point> {
        val (_,_,_, maxY) = grid.getMinMaxXY()
        //val gridData = grid.getDataPoints()
        var curPoint = fromPoint
        // if start point is underwater move it above water - to support tank inside tank
        while (grid.getDataPoint(curPoint) == TankData.WATER && curPoint.y > 0)
            curPoint = curPoint.above()
        while (curPoint.y < maxY) {
            val nextDataPoint = grid.getDataPoint(curPoint.below())
            if (grid.getDataPoint(curPoint) == null && nextDataPoint == TankData.DRIED) {
                grid.setDataPoint(curPoint, TankData.DRIED)     // water has reached here already - do nothing
                return emptyList()
            }
            if (nextDataPoint == null) {
                grid.setDataPoint(curPoint.below(), TankData.DRIED)
            }
            else
            if (nextDataPoint == TankData.WALL || nextDataPoint == TankData.WATER) {
                return fillOneTank(curPoint)
            }
            curPoint = curPoint.below()
        }
        return listOf(Point(-1,-1))     // running off of the grid
    }

    private fun fillOneTank(fillPoint: Point): List<Point> {
        var curPoint = fillPoint
        val overflowPoints = mutableListOf<Point>()
        while (curPoint.y >= 0 && overflowPoints.isEmpty()) {
            val (isWallLeft, leftPoint) = wallLeft(curPoint)
            val (isWallRight, rightPoint) = wallRight(curPoint)
            if (isWallLeft && isWallRight) {
                // fill tank
                fillLevel(leftPoint, rightPoint, TankData.WATER)
            }
            else {
                // overflow
                grid.setDataPoint(curPoint, TankData.DRIED)
                if (!isWallLeft)
                    overflowPoints.add(leftPoint)
                if (!isWallRight)
                    overflowPoints.add(rightPoint)
                fillLevel(leftPoint, rightPoint, TankData.DRIED)
            }
            curPoint = curPoint.above()
        }
        return overflowPoints
    }

    private fun fillLevel(left: Point, right: Point, fill: TankData) {
        (left.x .. right.x).forEach { x -> grid.setDataPoint(Point(x, left.y), fill) }
    }

    // if there is a wall to the left, return the point to the left where the water ends
    // otherwise return the point to the left where the overflow starts
    private fun wallLeft(point: Point): Pair<Boolean,Point> {
        val minX = grid.getMinMaxXY().x1
        var curPoint = point
        while (curPoint.x >= minX) {
            if (grid.getDataPoint(curPoint.below()) != TankData.WALL && grid.getDataPoint(curPoint.below()) != TankData.WATER)
                return Pair(false, curPoint)
            if (grid.getDataPoint(curPoint.left()) == TankData.WALL)
                return Pair(true, curPoint)
            curPoint = curPoint.left()
        }
        return Pair(false, Point(-1, -1))
    }

    // if there is a wall to the right, return the point to the right where the water ends
    // otherwise return the point to the right where the overflow starts
    private fun wallRight(point: Point): Pair<Boolean,Point> {
        val maxX = grid.getMinMaxXY().x2
        var curPoint = point
        while (curPoint.x <= maxX) {
            if (grid.getDataPoint(curPoint.below()) != TankData.WALL && grid.getDataPoint(curPoint.below()) != TankData.WATER)
                return Pair(false, curPoint)
            if (grid.getDataPoint(curPoint.right()) == TankData.WALL)
                return Pair(true, curPoint)
            curPoint = curPoint.right()
        }
        return Pair(false, Point(-1, -1))
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
    DRIED('|'),
    X('X');
    companion object {
        val mapper: Map<Char,TankData> = values().associateBy { it.value }
    }
}