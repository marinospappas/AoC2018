package mpdev.springboot.aoc2018.solutions.day23

import mpdev.springboot.aoc2018.utils.AocException
import mpdev.springboot.aoc2018.utils.PointND
import kotlin.math.abs

class Teleport(input: List<String>) {

    val nanobots = mutableListOf<Pair<PointND,Int>>()
    init {
        processInput(input)
    }

    fun findStrongestNanobot() = nanobots.maxBy { it.second }

    fun findNanobotsInRange(nanobot: Pair<PointND,Int>) = nanobots.filter { it.first.manhattan(nanobot.first) <= nanobot.second }.toList()

    fun findPointInRangeOfMost(): Int {
        val nanobotOutOfRangeOfOthers = BooleanArray(nanobots.size) { false }
        while (true) {
            val outOfRangeCnt = IntArray(nanobots.size) { 0 }
            for (indx in nanobots.indices) {
                if (nanobotOutOfRangeOfOthers[indx])
                    continue
                for (indx1 in nanobots.indices) {
                    if (nanobotOutOfRangeOfOthers[indx1])
                        continue
                    if (nanobots[indx].first.manhattan(nanobots[indx1].first) > nanobots[indx].second + nanobots[indx1].second)
                        ++outOfRangeCnt[indx1]
                }
            }
            if (outOfRangeCnt.all { it == 0 })
                break
            val maxOutOfRangeCnt = outOfRangeCnt.max()
            for (i in outOfRangeCnt.indices)
                if (outOfRangeCnt[i] == maxOutOfRangeCnt)
                    nanobotOutOfRangeOfOthers[i] = true
        }
        val distances = IntArray(nanobots.size) { 0 }
        for (i in nanobots.indices)
            if (!nanobotOutOfRangeOfOthers[i])
                distances[i] = abs(nanobots[i].first.x_i.sum() - nanobots[i].second)
        return distances.max()
    }

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