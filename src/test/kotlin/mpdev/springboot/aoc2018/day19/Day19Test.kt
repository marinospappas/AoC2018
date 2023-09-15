package mpdev.springboot.aoc2018.day19

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day19.Day19
import mpdev.springboot.aoc2018.solutions.vmcomputer.Program
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day19Test {

    private val day = 19                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day19()                      ///////// Update this for a new dayN test
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
    fun `Reads Input and sets up OpCodes`() {

    }


    @Test
    @Order(6)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("1")
    }

    @Test
    @Order(7)
    fun `Solves Part 2`() {
        println("solve part 2 not applicable - no test data")
    }
}
