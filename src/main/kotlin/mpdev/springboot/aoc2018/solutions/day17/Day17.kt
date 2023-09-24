package mpdev.springboot.aoc2018.solutions.day17

import mpdev.springboot.aoc2018.model.PuzzlePartSolution
import mpdev.springboot.aoc2018.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day17: PuzzleSolver() {

    final override fun setDay() {
        day = 17
    }

    init {
        setDay()
    }

    var result = 0
    lateinit var tank: Tank

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            tank = Tank(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            tank.fillTanks()
            result = tank.grid.countOf(TankData.WATER) +
                    tank.grid.getDataPoints().filter { it.key.y >= tank.MINY && it.value == TankData.DRIED }.count()
        }
        return PuzzlePartSolution(1, result.toString(), elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = tank.grid.countOf(TankData.WATER)
        }
        return PuzzlePartSolution(2, result.toString(), elapsed)
    }
}