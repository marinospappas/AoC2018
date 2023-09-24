package mpdev.springboot.aoc2018.day17

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day17.Day17
import mpdev.springboot.aoc2018.solutions.day17.Tank
import mpdev.springboot.aoc2018.solutions.day17.TankData
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
    fun `Reads Input and sets up Tanks`() {
        val tank = Tank(inputLines)
        tank.print()
        assertThat(tank.grid.getDimensions()).isEqualTo(Pair(14,16))
        assertThat(tank.grid.countOf(TankData.WALL)).isEqualTo(34)
    }

    @Test
    @Order(3)
    fun `Follows Water dropping and Settling in the Tanks`() {
        val tank = Tank(inputLines)
        println("Initial state")
        tank.print()
        println("Fill first tank")
        var overflowPts: List<Point> = emptyList()
        while (overflowPts.isEmpty()) {
            overflowPts = tank.waterDrops(tank.spring)
            tank.print()
        }
        println("Fill second tank")
        val start = overflowPts.first()
        overflowPts = emptyList()
        while (overflowPts.isEmpty()) {
            overflowPts = tank.waterDrops(start)
            tank.print()
        }
        println("Second tank overflow")
        val start1 = overflowPts.first()
        val start2 = overflowPts.last()
        overflowPts = emptyList()
        while (overflowPts.isEmpty()) {
            overflowPts = tank.waterDrops(start1)
            tank.print()
        }
        overflowPts = emptyList()
        while (overflowPts.isEmpty()) {
            overflowPts = tank.waterDrops(start2)
            tank.print()
        }
        assertThat(tank.grid.countOf(TankData.WATER) + tank.grid.countOf(TankData.DRIED)).isEqualTo(57)
    }

    @Test
    @Order(4)
    fun `Fills Tanks inside Tanks`() {
        val tank = Tank(testData2())
        println("Initial state")
        tank.print()
        println("Fill first tank")
        var overflowPts: List<Point> = emptyList()
        while (overflowPts.isEmpty()) {
            overflowPts = tank.waterDrops(tank.spring)
            tank.print()
        }
        println("Fill inside tank")
        val start = overflowPts.first()
        overflowPts = emptyList()
        while (overflowPts.isEmpty()) {
            overflowPts = tank.waterDrops(start)
            tank.print()
        }
        println("Inside tank overflow - fill rest of outside tank")
        val start1 = overflowPts.first()
        overflowPts = emptyList()
        while (overflowPts.isEmpty()) {
            overflowPts = tank.waterDrops(start1)
            tank.print()
        }
        println("Outside tank overflow")
        val start2 = overflowPts.last()
        overflowPts = emptyList()
        while (overflowPts.isEmpty()) {
            overflowPts = tank.waterDrops(start2)
            tank.print()
        }
        assertThat(tank.grid.countOf(TankData.WATER) + tank.grid.countOf(TankData.DRIED)).isEqualTo(52)
    }

    @Test
    @Order(4)
    fun `Fills Small Tank overlapping with Big Tank`() {
        val tank = Tank(testData3())
        println("Initial state")
        tank.print()
        println("Fill small tank")
        var overflowPts: List<Point> = emptyList()
        while (overflowPts.isEmpty()) {
            overflowPts = tank.waterDrops(tank.spring)
            tank.print()
        }
        println("Small Tank left side overflow - fill big tank")
        val start1 = overflowPts.first()
        val start2 = overflowPts.last()
        overflowPts = emptyList()
        while (overflowPts.isEmpty()) {
            overflowPts = tank.waterDrops(start1)
            tank.print()
        }
        println("Small Tank right side overflow - no tank is filled")
        overflowPts = emptyList()
        while (overflowPts.isEmpty()) {
            overflowPts = tank.waterDrops(start2)
            tank.print()
        }
        println("Big tank overflow")
        val start3 = overflowPts.first()
        val start4 = overflowPts.last()
        overflowPts = emptyList()
        while (overflowPts.isEmpty()) {
            overflowPts = tank.waterDrops(start3)
            tank.print()
        }
        overflowPts = emptyList()
        while (overflowPts.isEmpty()) {
            overflowPts = tank.waterDrops(start4)
            tank.print()
        }
        tank.print()
        assertThat(tank.grid.countOf(TankData.WATER) + tank.grid.countOf(TankData.DRIED)).isEqualTo(74)
    }

    @Test
    @Order(6)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("57")
    }

    @Test
    @Order(7)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("")
    }

    fun testData2() = listOf(
        "x=494, y=3..9",
        "y=9, x=494..502",
        "x=502, y=2..9",
        "x=497, y=5..7",
        "y=7, x=497..499",
        "x=499, y=5..7"
    )

    fun testData3() = listOf(
        "x=499, y=2..5",
        "y=5, x=499..502",
        "x=502, y=2..5",
        "x=495, y=8..11",
        "y=11, x=495..506",
        "x=506, y=8..11"
    )
}
