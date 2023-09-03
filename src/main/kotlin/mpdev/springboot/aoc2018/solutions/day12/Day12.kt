package mpdev.springboot.aoc2018.solutions.day12

import mpdev.springboot.aoc2018.model.PuzzlePartSolution
import mpdev.springboot.aoc2018.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day12: PuzzleSolver() {

    final override fun setDay() {
        day = 12
    }

    init {
        setDay()
    }

    var result = ""

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
        }
        return PuzzlePartSolution(1, result, elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
        }
        return PuzzlePartSolution(2, result, elapsed)
    }

}