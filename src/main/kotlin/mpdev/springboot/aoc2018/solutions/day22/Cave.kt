package mpdev.springboot.aoc2018.solutions.day22

import mpdev.springboot.aoc2018.utils.Grid
import mpdev.springboot.aoc2018.utils.Point

class Cave(input: List<String>) {

    val start = Point(0,0)
    var depth: Int = 0
    lateinit var target: Point
    var maxX = 0
    var maxY = 0

    lateinit var grid: Grid<RegionType>
    var geoIndexMap: Array<IntArray>
    var erosionMap: Array<IntArray>
    var dataMap = mutableMapOf<Point,RegionType>()

    init {
        processInput(input)
        maxX = target.x + target.x/2
        maxY = target.y + target.y/2
        geoIndexMap = Array(maxY) { IntArray(maxX) { 0 } }
        erosionMap = Array(maxY) { IntArray(maxX) { 0 } }
    }

    fun scanCave(dimX: Int, dimY: Int) {
        // calculate values for first row and first column
        erosionMap[0][0] = erosion(0, 0)
        for (y in 1 until dimY) {
            geoIndexMap[y][0] = geologicIndex0(0, y)
            erosionMap[y][0] = erosion(0, y)
        }
        for (x in 1 until dimX) {
            geoIndexMap[0][x] = geologicIndex0(x, 0)
            erosionMap[0][x] = erosion(x, 0)
        }
        // calculate values for the rest area
        for (y in 1 until dimY) {
                for (x in 1 until dimX) {
                    geoIndexMap[y][x] = geologicIndex(x, y)
                    erosionMap[y][x] = erosion(x, y)
            }
            println("done y = $y")
        }
        geoIndexMap[target.y][target.x] = 0
        erosionMap[target.y][target.x] = geologicIndex(target.x, target.y)
        // update grid
        for (y in 0 until dimY)
            for (x in 0 until dimX)
                dataMap[Point(x,y)] = RegionType.fromErosion(erosionMap[y][x])
        grid = Grid(dataMap, RegionType.mapper, border = 0)
        grid.updateDimensions()
    }

    fun calculateRiskLevel(): Int {
        /*return  grid.getDataPoints().filter { it.key.x <= target.x && it.key.y <= target.y }
            .values.sumOf { it.ordinal }*/
        var result = 0
        val data = grid.getDataPoints()
        for (x in 0 .. target.x)
            for (y in 0 .. target.y)
                result += data[Point(x,y)]!!.ordinal
        return result
    }

    fun geologicIndex0(x: Int, y: Int): Int {
        if (y == 0)
            return x * 16807
        return y * 48271
    }

    fun geologicIndex(x: Int, y: Int) = erosionMap[y][x-1] * erosionMap[y-1][x]


    fun erosion(x: Int, y: Int) = (geoIndexMap[y][x] + depth) % 20183

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
    MOUTH('M'),
    TARGET('T');
    companion object {
        val mapper: Map<Char, RegionType> = RegionType.values().associateBy { it.value }
        fun fromErosion(erosion: Int) = RegionType.values().first { it.ordinal == erosion % 3 }
    }
}
