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
    private var geoIndexMap: Array<IntArray>
    private var erosionMap: Array<IntArray>
    private var dataMap = mutableMapOf<Point,RegionType>()

    init {
        processInput(input)
        maxX = target.x + target.x/2 + 1
        maxY = target.y + target.y/2 + 1
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
        }
        geoIndexMap[target.y][target.x] = 0
        erosionMap[target.y][target.x] = geologicIndex(target.x, target.y)
        setupGrid()
    }

    private fun setupGrid() {
        for (y in 0 until maxY)
            for (x in 0 until maxX)
                dataMap[Point(x,y)] = RegionType.fromErosion(erosionMap[y][x])
        grid = Grid(dataMap, RegionType.mapper, border = 0)
        grid.setDataPoint(start, RegionType.MOUTH)
        grid.setDataPoint(target, RegionType.TARGET)
        grid.updateDimensions()
    }

    fun calculateRiskLevel() = dataMap.filter { it.key.x <= target.x && it.key.y <= target.y }.values.sumOf { it.ordinal }

    private fun geologicIndex0(x: Int, y: Int): Int {
        if (y == 0)
            return x * 16807
        return y * 48271
    }

    private fun geologicIndex(x: Int, y: Int): Int {
        if (x == target.x && y == target.y)
            return 0
        return erosionMap[y][x-1] * erosionMap[y-1][x]
    }


    private fun erosion(x: Int, y: Int) = (geoIndexMap[y][x] + depth) % 20183

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

