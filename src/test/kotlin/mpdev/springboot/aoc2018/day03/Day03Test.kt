package mpdev.springboot.aoc2018.day03

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day03.Day03
import mpdev.springboot.aoc2018.solutions.day03.Fabric
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day03Test {

    private val day = 3                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day03()                      ///////// Update this for a new dayN test
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
    fun `Reads Input and sets Claims list`() {
        val fabric = Fabric(inputLines)
        fabric.claims.forEach { (k, v) ->  println("claim $k: $v") }
        assertThat(fabric.claims.size).isEqualTo(3)
        assertThat(fabric.claims[1]!!.x2 - fabric.claims[1]!!.x1).isEqualTo(4)
        assertThat(fabric.claims[1]!!.y2 - fabric.claims[1]!!.y1).isEqualTo(4)
        assertThat(fabric.claims[2]!!.x2 - fabric.claims[2]!!.x1).isEqualTo(4)
        assertThat(fabric.claims[2]!!.y2 - fabric.claims[2]!!.y1).isEqualTo(4)
        assertThat(fabric.claims[3]!!.x2 - fabric.claims[3]!!.x1).isEqualTo(2)
        assertThat(fabric.claims[3]!!.y2 - fabric.claims[3]!!.y1).isEqualTo(2)
    }

    @Test
    @Order(3)
    fun `Calculates overlapping points`() {
        val fabric = Fabric(inputLines)
        val overlapingPts = fabric.findOverlappingPoints()
        println(overlapingPts)
        assertThat(overlapingPts.size).isEqualTo(4)
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("4")
    }

    @Test
    @Order(6)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("3")
    }
}
