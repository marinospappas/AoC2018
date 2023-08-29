package mpdev.springboot.aoc2018.solutions.day06

import mpdev.springboot.aoc2018.model.PuzzlePartSolution
import mpdev.springboot.aoc2018.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day06: PuzzleSolver() {

    final override fun setDay() {
        day = 6
    }

    init {
        setDay()
    }

    var result = 0
    lateinit var coordinates: Coordinates

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            coordinates = Coordinates(inputData)
            val (minX, maxX, minY, maxY) = coordinates.grid.getMinMaxXY()
            log.info("min max x/y: {}, {}, {}, {}", minX, maxX, minY, maxY)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            coordinates.findClosestPointsForEachCoord()
            result = coordinates.getNonInfiniteAreas().max() + 1
        }
        return PuzzlePartSolution(1, result.toString(), elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            val areaWithinReqDistance = coordinates.getAreaWithinReqDistance()
            result = areaWithinReqDistance.size
        }
        return PuzzlePartSolution(2, result.toString(), elapsed)
    }

}