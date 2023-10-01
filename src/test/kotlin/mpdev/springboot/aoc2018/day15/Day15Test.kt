package mpdev.springboot.aoc2018.day15

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day15.AreaId
import mpdev.springboot.aoc2018.solutions.day15.Combat
import mpdev.springboot.aoc2018.solutions.day15.CombatUnit
import mpdev.springboot.aoc2018.solutions.day15.Day15
import mpdev.springboot.aoc2018.utils.Point
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
        Combat.DEBUG = true
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(puzzleSolver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Reads Input and sets up Battlefield`() {
        val combat = Combat(inputLines)
        combat.grid.print()
        assertThat(combat.grid.countOf(AreaId.GOBLIN)).isEqualTo(4)
        assertThat(combat.grid.countOf(AreaId.ELF)).isEqualTo(2)
        assertThat(combat.grid.getDimensions()).isEqualTo(Pair(7,7))
    }

    @Test
    @Order(3)
    fun `Finds Nearest point in range of Target`() {
        val thisInput = listOf(
            "#######",
            "#E..G.#",
            "#...#.#",
            "#.G.#G#",
            "#######"
        )
        val combat = Combat(thisInput)
        combat.grid.print()
        val data = combat.grid.getDataPoints().entries.groupingBy { it.key }.aggregate { _, _: CombatUnit?, element, _ -> CombatUnit(element.value)  }
        val points = combat.findTargetPostions(AreaId.ELF, data)
        val nextStep = combat.stepToNearestInRangePosition(Point(1,1), points, data)
        combat.grid.setDataPoint(nextStep!!, AreaId.STEP)
        combat.grid.print()
        println(nextStep)
        assertThat(nextStep).isEqualTo(Point(2,1))
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
