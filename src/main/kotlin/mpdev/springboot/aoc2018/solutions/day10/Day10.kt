package mpdev.springboot.aoc2018.solutions.day10

import mpdev.springboot.aoc2018.model.PuzzlePartSolution
import mpdev.springboot.aoc2018.solutions.PuzzleSolver
import mpdev.springboot.aoc2018.utils.numOfDigits
import org.springframework.stereotype.Component
import kotlin.math.abs
import kotlin.system.measureTimeMillis

@Component
class Day10: PuzzleSolver() {

    final override fun setDay() {
        day = 10
    }

    init {
        setDay()
    }

    var result = 0
    lateinit var message: Message

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            result = 0
            message = Message(inputData)
        }
        log.info("initial min-max coordinates: x: {} - {} y: {} - {}", message.minX, message.maxX, message.minY, message.maxY)
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            val msgPoint = message.msgData.first { it.velocity.x != 0 }
            if (msgPoint.currentPos.x.numOfDigits() - msgPoint.velocity.x.numOfDigits() > 2) {
                val factor: Int = abs(msgPoint.currentPos.x / msgPoint.velocity.x)
                message.doMovement(factor)
                message.calculateMinMaxCoords()
                result = factor
                log.info("adjusted min-max coordinates: x: {} - {} y: {} - {}", message.minX, message.maxX, message.minY, message.maxY)
            }
            while (message.doMovement()) { // loop while the grid keeps shrinking
                ++result
            }
            log.info("final min-max coordinates: x: {} - {} y: {} - {}", message.minX, message.maxX, message.minY, message.maxY)
            message.print()
        }
        return PuzzlePartSolution(1, "message in logfile", elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        return PuzzlePartSolution(2, result.toString(), 0)
    }

}