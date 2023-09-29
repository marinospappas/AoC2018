package mpdev.springboot.aoc2018.day22

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day22.Cave
import mpdev.springboot.aoc2018.solutions.day22.Day22
import mpdev.springboot.aoc2018.solutions.day22.GraphId
import mpdev.springboot.aoc2018.solutions.day22.Tool
import mpdev.springboot.aoc2018.utils.Bfs
import mpdev.springboot.aoc2018.utils.Point
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day22Test {

    private val day = 22                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day22()                      ///////// Update this for a new dayN test
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
    fun `Reads Input and sets up Cave`() {
        val cave = Cave(inputLines)
        println(cave.depth)
        println(cave.target)
        assertThat(cave.depth).isEqualTo(510)
        assertThat(cave.target).isEqualTo(Point(10,10))
    }

    @Test
    @Order(3)
    fun `Scans Cave and calculates erosion`() {
        val cave = Cave(inputLines)
        cave.scanCave(cave.maxX, cave.maxY)
        cave.grid.print()
        val riskLevel = cave.calculateRiskLevel()
        println(riskLevel)
        assertThat(riskLevel).isEqualTo(114)
    }

    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("114")
    }

    @Test
    @Order(6)
    fun `Sets up Graph and calculates minimum path`() {
        val cave = Cave(inputLines)
        cave.scanCave(cave.maxX, cave.maxY)
        cave.setupGraph()
        cave.grid.print()
        println(Bfs<GraphId>().graphToString(cave.graph, cave.graph[GraphId(cave.start, Tool.TORCH)]))
        println(cave.graph.getNodes())
        println(cave.graph.getConnections)
        println(cave.graph.costs)
    }

    @Test
    @Order(7)
    fun `Solves Part 2`() {
        // NA
    }
}
