package mpdev.springboot.aoc2018.day13

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day13.Day13
import mpdev.springboot.aoc2018.solutions.day13.Track
import mpdev.springboot.aoc2018.utils.Point
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day13Test {

    private val day = 13                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day13()                      ///////// Update this for a new dayN test
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
    fun `Reads Input and sets up Track`() {
        val track = Track(inputLines)
        track.grid.print()
        track.print()
        println(track.cars)
        assertThat(track.cars.size).isEqualTo(2)
        assertThat(track.grid.getDataPoints().count()).isEqualTo(inputLines.sumOf { line -> line.count { it != ' ' } })
    }

    @Test
    @Order(3)
    fun `Finds first collision point`() {
        val track = Track(inputLines)
        track.print()
        println(track.cars)
        val collision = track.runCarsUntilFirstCrash(true)
        println(collision)
        assertThat(collision).isEqualTo(Point(7,3))
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("[7,3]")
    }

    @Test
    @Order(5)
    fun `Removes Crashes`() {
        val track = Track(inputPart2())
        val lastPosition = track.runCarsAndRemoveCrashes(true)
        println(lastPosition)
        assertThat(lastPosition).isEqualTo(Point(6,4))
    }

    @Test
    @Order(7)
    fun `Solves Part 2`() {
        puzzleSolver.inputData = inputPart2()
        puzzleSolver.initSolver()
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("[6,4]")
    }

    private fun inputPart2() = listOf(
        "/>-<\\  ",
        "|   |  ",
        "| /<+-\\",
        "| | | v",
        "\\>+</ |",
        "  |   ^",
        "  \\<->/"
    )
}
