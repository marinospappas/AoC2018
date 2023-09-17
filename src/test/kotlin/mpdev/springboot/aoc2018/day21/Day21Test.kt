package mpdev.springboot.aoc2018.day21

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day21.Day21
import mpdev.springboot.aoc2018.solutions.day21.ProgramUtilsDay21
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import java.time.Duration
import java.util.concurrent.TimeUnit

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day21Test {

    private val day = 21                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day21()                      ///////// Update this for a new dayN test
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
        val programUtils = ProgramUtilsDay21(inputLines)
        println("ip is mapped to reg ${programUtils.program.ipIndex}")
        programUtils.program.code.forEach { println(it) }
        assertThat(programUtils.program.ipIndex).isEqualTo(3)
        assertThat(programUtils.program.code.size).isEqualTo(31)
    }

    @Test
    @Timeout(value = 2, unit = TimeUnit.SECONDS)
    @Order(3)
    fun `Tests the program flow part 1`() {
        val programUtils = ProgramUtilsDay21(inputLines)
        programUtils.program.breakpoint = { programUtils.breakPointP1() }
        programUtils.executeProgram()
        assertThat(programUtils.getRegister()[1]).isEqualTo(1797184)
        programUtils.program.breakpoint = {false}
        programUtils.executeProgram(LongArray(1) { 1797184 })
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("1797184")
    }

    @Test
    @Order(5)
    fun `Tests the program flow part 2`() {
        val programUtils = ProgramUtilsDay21(inputLines)
        programUtils.executeProgram(LongArray(1) { 0 /*7516359*/ }, injectedCode =  { programUtils.injectedCodePart2() })
    }

    @Test
    @Order(7)
    fun `Solves Part 2`() {

    }
}
