package mpdev.springboot.aoc2018.day14

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day14.Day14
import mpdev.springboot.aoc2018.solutions.day14.Recipes
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day14Test {

    private val day = 14                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day14()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
    private var inputLines: List<String> = inputDataReader.read(day)

    @BeforeEach
    fun setup() {
        puzzleSolver.setDay()
        puzzleSolver.inputData = inputLines
        puzzleSolver.initSolver()
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(puzzleSolver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Reads Input and sets up Recipes`() {
        val recipes = Recipes(inputLines)
        assertThat(recipes.numberOfRecipes).isEqualTo(9)
    }

    @Test
    @Order(3)
    fun `Creates new Recipes`() {
        val recipes = Recipes(inputLines)
        println(recipes.scores)
        println(recipes.elfIndices)
        while(recipes.scores.size < 19) {
            recipes.makeRecipe()
            recipes.increaseIndices()
            println(recipes.scores)
            println(recipes.elfIndices)
        }
        assertThat(recipes.scores.subList(9,19)).isEqualTo(listOf(5,1,5,8,9,1,6,7,7,9))
    }

    @Test
    @Order(4)
    fun `Verify next 10 recipes after multiple recipes made`() {
        inputLines.forEach { line ->
            val (count, expected) = line.split(' ')
            val recipes = Recipes(inputLines)
            val result = recipes.getNext10RecipesAfterRepeat(count.toInt())
            println(recipes.scores)
            println(recipes.elfIndices)
            assertThat(result).isEqualTo(expected)
        }
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("5158916779")
    }

    @Test
    @Order(6)
    fun `Verify Number of recipes before pattern`() {
        inputLines.forEach { line ->
            val (expected, pattern) = line.split(' ')
            val recipes = Recipes(inputLines)
            val result = recipes.getNumberOfRecipesBeforePattern(pattern.substring(0,5))
            println(result)
            assertThat(result).isEqualTo(expected.toInt())
        }
    }

    @Test
    @Order(8)
    fun `xxx`() {
        val recipes = Recipes(listOf("165061"))
        recipes.part2()
    }

    @Test
    @Order(7)
    fun `Solves Part 2`() {
        puzzleSolver.inputData = listOf("51589")
        puzzleSolver.initSolver()
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("9")
    }
}
