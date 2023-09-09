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

    var result = 0
    lateinit var plants: Plants

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            plants = Plants(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            repeat(20) { plants.newGeneration() }
            result = plants.grid.getDataPoints().keys.filter { it.y == 20 }.sumOf { it.x }
        }
        return PuzzlePartSolution(1, result.toString(), elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
        }
        return PuzzlePartSolution(2, result.toString(), elapsed)
    }

}