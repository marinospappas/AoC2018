package mpdev.springboot.aoc2018.solutions.day10

import mpdev.springboot.aoc2018.model.PuzzlePartSolution
import mpdev.springboot.aoc2018.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day10: PuzzleSolver() {

    final override fun setDay() {
        day = 10
    }

    init {
        setDay()
    }

    var result = ""
    lateinit var message: Message

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            message = Message(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            while (!message.msgAppeared()) {
                message.doMovement()
                message.print()
            }
        }
        return PuzzlePartSolution(1, result, elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
        }
        return PuzzlePartSolution(2, result, elapsed)
    }

}