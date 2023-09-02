package mpdev.springboot.aoc2018.solutions.day09

import mpdev.springboot.aoc2018.model.PuzzlePartSolution
import mpdev.springboot.aoc2018.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day09: PuzzleSolver() {

    final override fun setDay() {
        day = 9
    }

    init {
        setDay()
    }

    var result = 0L
    lateinit var marbleGame: MarbleGame

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            marbleGame = MarbleGame(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            while(marbleGame.idPlayed < marbleGame.maxMarbleId) {
                marbleGame.playMarble()
            }
            result = marbleGame.scoreMap.values.max()
        }
        return PuzzlePartSolution(1, result.toString(), elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            marbleGame = MarbleGame(inputData)
            marbleGame.maxMarbleId *= 100
            while(marbleGame.idPlayed < marbleGame.maxMarbleId) {
                marbleGame.playMarble()
            }
            result = marbleGame.scoreMap.values.max()
        }
        return PuzzlePartSolution(2, result.toString(), elapsed)
    }

}