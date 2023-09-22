package mpdev.springboot.aoc2018.day18

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day18.Day18
import mpdev.springboot.aoc2018.solutions.day18.Plot
import mpdev.springboot.aoc2018.solutions.day18.Woods
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day18Test {

    private val day = 18                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day18()                      ///////// Update this for a new dayN test
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
    fun `Reads Input and sets up Woods`() {
        val woods = Woods(inputLines)
        woods.print()
        assertThat(woods.grid.getDimensions()).isEqualTo(Pair(10,10))
        assertThat(woods.grid.countOf(Plot.WOOD)).isEqualTo(27)
        assertThat(woods.grid.countOf(Plot.LUMBER)).isEqualTo(17)
    }

    @Test
    @Order(3)
    fun `Performs State Change`() {
        val woods = Woods(inputLines)
        println("Initial State")
        woods.print()
        repeat(10) {
            println()
            println("iteration ${it+1}")
            woods.executeStateTransition()
            woods.print()
        }
        assertThat(woods.grid.countOf(Plot.WOOD)).isEqualTo(37)
        assertThat(woods.grid.countOf(Plot.LUMBER)).isEqualTo(31)
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("1147")
    }

    @Test
    @Order(6)
    fun `Performs State Change part 2`() {
        val woods = Woods(inputLines)
        println("Initial State")
        woods.print()
        repeat(10000) {
            println()
            println("iteration ${it+1}")
            woods.executeStateTransition()
            woods.print()
        }
        assertThat(woods.grid.countOf(Plot.WOOD)).isEqualTo(37)
        assertThat(woods.grid.countOf(Plot.LUMBER)).isEqualTo(31)
    }

    @Test
    @Order(7)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("")
    }
}
