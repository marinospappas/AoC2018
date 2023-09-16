package mpdev.springboot.aoc2018.day16

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day16.Day16
import mpdev.springboot.aoc2018.solutions.day16.ProgramUtilsDay16
import mpdev.springboot.aoc2018.solutions.vmcomputer.Program
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day16Test {

    private val day = 16                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day16()                      ///////// Update this for a new dayN test
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
    fun `Reads Input and sets up OpCodes`() {
        val programUtils = ProgramUtilsDay16(inputLines)
        programUtils.samples.forEach { println(it) }
        println()
        programUtils.codeDay16.forEach { println(it) }
        assertThat((programUtils.samples.size)).isEqualTo(2)
        assertThat((programUtils.codeDay16.size)).isEqualTo(5)
    }

    @Test
    @Order(3)
    fun `Attempts to execute opcodes`() {
        val programUtils = ProgramUtilsDay16(inputLines)
        programUtils.program.DEBUG = true
        val (before, instr, after) = programUtils.samples[0]
        val matches = mutableListOf<Program.OpCode>()
        Program.OpCode.values().filterNot { it == Program.OpCode.nop }.forEach { opcode ->
            before.toLongArray().copyInto(programUtils.program.register)
            programUtils.program.executeStep(opcode, instr.params)
            if (programUtils.getRegister().toMutableList() == after) {
                print("matched")
                matches.add(opcode)
            }
            println()
        }
        println()
        println(matches)
        assertThat(matches).isEqualTo(listOf(Program.OpCode.addi, Program.OpCode.mulr, Program.OpCode.seti))
    }

    @Test
    @Order(4)
    fun `Finds Matching OpCodes`() {
        val programUtils = ProgramUtilsDay16(inputLines)
        val matches = programUtils.findMatches(programUtils.samples[0])
        println(matches)
        assertThat(matches).isEqualTo(listOf(Program.OpCode.addi, Program.OpCode.mulr, Program.OpCode.seti))
    }

    @Test
    @Order(4)
    fun `Builds Map of Matching OpCodes`() {
        val programUtils = ProgramUtilsDay16(inputLines)
        programUtils.buildListOfMatchingOpCodes()
        programUtils.matchingOpCodes.forEach { println(it) }
    }

    @Test
    @Order(6)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("1")
    }

    @Test
    @Order(7)
    fun `Solves Part 2`() {
        println("solve part 2 not applicable - no test data")
    }
}
