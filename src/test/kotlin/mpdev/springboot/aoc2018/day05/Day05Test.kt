package mpdev.springboot.aoc2018.day05

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day05.Day05
import mpdev.springboot.aoc2018.solutions.day05.Polymer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day05Test {

    private val day = 5                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day05()                      ///////// Update this for a new dayN test
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
    fun `Reads Input and sets Polymer data`() {
        val polymer = Polymer(inputLines)
        println(polymer.data)
        assertThat(polymer.data.length).isEqualTo(16)
    }

    @Test
    @Order(3)
    fun `Executes Polymer reaction`() {
        val polymer = Polymer(inputLines)
        val result = polymer.doReaction(polymer.data, true)
        assertThat(result).isEqualTo("dabCBAcaDA")
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("10")
    }

    @Test
    @Order(5)
    fun `Improves Polymer`() {
        val polymer = Polymer(inputLines)
        val result = polymer.doReaction(polymer.data, true)
        polymer.reducedData = result
        assertThat(result).isEqualTo("dabCBAcaDA")
        val improvedMap = polymer.removeElementAndDoReaction(true)
        assertThat(improvedMap.values.minOfOrNull { it.length }).isEqualTo(4)
    }

    @Test
    @Order(6)
    fun `Solves Part 2`() {
        puzzleSolver.solvePart1()
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("4")
    }
}
