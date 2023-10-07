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
    lateinit var immuneSystem: ImmuneSystem

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            immuneSystem = ImmuneSystem(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            val (winning, unitsLeft) = immuneSystem.battle()
            log.info("part 1: $winning group won")
            result = unitsLeft
        }
        return PuzzlePartSolution(1, result.toString(), elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
        }
        return PuzzlePartSolution(2, result.toString(), elapsed)
    }
}