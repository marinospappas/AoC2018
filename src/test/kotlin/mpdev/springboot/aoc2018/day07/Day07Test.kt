package mpdev.springboot.aoc2018.day07

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day07.Day07
import mpdev.springboot.aoc2018.solutions.day07.Instructions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day07Test {

    private val day = 7                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day07()                      ///////// Update this for a new dayN test
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
    fun `Reads Input and sets Instructions data`() {
        val instructions = Instructions(inputLines)
        instructions.graph.forEach { (k,v) -> println("step $k next: $v") }
        println(instructions.dependentSteps)
        val topSteps = instructions.findFirstSteps()
        println("top step: $topSteps")
        instructions.revDependenciesMap.forEach { (k,v) -> println("step $k depends on: $v") }
        assertThat(instructions.graph.size).isEqualTo(6)
        assertThat(instructions.revDependenciesMap.size).isEqualTo(5)
        assertThat(topSteps).isEqualTo(listOf('C'))
    }

    @Test
    @Order(3)
    fun `Calculates order of steps`() {
        val instructions = Instructions(inputLines)
        val orderedSteps = instructions.topologicalSortBfs(instructions.findFirstSteps())
        println("ordered steps: $orderedSteps")
        assertThat(orderedSteps).isEqualTo("CABDFE")
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("CABDFE")
    }

    @Test
    @Order(5)
    fun `Calculates order of steps and time for Part 2`() {
        val instructions = Instructions(inputLines)
        instructions.workers = Instructions.WORKERS_TEST
        instructions.timeSpent = Instructions.TIME_SPENT_TEST
        val orderedSteps = instructions.topologicalSortBfsMultiTasking(instructions.findFirstSteps())
        println("ordered steps: ${orderedSteps.first}")
        println("time         : ${orderedSteps.second}")
        assertThat(orderedSteps.first).isEqualTo("CABFDE")
        assertThat(orderedSteps.second).isEqualTo(15)
    }

    @Test
    @Order(7)
    fun `Solves Part 2`() {
        puzzleSolver.instructions.workers = Instructions.WORKERS_TEST
        puzzleSolver.instructions.timeSpent = Instructions.TIME_SPENT_TEST
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("15")
    }
}
