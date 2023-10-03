package mpdev.springboot.aoc2018.solutions.day24

import mpdev.springboot.aoc2018.model.PuzzlePartSolution
import mpdev.springboot.aoc2018.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day24: PuzzleSolver() {

    final override fun setDay() {
        day = 24
    }

    init {
        setDay()
    }

    var result = 0

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
        }
        return PuzzlePartSolution(1, result.toString(), elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
        }
        return PuzzlePartSolution(2, result.toString(), elapsed)
    }
}