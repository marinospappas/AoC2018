package mpdev.springboot.aoc2018.solutions.day10

import mpdev.springboot.aoc2018.utils.AocException
import java.awt.Point

class Message(input: List<String>) {

    val msgData = mutableListOf<MessagePoint>()
    init {
        processInput(input)
    }

    fun processInput(input: List<String>) {
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
}

data class MessagePoint(val initialPos: Point, val velocity: Point, var currentPos: Point) {
    override fun toString() = "point ${initialPos.x},${initialPos.y} velocity ${velocity.x},${velocity.y} cur.position ${currentPos.x},${currentPos.y}"
}