package mpdev.springboot.aoc2018.day09

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day09.Day09
import mpdev.springboot.aoc2018.solutions.day09.MarbleGame
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day09Test {

    private val day = 9                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day09()                      ///////// Update this for a new dayN test
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
    fun `Reads Input and sets up Tree`() {
        val marbleGame = MarbleGame(inputLines)
        println("players: ${marbleGame.numberOfPlayers}, marbles: ${marbleGame.maxMarbleId}, high score: ${marbleGame.highScore}")
        println("marbles list: ${marbleGame.marbles}")
        assertThat(marbleGame.numberOfPlayers).isEqualTo(9)
        assertThat(marbleGame.maxMarbleId).isEqualTo(15)
        assertThat(marbleGame.highScore).isEqualTo(32)
        assertThat(marbleGame.current.id).isEqualTo(0)
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
