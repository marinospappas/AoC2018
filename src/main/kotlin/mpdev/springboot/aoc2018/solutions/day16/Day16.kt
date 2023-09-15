package mpdev.springboot.aoc2018.solutions.day16

import mpdev.springboot.aoc2018.model.PuzzlePartSolution
import mpdev.springboot.aoc2018.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day16: PuzzleSolver() {

    final override fun setDay() {
        day = 16
    }

    init {
        setDay()
    }

    var result = 0
    lateinit var programUtils: ProgramUtilsDay16

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            programUtils = ProgramUtilsDay16(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            programUtils.buildListOfMatchingOpCodes()
            result = programUtils.matchingOpCodes.count { it.second.size >= 3 }
        }
        return PuzzlePartSolution(1, result.toString(), elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            programUtils.identifyOpCodes()
            programUtils.executeProgram()
            result = programUtils.getRegister()[0]
        }
        return PuzzlePartSolution(2, result.toString(), elapsed)
    }
}