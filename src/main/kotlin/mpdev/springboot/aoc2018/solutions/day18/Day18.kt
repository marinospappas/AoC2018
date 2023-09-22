package mpdev.springboot.aoc2018.solutions.day18

import mpdev.springboot.aoc2018.model.PuzzlePartSolution
import mpdev.springboot.aoc2018.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day18: PuzzleSolver() {

    final override fun setDay() {
        day = 18
    }

    init {
        setDay()
    }

    var result = 0
    lateinit var woods: Woods

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            woods = Woods(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            repeat(10) { woods.executeStateTransition() }
            result = woods.grid.countOf(Plot.WOOD) * woods.grid.countOf(Plot.LUMBER)
        }
        return PuzzlePartSolution(1, result.toString(), elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
        }
        return PuzzlePartSolution(2, result.toString(), elapsed)
    }
}