package mpdev.springboot.aoc2018.solutions.day10

import mpdev.springboot.aoc2018.utils.AocException
import mpdev.springboot.aoc2018.utils.Grid
import mpdev.springboot.aoc2018.utils.plus
import mpdev.springboot.aoc2018.utils.times
import java.awt.Point

class Message(input: List<String>) {

    val msgData = mutableListOf<MessagePoint>()
    var minX: Int = 0
    var maxX: Int = 0
    var minY: Int = 0
    var maxY: Int = 0

    init {
        processInput(input)
        calculateMinMaxCoords()
    }

    fun calculateMinMaxCoords() {
        minX = msgData.minOf { it.currentPos.x }
        maxX = msgData.maxOf { it.currentPos.x }
        minY = msgData.minOf { it.currentPos.y }
        maxY = msgData.maxOf { it.currentPos.y }
    }

    fun doMovement(factor: Int = 1): Boolean {
        val dimX = maxX - minX
        val dimY = maxY - minY
        val newPts = mutableListOf<Point>()
        msgData.forEach { point -> newPts.add(point.currentPos + point.velocity.times(factor)) }
        val newDimX = newPts.maxOf { it.x } - newPts.minOf { it.x }
        val newDimY = newPts.maxOf { it.y } - newPts.minOf { it.y }
        if (newDimX < dimX && newDimY < dimY) {     // if the grid shrank, update new positions and return true
            for (i in newPts.indices)
                msgData[i].currentPos = newPts[i]
            calculateMinMaxCoords()
            return true
        }
        return false
    }

    fun print() {
        Grid(Array(msgData.size) { msgData[it].currentPos }, mapper).print()
    }

    private fun processInput(input: List<String>) {
        input.forEach { s ->
            // position=<-3,  6> velocity=< 2, -1>
            val match = Regex("""position=< ?(-?\d+),  ?(-?\d+)> velocity=< ?(-?\d+),  ?(-?\d+)>""").find(s)
            try {
                val (x, y, vx, vy) = match!!.destructured
                msgData.add(MessagePoint(Point(x.toInt(),y.toInt()), Point(vx.toInt(),vy.toInt()), Point(x.toInt(),y.toInt())))
            } catch (e: Exception) {
                throw AocException("bad input line $s")
            }
        }
    }

    companion object {
        const val DATA_VALUE = -1
        val mapper = mapOf('#' to DATA_VALUE)
    }
}

data class MessagePoint(val initialPos: Point, val velocity: Point, var currentPos: Point) {
    override fun toString() = "point ${initialPos.x},${initialPos.y} velocity ${velocity.x},${velocity.y} cur.position ${currentPos.x},${currentPos.y}"
}