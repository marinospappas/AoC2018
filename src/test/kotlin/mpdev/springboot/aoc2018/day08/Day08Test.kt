package mpdev.springboot.aoc2018.day08

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day08.Day08
import mpdev.springboot.aoc2018.solutions.day08.Tree
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day08Test {

    private val day = 8                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day08()                      ///////// Update this for a new dayN test
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
    fun `Reads Input and sets up Tree`() {
        val tree = Tree(inputLines)
        tree.print()
        var numNodes = 0
        tree.traverseTree(tree.dataRoot) { _,_ -> ++numNodes }
        assertThat(numNodes).isEqualTo(4)
        assertThat(tree.dataRoot.noOfChildren).isEqualTo(2)
        assertThat(tree.dataRoot.sizeOfMetadata).isEqualTo(3)
        assertThat(tree.dataRoot.metadata).isEqualTo(listOf(1,1,2))
    }

    @Test
    @Order(3)
    fun `Traverses Tree and Calculates Metadata sum`() {
        val tree = Tree(inputLines)
        val sum = tree.calculateMetadataSum()
        println("sum of metadata: $sum")
        assertThat(sum).isEqualTo(138)
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("138")
    }

    @Test
    @Order(7)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("66")
    }
}
