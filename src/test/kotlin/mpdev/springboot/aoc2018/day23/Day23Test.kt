package mpdev.springboot.aoc2018.day23

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day23.Day23
import mpdev.springboot.aoc2018.solutions.day23.Teleport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day23Test {

    private val day = 23                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day23()                      ///////// Update this for a new dayN test
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
    fun `Reads Input and sets up Nanobots`() {
        val teleport = Teleport(inputLines)
        teleport.nanobots.forEach { println(it) }
        assertThat(teleport.nanobots.size).isEqualTo(9)
    }

    @Test
    @Order(3)
    fun `Identifies Nanobots within range`() {
        val teleport = Teleport(inputLines)
        val strongest = teleport.findStrongestNanobot()
        val nanobotsInRange = teleport.findNanobotsInRange(strongest)
        println("Strongest: $strongest")
        println("In range: $nanobotsInRange")
    }

    @Test
    @Order(6)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("7")
    }

    @Test
    @Order(7)
    fun `Solves Part 2`() {
        puzzleSolver.inputData = inputPart2()
        puzzleSolver.initSolver()
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("36")
    }

    private fun inputPart2(): List<String> =
        listOf(
            "pos=<10,12,12>, r=2",
            "pos=<12,14,12>, r=2",
            "pos=<16,12,12>, r=4",
            "pos=<14,14,14>, r=6",
            "pos=<50,50,50>, r=200",
            "pos=<10,10,10>, r=5"
        )
}
