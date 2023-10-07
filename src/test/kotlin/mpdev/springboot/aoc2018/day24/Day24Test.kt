package mpdev.springboot.aoc2018.day24

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day24.Day24
import mpdev.springboot.aoc2018.solutions.day24.ImmuneSystem
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Day24Test {

    private val day = 24                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day24()                      ///////// Update this for a new dayN test
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
    fun `Reads Input and sets up Immune System and Infections`() {
        val immuneSystem = ImmuneSystem(inputLines)
        immuneSystem.antibodies.forEach { println("$it") }
        println()
        immuneSystem.infection.forEach { println("$it") }
        assertThat(immuneSystem.antibodies.size).isEqualTo(2)
        assertThat(immuneSystem.infection.size).isEqualTo(2)
        assertThat(immuneSystem.antibodies.values.map { it.numOfUnits }).isEqualTo(listOf(17, 989))
        assertThat(immuneSystem.infection.values.map { it.numOfUnits }).isEqualTo(listOf(801, 4485))
    }

    @Test
    @Order(3)
    fun `Calculates Damage Level`() {
        val immuneSystem = ImmuneSystem(inputLines)
        immuneSystem.antibodies.forEach { println(it) }
        println()
        immuneSystem.infection.forEach { println(it) }
        println("\nselect targets")
        immuneSystem.selectTargets()
        println()
        immuneSystem.infection.forEach { println("infection group ${it.key} chooses immune group ${it.value.currentTarget}") }
        immuneSystem.antibodies.forEach { println("immune group ${it.key} chooses infection group ${it.value.currentTarget}") }
    }

    @Test
    @Order(4)
    fun `Simulates Attack Rounds`() {
        val immuneSystem = ImmuneSystem(inputLines)
        println("Initial")
        immuneSystem.antibodies.forEach { println(it) }
        immuneSystem.infection.forEach { println(it) }
        var count = 0
        while(immuneSystem.antibodies.count { it.value.numOfUnits > 0 } > 0 && immuneSystem.infection.count { it.value.numOfUnits > 0 } > 0) {
            immuneSystem.reset()
            println()
            println("***** round ${++count}")
            immuneSystem.selectTargets()
            immuneSystem.attack()
            println("units left")
            immuneSystem.antibodies.forEach { println(it) }
            immuneSystem.infection.forEach { println(it) }
            println("total units left = ${immuneSystem.antibodies.values.sumOf { it.numOfUnits } + immuneSystem.infection.values.sumOf { it.numOfUnits }}")
        }
        val unitsLeft = immuneSystem.antibodies.values.sumOf { it.numOfUnits } + immuneSystem.infection.values.sumOf { it.numOfUnits }
        println("total units left = $unitsLeft")
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
