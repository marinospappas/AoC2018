package mpdev.springboot.aoc2018.solutions.day22

import mpdev.springboot.aoc2018.utils.Point

class Cave(input: List<String>) {

    val start = Point(0,0)
    var depth: Int = 0
    lateinit var target: Point

    init {
        processInput(input)
    }

    fun geologicIndex(point: Point): Int {
        if (point == start || point == target)
            return 0
        if (point.y == 0)
            return point.x * 16807
        if (point.x == 0)
            return point.y * 48271
        return erosion(Point(point.x-1, point.y)) * erosion(Point(point.x, point.y-1))
    }

    fun erosion(point: Point) = (geologicIndex(point) + depth) % 20183

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
        fun fromErosion(erosion: Int) = RegionType.values().first { it.ordinal == erosion % 3 }
    }
}

