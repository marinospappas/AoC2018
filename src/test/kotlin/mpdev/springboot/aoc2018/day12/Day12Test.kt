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
        plants.grid.print()
        assertThat(plants.grid.getDataPoints().count()).isEqualTo(inputLines[0].count { it == '#' })
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("")
    }

    @Test
    @Order(7)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("")
    }

}
