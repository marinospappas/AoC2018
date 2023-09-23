package mpdev.springboot.aoc2018.day17

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day17.Day17
import mpdev.springboot.aoc2018.solutions.day17.Tank
import mpdev.springboot.aoc2018.utils.Point
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day17Test {

    private val day = 17                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day17()                      ///////// Update this for a new dayN test
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
    fun `Reads Input and sets up Underground Tanks`() {
        val tank = Tank(inputLines)
        tank.print()
    }

    @Test
    @Order(3)
    fun `Follows Water dropping and Settling in the Tanks`() {
        val tank = Tank(inputLines)
        tank.print()
        tank.waterDrops(tank.spring)
        tank.print()
        tank.waterDrops(Point(502,0))
        tank.print()
        tank.waterDrops(Point(505,8))
        tank.print()
        tank.waterDrops(Point(506,0))
        tank.print()
    }

    @Test
    @Order(6)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("")
    }

    @Test
    @Order(7)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("")
    }
}
