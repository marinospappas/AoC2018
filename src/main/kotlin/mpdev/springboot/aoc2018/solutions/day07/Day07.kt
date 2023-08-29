package mpdev.springboot.aoc2018.solutions.day07

import mpdev.springboot.aoc2018.model.PuzzlePartSolution
import mpdev.springboot.aoc2018.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day07: PuzzleSolver() {

    final override fun setDay() {
        day = 7
    }

    init {
        setDay()
    }

    var result = ""
    lateinit var instructions: Instructions

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            instructions = Instructions(inputData)
            instructions.graph.toSortedMap().forEach { (k,v) -> println("$k: $v") }
            instructions.revDependenciesMap.toSortedMap().forEach { (k,v) -> println("$k depends on: $v") }
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            val firstSteps = instructions.findFirstSteps().also { log.info("top steps: {}", it) }
            result = instructions.topologicalSortBfs(firstSteps)
        }
        return PuzzlePartSolution(1, result, elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            val firstSteps = instructions.findFirstSteps()
            instructions.graph.forEach { (_,v) -> v.completed = false }
            result = instructions.topologicalSortBfsMultiTasking(firstSteps).second.toString()
        }
        return PuzzlePartSolution(2, result, elapsed)
    }

}