package mpdev.springboot.aoc2018.solutions.day13

import mpdev.springboot.aoc2018.model.PuzzlePartSolution
import mpdev.springboot.aoc2018.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day13: PuzzleSolver() {

    final override fun setDay() {
        day = 13
    }

    init {
        setDay()
    }

    var result = ""
    lateinit var track: Track

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            track = Track(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = track.runCarsUntilFirstCrash().toString()
        }
        return PuzzlePartSolution(1, result, elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            track = Track(inputData)
            result = track.runCarsAndRemoveCrashes().toString()
        }
        return PuzzlePartSolution(2, result, elapsed)
    }

}