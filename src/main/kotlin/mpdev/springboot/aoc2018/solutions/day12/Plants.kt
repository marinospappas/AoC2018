package mpdev.springboot.aoc2018.solutions.day12

import mpdev.springboot.aoc2018.utils.AocException
import mpdev.springboot.aoc2018.utils.Grid

class Plants(input: List<String>) {

    var grid: Grid<Pot>
    var rules = mutableSetOf<Int>()

    init {
        grid = processInput(input)
    }

    private fun processInput(input: List<String>): Grid<Pot> {
        var firstLine = true
        var initialState = ""
        input.filter { it.isNotEmpty() }.forEach { line ->
            if (firstLine) {
                val match = Regex("""initial state: (.+)""").find(line)
                try {
                    val (plantState) = match!!.destructured
                    initialState = plantState
                    firstLine = false
                } catch (e: Exception) {
                    throw AocException("bad input line $line")
                }
            }
            else {
            }
        }
        return Grid(listOf(initialState), Pot.mapper)
    }
}

enum class Pot(val value: Char) {
    PLANT('#');

    companion object {
        val mapper: Map<Char,Pot> = values().associateBy { it.value }
    }
}