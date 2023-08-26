package mpdev.springboot.aoc2018.solutions.day02

import mpdev.springboot.aoc2018.model.PuzzlePartSolution
import mpdev.springboot.aoc2018.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

@Component
class Day02: PuzzleSolver() {

    final override fun setDay() {
        day = 2
    }

    init {
        setDay()
    }

    var result = ""

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureNanoTime {
        }
        return Pair(elapsed/1000, "micro-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = (getSumOfMultiples(inputData, 2) * getSumOfMultiples(inputData, 3)).toString()
        }
        return PuzzlePartSolution(1, result, elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            val (s1, s2) = differByOne()
            result = removeDiffChar(s1, s2)
        }
        return PuzzlePartSolution(2, result, elapsed)
    }

    private fun getSumOfMultiples(strings: List<String>, count: Int): Int {
        var sum = 0
        strings.forEach { s ->
                for (c in s.toCharArray()) {
                    if (s.count { it == c } == count) {
                        ++sum
                        break
                    }
                }
        }
        return sum
    }

    private fun differByOne(): Pair<String,String> {
        for (i in 0 .. inputData.lastIndex-1) {
            for (j in i+1 .. inputData.lastIndex)
                if (getDifferentChars(inputData[i], inputData[j]) == 1)
                    return Pair(inputData[i], inputData[j])
        }
        return Pair("","")
    }

    private fun getDifferentChars(s1: String, s2: String): Int {
        var count = 0
        if (s1.length != s2.length)
            return -1
        s1.indices.forEach {
            if (s1[it] != s2[it])
                ++count
        }
        return count
    }

    private fun removeDiffChar(s1: String, s2: String): String {
        if (s1.length != s2.length)
            return s1
        for (i in s1.indices)
            if (s1[i] != s2[i])
                return s1.removeRange(i..i)
        return s1
    }
}