package mpdev.springboot.aoc2018.day25

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day25.Constellations
import mpdev.springboot.aoc2018.solutions.day25.Day25
import mpdev.springboot.aoc2018.utils.PointND
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day25Test {

    private val day = 25                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day25()                      ///////// Update this for a new dayN test
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
    fun `Reads Input and sets up Program`() {
        val constellations = Constellations(inputLines)
        constellations.data.forEach { println(it) }
        assertThat(constellations.data.size).isEqualTo(8)
    }

    @Test
    @Order(3)
    fun `Identifies Constellations`() {
        val constellations = Constellations(inputLines)
        constellations.identifyConstellations()
        constellations.constList.forEach { println(it) }
        assertThat(constellations.constList.size).isEqualTo(2)
        constellations.data = constellations.data + PointND(intArrayOf(6,0,0,0))
        constellations.identifyConstellations()
        constellations.constList.forEach { println(it) }
        assertThat(constellations.constList.size).isEqualTo(1)
    }

    @Test
    @Order(6)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("2")
    }

    @Test
    @Order(7)
    fun `Solves Part 2`() {
        assert(true)
    }
}
