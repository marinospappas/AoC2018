package mpdev.springboot.aoc2018.day16

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day16.Day16
import mpdev.springboot.aoc2018.solutions.day16.Program
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
        val program = Program(inputLines)
        program.samples.forEach { println(it) }
        println()
        program.code.forEach { println(it) }
        assertThat((program.samples.size)).isEqualTo(2)
        assertThat((program.code.size)).isEqualTo(5)
    }

    @Test
    @Order(3)
    fun `Attempts to execute opcodes`() {
        val program = Program(inputLines)
        val (before, instr, after) = program.samples[0]
        val matches = mutableListOf<Program.OpCode>()
        Program.OpCode.values().forEach { opcode ->
            before.toIntArray().copyInto(Program.register)
            program.executeStep(opcode, instr.params)
            print("${opcode.name}  ${Program.register.toList()}  ")
            if (Program.register.toList() == after) {
                print("****")
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
        val program = Program(inputLines)
        val matches = program.findMatches(program.samples[0])
        println(matches)
        assertThat(matches).isEqualTo(listOf(Program.OpCode.addi, Program.OpCode.mulr, Program.OpCode.seti))
    }

    @Test
    @Order(4)
    fun `Builds Map of Matching OpCodes`() {
        val program = Program(inputLines)
        program.buildMapOfMatchingOpCodes()
        program.matchingOpCodes.forEach { println(it) }
    }

    @Test
    @Order(6)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("1")
    }

    @Test
    @Order(7)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("")
    }
}
