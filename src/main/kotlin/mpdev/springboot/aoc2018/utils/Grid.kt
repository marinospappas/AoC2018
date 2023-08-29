package mpdev.springboot.aoc2018.utils

import java.awt.Point

open class Grid<T>(inputGridVisual: List<String> = emptyList(), private val mapper: Map<Char,T>) {

    protected var data = mutableMapOf<Point,T>()
    protected var maxX: Int = 0
    protected var maxY: Int = 0
    protected var minX: Int = 0
    protected var minY: Int = 0

    init {
        if (inputGridVisual.isNotEmpty()) {
            processInputVisual(inputGridVisual)
            updateXYDimensions()
        }
    }

    constructor(gridData: Map<Point,T>, mapper: Map<Char,T>): this(mapper = mapper) {
        data = gridData.toMutableMap()
        updateXYDimensions()
    }

    constructor(inputGridXY: Set<String>, mapper: Map<Char,T>): this(mapper = mapper) {
        processInputXY(inputGridXY)
        updateXYDimensions()
    }

    private fun updateXYDimensions() {
        maxX = data.keys.maxOf { it.x } + 1
        maxY = data.keys.maxOf { it.y } + 1
        minX = data.keys.minOf { it.x } - 1
        minY = data.keys.minOf { it.y } - 1
    }

    fun getDataPoints() = data.toMap()
    open fun getDataPoint(p: Point) = data[p]
    open fun setDataPoint(p: Point, t: T) {
        data[p] = t
    }

    fun getDimensions() = Pair(maxX-minX+1, maxY-minY+1)
    fun getMinMaxXY() = FourComponents(minX, maxX, minY, maxY)
    fun countOf(item: T) = data.values.count { it == item }

    open fun updateDimensions() {
       updateXYDimensions()
    }

    // mapping of a column or a row to int by interpreting the co-ordinates as bit positions
    fun mapRowToInt(n: Int, predicate: (T) -> Boolean = { true }) =
        data.filter { e -> predicate(e.value) && e.key.y == n }.map { e -> bitToInt[e.key.x] }
            .fold(0) { acc, i -> acc + i }

    fun mapColToInt(n: Int, predicate: (T) -> Boolean = { true }) =
        data.filter { e -> predicate(e.value) && e.key.x == n }.map { e -> bitToInt[e.key.y] }
            .fold(0) { acc, i -> acc + i }

    companion object {
        const val DEFAULT_CHAR = '.'
        private val bitToInt = intArrayOf( 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768,
            65536, 131072, 262144, 524288, 1_048_576, 2_097_152, 4_194_304, 8_388_608,
            16_777_216, 33_554_432, 67_108_864, 134_217_728, 268_435_456, 536_870_912, 1_073_741_824 )
    }

    private fun processInputVisual(input: List<String>) {
        input.indices.forEach { y ->
            input[y].indices.forEach { x ->
                if (mapper[input[y][x]] != null)
                    data[Point(x, y)] = mapper[input[y][x]]!!
            }
        }
    }

    private fun processInputXY(input: Set<String>) {
        input.forEach { s ->
            val (x, y) = s.split(",")
            data[Point(x.trim().toInt(), y.trim().toInt())] = mapper.values.first()
        }
    }

    private fun data2Grid(): Array<CharArray> {
        val grid: Array<CharArray> = Array(maxY-minY+1) { CharArray(maxX-minX+1) { DEFAULT_CHAR } }
        data.forEach { (pos, item) -> grid[pos.y - minY][pos.x - minX] = map2Char(item) }
        return grid
    }

    protected fun map2Char(t: T) = mapper.entries.firstOrNull { e -> e.value == t }?.key ?:
        if (t is Int) '0' + t%10 else 'x'

    open fun print() {
        printGrid(data2Grid())
    }

    protected fun printGrid(grid: Array<CharArray>) {
        for (i in grid.indices) {
            print("${String.format("%2d",i)} ")
            for (j in grid.first().indices)
                print(grid[i][j])
            println()
        }
        print("   ")
        for (i in grid.first().indices)
            print(if (i%10 == 0) i/10 else " ")
        println()
        print("   ")
        for (i in grid.first().indices)
            print(i%10)
        println()
    }

}

data class FourComponents(val x1: Int, val x2: Int, val x3: Int, val x4: Int)