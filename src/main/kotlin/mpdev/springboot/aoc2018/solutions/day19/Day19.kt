package mpdev.springboot.aoc2018.solutions.day19

import mpdev.springboot.aoc2018.model.PuzzlePartSolution
import mpdev.springboot.aoc2018.solutions.PuzzleSolver
import mpdev.springboot.aoc2018.solutions.intcodecomputer.Program
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day19: PuzzleSolver() {

    final override fun setDay() {
        day = 16
    }

    init {
        setDay()
    }

    var result = 0
    lateinit var program: Program

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            program = Program(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            program.buildListOfMatchingOpCodes()
            result = program.matchingOpCodes.count { it.second.size >= 3 }
        }
        return PuzzlePartSolution(1, result.toString(), elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            program.identifyOpCodes()
            program.executeProgram()
            result = Program.register[0]
        }
        return PuzzlePartSolution(2, result.toString(), elapsed)
    }
}