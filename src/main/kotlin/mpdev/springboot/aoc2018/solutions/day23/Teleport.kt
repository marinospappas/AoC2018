package mpdev.springboot.aoc2018.solutions.day23

import mpdev.springboot.aoc2018.utils.AocException
import mpdev.springboot.aoc2018.utils.PointND

class Teleport(input: List<String>) {

    val nanobots = mutableListOf<Pair<PointND,Int>>()
    val refPoint = PointND(intArrayOf(0,0,0))

    init {
        processInput(input)
    }

    fun findStrongestNanobot() = nanobots.maxBy { it.second }

    fun findNanobotsInRange(nanobot: Pair<PointND,Int>) = nanobots.filter { it.first.manhattan(nanobot.first) <= nanobot.second }.toList()

    fun findPointInRangeOfMost(): Int {     // based on an idea from autid - translated from Fortran
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
        // here we have eliminated the nanobots that are not in range of any other nanobots
        // we assume that the point we are looking for is in range off (most of) all the remaining nanobots
        // if that point is p then for each nanobot n -> n.manhattan(p) < n.r
        // however n.manhattan(p) = n.manhattan(ref) - p.manhattan(ref)
        // which means n.manhattan(ref) - n.r is < p.manhattan(ref)
        // but given that ref = (0,0,0) p.manhattan(ref) = the sum of x,y,z of the requested point
        // by taking the max the values below we ensure that that point is in range of most nanobots
        for (i in nanobots.indices)
            if (!nanobotOutOfRangeOfOthers[i])
                distances[i] = nanobots[i].first.manhattan(refPoint) - nanobots[i].second
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