package mpdev.springboot.aoc2018.solutions.day05

import mpdev.springboot.aoc2018.model.PuzzlePartSolution
import mpdev.springboot.aoc2018.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day05: PuzzleSolver() {

    final override fun setDay() {
        day = 5
    }

    init {
        setDay()
    }

    var result = 0
    lateinit var polymer: Polymer

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            polymer = Polymer(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            polymer.reducedData = polymer.doReaction(polymer.data)
            result = polymer.reducedData.length
        }
        return PuzzlePartSolution(1, result.toString(), elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            // uses the improved polymer saved in part 1
            result = polymer.removeElementAndDoReaction().values.minOfOrNull { it.length } ?: Int.MAX_VALUE

        }
        return PuzzlePartSolution(2, result.toString(), elapsed)
    }

}