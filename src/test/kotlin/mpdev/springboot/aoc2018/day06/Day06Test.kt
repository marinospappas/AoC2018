package mpdev.springboot.aoc2018.day06

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day06.Coordinates
import mpdev.springboot.aoc2018.solutions.day06.Day06
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day06Test {

    private val day = 6                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day06()                      ///////// Update this for a new dayN test
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
    fun `Reads Input and sets coordinates`() {
        val coordinates = Coordinates(inputLines)
        coordinates.grid.print()
        coordinates.coordIndex.forEach { println(it) }
        assertThat(coordinates.grid.getDataPoints().size).isEqualTo(6)
        assertThat(coordinates.coordIndex.size).isEqualTo(6)
    }

    @Test
    @Order(3)
    fun `Calculates areas of nearest points`() {
        val coordinates = Coordinates(inputLines)
        coordinates.findClosestPointsForEachCoord()
        coordinates.grid.print()
        val nonInfiniteAreas = coordinates.getNonInfiniteAreas()
        println(nonInfiniteAreas)
        assertThat(nonInfiniteAreas).isEqualTo(listOf(8,16))
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("17")
    }

    @Test
    @Order(5)
    fun `Calculates area within Req Distance`() {
        val coordinates = Coordinates(inputLines)
        coordinates.reqDistance = 32
        val areaWithinReqDistance = coordinates.getAreaWithinReqDistance()
        areaWithinReqDistance.forEach { p -> coordinates.grid.setDataPoint(p, 9) }
        coordinates.grid.print()
        assertThat(areaWithinReqDistance.size).isEqualTo(16)
    }

    @Test
    @Order(7)
    fun `Solves Part 2`() {
        puzzleSolver.coordinates.reqDistance = 32
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("16")
    }
}
