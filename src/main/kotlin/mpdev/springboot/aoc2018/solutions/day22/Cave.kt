package mpdev.springboot.aoc2018.solutions.day22

import mpdev.springboot.aoc2018.utils.Point

class Cave(input: List<String>) {

    val start = Point(0,0)
    var depth: Int = 0
    lateinit var target: Point

    init {
        processInput(input)
    }

    private fun processInput(input: List<String>) {
        // depth: 510
        val match1 = Regex("""depth: (\d+)""").find(input[0])
        val (d) = match1!!.destructured
        depth = d.toInt()
        // target: 10,10
        val match2 = Regex("""target: (\d+),(\d+)""").find(input[1])
        val (x, y) = match2!!.destructured
        target = Point(x.toInt(), y.toInt())
    }
}

enum class RegionType(val value: Char) {
    ROCKY('.'),
    WET('='),
    NARROW('|'),
    MOUTH('M');
    companion object {
        val mapper: Map<Char, RegionType> = RegionType.values().associateBy { it.value }
    }
}

