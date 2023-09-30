package mpdev.springboot.aoc2018.day15

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day15.AreaId
import mpdev.springboot.aoc2018.solutions.day15.Combat
import mpdev.springboot.aoc2018.solutions.day15.Day15
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day15Test {

    private val day = 15                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day15()                      ///////// Update this for a new dayN test
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
    fun `Reads Input and sets up Recipes`() {
        val combat = Combat(inputLines)
        combat.grid.print()
        assertThat(combat.grid.countOf(AreaId.GOBLIN)).isEqualTo(4)
        assertThat(combat.grid.countOf(AreaId.ELF)).isEqualTo(2)
        assertThat(combat.grid.getDimensions()).isEqualTo(Pair(7,7))
    }


    @Test
    @Order(5)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("")
    }

    @Test
    @Order(7)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("")
    }
}
