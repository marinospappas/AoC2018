package mpdev.springboot.aoc2018.solutions.day21

import mpdev.springboot.aoc2018.model.PuzzlePartSolution
import mpdev.springboot.aoc2018.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day21: PuzzleSolver() {

    final override fun setDay() {
        day = 21
    }

    init {
        setDay()
    }

    var result = 0L
    lateinit var programUtils: ProgramUtilsDay21

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            programUtils = ProgramUtilsDay21(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            programUtils.program.breakpoint = { programUtils.breakPointP1() }
            programUtils.executeProgram()
            result = programUtils.getRegister()[1]
        }
        return PuzzlePartSolution(1, result.toString(), elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = programUtils.getRegister()[0]
        }
        return PuzzlePartSolution(2, result.toString(), elapsed)
    }
}