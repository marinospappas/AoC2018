package mpdev.springboot.aoc2018.solutions.day12

import mpdev.springboot.aoc2018.utils.AocException
import mpdev.springboot.aoc2018.utils.Grid
import java.awt.Point

class Plants(input: List<String>) {

    companion object {
        const val PATTERN_LENGTH = 5
    }

    var grid: Grid<Pot>
    var rules = mutableSetOf<Int>()

    init {
        grid = processInput(input)
    }

    fun newGeneration() {
        val (minX, maxX, _, maxY) = grid.getMinMaxXY()
        val y = maxY + 1
        for (x in (minX-4 .. maxX+4)) {
            if (rules.contains(patternToInt(x, y-1)))
                grid.setDataPoint(Point(x,y), Pot.PLANT)
            else {
                if (grid.getDataPoint(Point(x, y)) != null)
                    grid.removeDataPoint(Point(x, y))
            }
        }
        grid.updateDimensions()
    }

    fun patternToInt(x: Int, y: Int): Int {
        var result = 0
        (0 until PATTERN_LENGTH).forEach { i ->
            result = 2 * result + if (grid.getDataPoint(Point(x-2+i,y)) == null) 0 else 1
        }
        return result
    }

    fun print(printRules: Boolean = false) {
        grid.print()
        if (printRules)
            rules.forEach { println("${patternFromInt(it)} => true")}
    }

    private fun patternFromInt(i: Int) =
        i.toString(2).padStart(PATTERN_LENGTH, '0').replace('0','.').replace('1','#')


    private fun processInput(input: List<String>): Grid<Pot> {
        var firstLine = true
        var initialState = ""
        input.filter { it.isNotEmpty() }.forEach { line ->
            try {
                if (firstLine) {
                    val match = Regex("""initial state: (.+)""").find(line)
                    val (plantState) = match!!.destructured
                    initialState = plantState
                    firstLine = false
                }
                else {
                    val match = Regex("""([.#]{5}) => ([.#])""").find(line)
                    val (pattern, newPlant) = match!!.destructured
                    if (newPlant == "#")
                        rules.add(pattern.replace('.','0').replace('#','1').toInt(2))
                }
            } catch (e: Exception) {
                throw AocException("bad input line $line")
            }
        }
        return Grid(listOf(initialState), Pot.mapper, 0)
    }
}

enum class Pot(val value: Char) {
    PLANT('#');

    companion object {
        val mapper: Map<Char,Pot> = values().associateBy { it.value }
    }
}