package mpdev.springboot.aoc2018.solutions.day10

import mpdev.springboot.aoc2018.solutions.day06.Coordinates
import mpdev.springboot.aoc2018.utils.AocException
import mpdev.springboot.aoc2018.utils.Grid
import mpdev.springboot.aoc2018.utils.plus
import java.awt.Point

class Message(input: List<String>) {

    val msgData = mutableListOf<MessagePoint>()
    init {
        processInput(input)
    }

    fun doMovement() {
        msgData.forEach { point ->
            point.currentPos += point.velocity
        }
    }

    fun msgAppeared(): Boolean {
        msgData.forEach { p ->
            var msgOn = true
            for (i in 1..4) {
                if (!msgData.map{it.currentPos}.contains(p.currentPos + Point(p.currentPos.x, p.currentPos.y+i))) {
                    msgOn = false
                    break
                }
            }
            if (msgOn)
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