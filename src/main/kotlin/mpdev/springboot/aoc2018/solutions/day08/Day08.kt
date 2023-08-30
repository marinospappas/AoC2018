package mpdev.springboot.aoc2018.solutions.day08

import mpdev.springboot.aoc2018.model.PuzzlePartSolution
import mpdev.springboot.aoc2018.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day08: PuzzleSolver() {

    final override fun setDay() {
        day = 8
    }

    init {
        setDay()
    }

    var result = 0
    lateinit var tree: Tree
    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            tree = Tree(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = tree.calculateMetadataSum()
        }
        return PuzzlePartSolution(1, result.toString(), elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = tree.calculateValue()
        }
        return PuzzlePartSolution(2, result.toString(), elapsed)
    }

}