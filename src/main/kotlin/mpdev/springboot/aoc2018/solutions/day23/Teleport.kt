package mpdev.springboot.aoc2018.solutions.day23

import mpdev.springboot.aoc2018.utils.AocException
import mpdev.springboot.aoc2018.utils.PointND

class Teleport(input: List<String>) {

    val nanobots = mutableListOf<Pair<PointND,Int>>()
    lateinit var strongestNanobot: Pair<PointND,Int>
    init {
        processInput(input)
    }

    fun findStrongestNanobot() = nanobots.maxBy { it.second }

    fun findNanobotsInRange(nanobot: Pair<PointND,Int>) = nanobots.filter { it.first.manhattan(nanobot.first) <= nanobot.second }.toList()


    private fun processInput(input: List<String>) {
        input.forEach { line ->
            // pos=<0,0,0>, r=4
            val match = Regex("""pos=<(-?\d+),(-?\d+),(-?\d+)>, r=(\d+)""").find(line)
            try {
                val (x, y, z, r) = match!!.destructured
                nanobots.add(Pair(PointND(intArrayOf(x.toInt(),y.toInt(),z.toInt())), r.toInt()))
            } catch (e: Exception) {
                throw AocException("bad input line $line")
            }
        }
    }
}