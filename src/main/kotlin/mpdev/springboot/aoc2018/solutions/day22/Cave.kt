package mpdev.springboot.aoc2018.solutions.day22

import mpdev.springboot.aoc2018.utils.*

class Cave(input: List<String>) {

    val start = Point(0,0)
    var depth: Int = 0
    lateinit var target: Point
    var maxX = 0
    var maxY = 0

    private var geoIndexMap: Array<IntArray>
    private var erosionMap: Array<IntArray>
    private var dataMap = mutableMapOf<Point,RegionType>()
    lateinit var grid: Grid<RegionType>

    companion object {
        const val TOOL_SWAP_COST = 7
    }

    init {
        processInput(input)
        maxX = target.x + 5 * target.x
        maxY = target.y + if (target.y > 100) target.y / 40 else target.y / 2
        geoIndexMap = Array(maxY) { IntArray(maxX) { 0 } }
        erosionMap = Array(maxY) { IntArray(maxX) { 0 } }
    }

    /////////////// part1

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

    ////////////// part 2

    val graph = Graph<GraphId>()

    fun setupGraph() {
        dataMap[start] = RegionType.MOUTH
        dataMap[target] = RegionType.TARGET
        dataMap.forEach { (k,v) ->
            v.tools.forEach { tool ->
                graph.addNode(GraphId(k, tool))
            }
        }
        addNeighboursAndCost()
    }

    private fun addNeighboursAndCost() {
        dataMap.forEach { (point, v) ->
            point.adjacent(false).filter { it.x >= 0 && it.y >= 0 && it.x < maxX && it.y < maxY }.forEach { neighbour ->
                val allowedTools = v.tools intersect dataMap[neighbour]!!.tools
                v.tools.forEach { tool ->
                     allowedTools.forEach { newTool ->
                        graph.connect(GraphId(point, tool), GraphId(neighbour, newTool))
                        graph.updateCost(GraphId(point, tool), GraphId(neighbour, newTool),
                            1 + (if (tool != newTool) TOOL_SWAP_COST else 0))
                    }
                }
            }
        }
    }

    fun findMinPath(): Int {
        val dijkstra = Dijkstra(graph.costs)
        val a = graph[GraphId(start, Tool.TORCH)]
        val b = graph[GraphId(target, Tool.TORCH)]
        val minCostPath = dijkstra.runIt(a, b)
        minCostPath.path.forEach { pair ->
            if (grid.getDataPoint(pair.first.position) != RegionType.MOUTH && grid.getDataPoint(pair.first.position) != RegionType.TARGET)
                grid.setDataPoint(pair.first.position, RegionType.PATH)
        }
        return minCostPath.minCost
    }

    //////////////////////

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

enum class RegionType(val value: Char, val tools: Set<Tool>) {
    ROCKY('.', setOf(Tool.CLIMBING, Tool.TORCH)),
    WET('=',  setOf(Tool.CLIMBING, Tool.NONE)),
    NARROW('|',  setOf(Tool.NONE, Tool.TORCH)),
    MOUTH('M', setOf(Tool.TORCH)),
    TARGET('T', setOf(Tool.TORCH)),
    PATH('X', setOf());
    companion object {
        val mapper: Map<Char, RegionType> = RegionType.values().associateBy { it.value }
        fun fromErosion(erosion: Int) = RegionType.values().first { it.ordinal == erosion % 3 }
    }
}

enum class Tool {
    TORCH,
    CLIMBING,
    NONE
}

data class GraphId(val position: Point, val tool: Tool)
