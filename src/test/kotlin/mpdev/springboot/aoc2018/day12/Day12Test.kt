package mpdev.springboot.aoc2018.day12

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day12.Day12
import mpdev.springboot.aoc2018.solutions.day12.Plants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day12Test {

    private val day = 12                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day12()                      ///////// Update this for a new dayN test
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
    fun `Reads Input and sets up Plants grid`() {
        val plants = Plants(inputLines)
        plants.print(true)
        println(plants.grid.getDimensions())
        assertThat(plants.grid.getDataPoints().count()).isEqualTo(inputLines[0].count{ it == '#' })
        assertThat(plants.rules.size).isEqualTo(inputLines.size-2)
    }

    @Test
    @Order(3)
    fun `Encodes plant pattern to Integer`() {
        val plants = Plants(inputLines)
        plants.print()
        println()
        val expected = listOf(0,1,2,4,9,18,5,10,20,9,19,6,12,24,16,0,
                              0,1,3,7,14,28,24,17,3,7,14,28,24,16,0)
        repeat(31) {
            val x = it - 3
            val intPattern = plants.patternToInt(x, 0)
            println("$x => $intPattern, ${intPattern.toString(2).padStart(Plants.PATTERN_LENGTH, '0')}")
            assertThat(intPattern).isEqualTo(expected[it])
        }
    }

    @Test
    @Order(4)
    fun `Creates new generation`() {
        val plants = Plants(inputLines)
        plants.print(true)
        println()
        repeat(20) { plants.newGeneration() }
        plants.print()
        val lastGen = plants.grid.getDataPoints().filter { it.key.y == 20 }.keys
        println("last generation: ${lastGen.minOf { it.x }}, ${lastGen.maxOf { it.x }}, ${lastGen.sumOf { it.x }}")
        assertThat(lastGen.minOf { it.x }).isEqualTo(-2)
        assertThat(lastGen.maxOf { it.x }).isEqualTo(34)
        assertThat(lastGen.sumOf { it.x }).isEqualTo(325)
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("325")
    }

    @Test
    @Order(7)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("")
    }

}
