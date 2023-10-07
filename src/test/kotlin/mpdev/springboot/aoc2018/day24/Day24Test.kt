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
    fun `Calculates Damage Level and Selects Targets`() {
        val immuneSystem = ImmuneSystem(inputLines)
        immuneSystem.debug = true
        immuneSystem.antibodies.forEach { println(it) }
        println()
        immuneSystem.infection.forEach { println(it) }
        println("\nselect targets")
        var damage11 = immuneSystem.calcDamage(immuneSystem.infection[1]!!, immuneSystem.antibodies[1]!!)
        var damage12 = immuneSystem.calcDamage(immuneSystem.infection[1]!!, immuneSystem.antibodies[2]!!)
        var damage21 = immuneSystem.calcDamage(immuneSystem.infection[2]!!, immuneSystem.antibodies[1]!!)
        var damage22 = immuneSystem.calcDamage(immuneSystem.infection[2]!!, immuneSystem.antibodies[2]!!)
        println("Infection Group 1 would deal Immune Group 1 $damage11 damage")
        println("Infection Group 1 would deal Immune Group 2 $damage12 damage")
        println("Infection Group 2 would deal Immune Group 1 $damage21 damage")
        println("Infection Group 2 would deal Immune Group 2 $damage22 damage")
        assertThat(damage11).isEqualTo(185832)
        assertThat(damage12).isEqualTo(185832)
        assertThat(damage22).isEqualTo(107640)
        damage11 = immuneSystem.calcDamage(immuneSystem.antibodies[1]!!, immuneSystem.infection[1]!!)
        damage12 = immuneSystem.calcDamage(immuneSystem.antibodies[1]!!, immuneSystem.infection[2]!!)
        damage21 = immuneSystem.calcDamage(immuneSystem.antibodies[2]!!, immuneSystem.infection[1]!!)
        damage22 = immuneSystem.calcDamage(immuneSystem.antibodies[2]!!, immuneSystem.infection[2]!!)
        println("Immune Group 1 would deal Infection Group 1 $damage11 damage")
        println("Immune Group 1 would deal Infection Group 2 $damage12 damage")
        println("Immune Group 2 would deal Infection Group 1 $damage21 damage")
        println("Immune Group 2 would deal Infection Group 2 $damage22 damage")
        assertThat(damage11).isEqualTo(76619)
        assertThat(damage12).isEqualTo(153238)
        assertThat(damage21).isEqualTo(24725)

        immuneSystem.selectTargets()

        assertThat(immuneSystem.antibodies[1]!!.currentTarget).isEqualTo(2)
        assertThat(immuneSystem.antibodies[2]!!.currentTarget).isEqualTo(1)
        assertThat(immuneSystem.infection[1]!!.currentTarget).isEqualTo(1)
        assertThat(immuneSystem.infection[2]!!.currentTarget).isEqualTo(2)
    }

    @Test
    @Order(4)
    fun `Simulates Battle`() {
        val immuneSystem = ImmuneSystem(inputLines)
        immuneSystem.debug = true
        println("Initial")
        immuneSystem.antibodies.forEach { println(it) }
        immuneSystem.infection.forEach { println(it) }
        var count = 0
        var unitsLeft = -1
        while(immuneSystem.antibodies.count { it.value.numOfUnits > 0 } > 0 && immuneSystem.infection.count { it.value.numOfUnits > 0 } > 0) {
            immuneSystem.reset()
            println()
            println("** round ${++count}")
            println("select target")
            immuneSystem.selectTargets()
            println("attack")
            immuneSystem.attack()
            println("units left")
            immuneSystem.antibodies.forEach { println(it) }
            immuneSystem.infection.forEach { println(it) }
            unitsLeft = immuneSystem.antibodies.values.sumOf { it.numOfUnits } + immuneSystem.infection.values.sumOf { it.numOfUnits }
            println("total units left = $unitsLeft")
        }
        assertThat(unitsLeft).isEqualTo(5216)
    }

    @Test
    @Order(6)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("5216")
    }

    @Test
    @Order(7)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("")
    }
}
