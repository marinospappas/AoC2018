package mpdev.springboot.aoc2018.day20

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day20.Day20
import mpdev.springboot.aoc2018.solutions.day20.Maze
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day20Test {

    private val day = 20                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day20()                      ///////// Update this for a new dayN test
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
    fun `Reads Input and sets up Maze directions`() {
        val maze = Maze(inputLines)
        assertThat(maze.directions).isEqualTo("ENWWW(NEEE|SSE(EE|N))")
    }

    @Test
    @Order(2)
    fun `Builds Graph from directions`() {
        val maze = Maze(inputLines)
        maze.buildDataMapFromDirections()
        maze.dataMap.forEach { (k,v) -> println("$k -> $v") }
        println(maze.dataMap.maxBy { it.value }.value)
        assertThat(maze.dataMap.values.max()).isEqualTo(10)
    }

    @Test
    @Order(6)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("10")
    }

    @Test
    @Order(7)
    fun `Solves Part 2`() {
        // NA -- assertThat(puzzleSolver.solvePart2().result).isEqualTo("")
    }
}
