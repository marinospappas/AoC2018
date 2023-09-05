package mpdev.springboot.aoc2018.day11

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day11.Day11
import mpdev.springboot.aoc2018.solutions.day11.FuelCellGrid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.awt.Point

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day11Test {

    private val day = 11                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day11()                      ///////// Update this for a new dayN test
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
        val fuelCellGrid = FuelCellGrid(inputLines)
        println("serial number: ${fuelCellGrid.serialNumber}")
        fuelCellGrid.printGrid()
        println("sub sums")
        (0 until 300).forEach { y ->
            (0 until 300).forEach { x ->
                print("${fuelCellGrid.subGridSums[y][x]}  ")
            }
            println()
        }
        assertThat(fuelCellGrid.grid.size).isEqualTo(300)
        assertThat(fuelCellGrid.grid[0].size).isEqualTo(300)
        assertThat(fuelCellGrid.serialNumber).isEqualTo(inputLines[0].toInt())
    }

    @ParameterizedTest
    @CsvSource(
        "  3,   5,  8,  4",
        "122,  79, 57, -5",
        "217, 196, 39,  0",
        "101, 153, 71,  4"
    )
    @Order(3)
    fun `Calculates Cell Power level`(x: Int, y: Int, serNum: Int, expected: Int) {
        val fuelCellGrid = FuelCellGrid(listOf(serNum.toString()))
        assertThat(fuelCellGrid.getPowerLevel(x,y)).isEqualTo(expected)
    }

    @ParameterizedTest
    @CsvSource(
        "18, 33, 45, 29",
        "42, 21, 61, 30"
    )
    @Order(3)
    fun `Calculates Max Power 3x3 Square`(serNum: String, expX: Int, expY: Int, expPower: Int) {
        val fuelCellGrid = FuelCellGrid(listOf(serNum))
        val (maxPwrCoord, maxPower) = fuelCellGrid.findHighestPower3x3SubGrid()
        println(maxPower)
        fuelCellGrid.printSubgrid(maxPwrCoord)
        println()
        assertThat(maxPwrCoord).isEqualTo(Point(expX,expY))
        assertThat(maxPower).isEqualTo(expPower)
    }

    @ParameterizedTest
    @CsvSource(
        "18, 33, 45",
        "42, 21, 61"
    )
    @Order(4)
    fun `Solves Part 1`(serNum: String, expX: Int, expY: Int) {
        puzzleSolver.inputData = listOf(serNum)
        puzzleSolver.initSolver()
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("$expX,$expY")
    }

    @ParameterizedTest
    @CsvSource(
        "18,  90, 269, 16, 113",
        "42, 232, 251, 12, 119"
    )
    @Order(5)
    fun `Calculates Max Power Any size Square`(serNum: String, expX: Int, expY: Int, expSize: Int, expPower: Int) {
        val fuelCellGrid = FuelCellGrid(listOf(serNum))
        val (maxPwrCoord, size, maxPower) = fuelCellGrid.findAnyHighestPowerSubGrid()
        println("${maxPwrCoord.x},${maxPwrCoord.y},$size   $maxPower")
        fuelCellGrid.printSubgrid(maxPwrCoord, size)
        println()
        assertThat(maxPwrCoord).isEqualTo(Point(expX,expY))
        assertThat(maxPower).isEqualTo(expPower)
    }

    @ParameterizedTest
    @CsvSource(
        "18,  90, 269, 16",
        "42, 232, 251, 12"
    )
    @Order(7)
    fun `Solves Part 2`(serNum: String, expX: Int, expY: Int, expSize: Int) {
        puzzleSolver.inputData = listOf(serNum)
        puzzleSolver.initSolver()
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("$expX,$expY,$expSize")
    }

}
