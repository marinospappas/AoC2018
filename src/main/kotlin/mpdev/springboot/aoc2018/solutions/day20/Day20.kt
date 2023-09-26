package mpdev.springboot.aoc2018.solutions.day20

import mpdev.springboot.aoc2018.model.PuzzlePartSolution
import mpdev.springboot.aoc2018.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day20: PuzzleSolver() {

    final override fun setDay() {
        day = 20
    }

    init {
        setDay()
    }

    var result = 0
    lateinit var maze: Maze

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            maze = Maze(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            //maze.buildDataMapFromDirections()
            //result = maze.dataMap.values.max()
            maze.buildGraphFromDirections()
            maze.calculateMinDistancesToAllNodes()
            result = maze.distMap.values.max()
        }
        return PuzzlePartSolution(1, result.toString(), elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = maze.dataMap.filter{ it.value >= 1000 }.count()
        }
        return PuzzlePartSolution(2, result.toString(), elapsed)
    }
}