package mpdev.springboot.aoc2018.solutions.day14

import mpdev.springboot.aoc2018.model.PuzzlePartSolution
import mpdev.springboot.aoc2018.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day14: PuzzleSolver() {

    final override fun setDay() {
        day = 14
    }

    init {
        setDay()
    }

    var result = ""
    lateinit var recipes: Recipes

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            recipes = Recipes(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = recipes.getNext10RecipesAfterRepeat(recipes.numberOfRecipes)
        }
        return PuzzlePartSolution(1, result, elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            result = recipes.getNumberOfRecipesBeforePattern(inputData[0]).toString()
        }
        return PuzzlePartSolution(2, result, elapsed)
    }
}