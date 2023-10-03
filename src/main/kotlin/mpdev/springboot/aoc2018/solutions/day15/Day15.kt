package mpdev.springboot.aoc2018.solutions.day15

import mpdev.springboot.aoc2018.model.PuzzlePartSolution
import mpdev.springboot.aoc2018.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day15: PuzzleSolver() {

    final override fun setDay() {
        day = 15
    }

    init {
        setDay()
    }

    var result = 0
    lateinit var combat: Combat

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            combat = Combat(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            val (rounds, hitPoints) = combat.run()
            result = rounds * hitPoints
        }
        return PuzzlePartSolution(1, result.toString(), elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            combat = Combat(inputData)
            result = combat.findMinElfAttackPwr()
        }
        return PuzzlePartSolution(2, result.toString(), elapsed)
    }
}