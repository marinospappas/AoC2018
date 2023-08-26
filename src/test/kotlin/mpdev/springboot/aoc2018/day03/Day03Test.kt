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
        fabric.claims.forEach { k, v ->  println("claim $k:"); v.print() }
        assertThat(fabric.claims.size).isEqualTo(3)
        assertThat(fabric.claims[1]!!.getDimensions()).isEqualTo(Pair(4,4))
        assertThat(fabric.claims[2]!!.getDimensions()).isEqualTo(Pair(4,4))
        assertThat(fabric.claims[3]!!.getDimensions()).isEqualTo(Pair(2,2))
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
