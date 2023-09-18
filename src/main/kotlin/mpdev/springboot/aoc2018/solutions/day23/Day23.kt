package mpdev.springboot.aoc2018.solutions.day23

import mpdev.springboot.aoc2018.model.PuzzlePartSolution
import mpdev.springboot.aoc2018.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day23: PuzzleSolver() {

    final override fun setDay() {
        day = 23
    }

    init {
        setDay()
    }

    var result = 0
    lateinit var teleport: Teleport

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            teleport = Teleport(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = teleport.findNanobotsInRange(teleport.findStrongestNanobot()).size
        }
        return PuzzlePartSolution(1, result.toString(), elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = teleport.findPointInRangeOfMost()
        }
        return PuzzlePartSolution(2, result.toString(), elapsed)
    }
}