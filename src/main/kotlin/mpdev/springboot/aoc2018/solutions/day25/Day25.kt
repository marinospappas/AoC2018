package mpdev.springboot.aoc2018.solutions.day25

import mpdev.springboot.aoc2018.model.PuzzlePartSolution
import mpdev.springboot.aoc2018.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day25: PuzzleSolver() {

    final override fun setDay() {
        day = 25
    }

    init {
        setDay()
    }

    var result = 0
    lateinit var constellations: Constellations

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            constellations = Constellations(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            constellations.identifyConstellations()
            result = constellations.constList.size
        }
        return PuzzlePartSolution(1, result.toString(), elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        return PuzzlePartSolution(2, "AoC 2018 Finished", 0)
    }
}