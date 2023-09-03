package mpdev.springboot.aoc2018.solutions.day11

import mpdev.springboot.aoc2018.model.PuzzlePartSolution
import mpdev.springboot.aoc2018.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day11: PuzzleSolver() {

    final override fun setDay() {
        day = 11
    }

    init {
        setDay()
    }

    var result = ""
    lateinit var fuelCellGrid: FuelCellGrid

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            fuelCellGrid = FuelCellGrid(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            val maxPowerSquare = fuelCellGrid.findHighestPowerSubGrid()
            result = "${maxPowerSquare.first.x},${maxPowerSquare.first.y}"
        }
        return PuzzlePartSolution(1, result, elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {

        }
        return PuzzlePartSolution(2, result, 0)
    }

}