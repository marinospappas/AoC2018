package mpdev.springboot.aoc2018.day19

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day19.Day19
import mpdev.springboot.aoc2018.solutions.day19.ProgramUtilsDay19
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day19Test {

    private val day = 19                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day19()                      ///////// Update this for a new dayN test
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
    fun `Reads Input and sets up Program`() {
        val programUtils = ProgramUtilsDay19(inputLines)
        println("ip is mapped to reg ${programUtils.program.ipIndex}")
        programUtils.program.code.forEach { println(it) }
        assertThat(programUtils.program.ipIndex).isEqualTo(0)
        assertThat(programUtils.program.code.size).isEqualTo(7)
    }

    @Test
    @Order(3)
    fun `Part 1 Divisors`() {
        val divisors = mutableListOf<Int>()
        val n = 954
        (1..n).forEach { i -> if (n % i == 0) divisors.add(i) }
        println(divisors)
        println(divisors.sum())
        assertThat(divisors.sum()).isEqualTo(2106)
    }

    @Test
    @Order(6)
    fun `Solves Part 1`() {
        puzzleSolver.programUtils.program.DEBUG = true
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("6")
    }

    @Test
    @Order(7)
    fun `Solves Part 2`() {
        val divisors = mutableListOf<Int>()
        val n = 10551354
        (1..n).forEach { i -> if (n % i == 0) divisors.add(i) }
        println(divisors)
        println(divisors.sum())
        assertThat(divisors.sum()).isEqualTo(23021280)
    }
}
