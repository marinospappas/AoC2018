package mpdev.springboot.aoc2018.day10

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day10.Day10
import mpdev.springboot.aoc2018.solutions.day10.Message
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day10Test {

    private val day = 10                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day10()                      ///////// Update this for a new dayN test
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
    fun `Reads Input and sets up Grid data`() {
        val message = Message(inputLines)
        message.msgData.forEach { println(it) }
        assertThat(message.msgData.size).isEqualTo(inputLines.size)
        message.msgData.forEach { assertThat(it.initialPos).isEqualTo(it.currentPos) }
    }

    @Test
    @Order(3)
    fun `Plays next marble`() {

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
