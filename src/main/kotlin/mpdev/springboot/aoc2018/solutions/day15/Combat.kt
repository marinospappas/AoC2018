package mpdev.springboot.aoc2018.solutions.day15

import mpdev.springboot.aoc2018.utils.*

class Combat(input: List<String>) {

    companion object {
        var DEBUG = false
    }

    var grid = Grid(input, AreaId.mapper, border = 0)
    val minX = grid.getMinMaxXY().x1
    val maxX = grid.getMinMaxXY().x2
    val minY = grid.getMinMaxXY().x3
    val maxY = grid.getMinMaxXY().x4

    fun run() {
        var data = grid.getDataPoints().entries.groupingBy { it.key }.aggregate { _, _: CombatUnit?, element, _ -> CombatUnit(element.value)  }
        while (true) {
            val (outcome, nextRound) = doRound(data)
            if (!outcome)
                break
            data = nextRound.toMap()
        }
    }

    fun doRound(data: Map<Point,CombatUnit>): Pair<Boolean,Map<Point,CombatUnit>> {
        val nextRoundData = data.toMutableMap()
        for (y in grid.getMinMaxXY().x3 .. grid.getMinMaxXY().x4)
            for (x in grid.getMinMaxXY().x1 .. grid.getMinMaxXY().x2) {
                val thisUnitPosition = Point(x, y)
                val thisUnit = data[thisUnitPosition] ?: continue
                if (thisUnit.id == AreaId.WALL)
                    continue
                val targetUnitPositions = findTargetPostions(thisUnit.id, data).also { if (it.isEmpty()) return Pair(false, mapOf()) }
                var targetInRangePosition: Point?
                if (findTargetInRange(thisUnitPosition, targetUnitPositions).also { targetInRangePosition = it } != null)
                    unitAttacks(thisUnit, targetInRangePosition!!)
                else
                    unitMoves(thisUnitPosition, thisUnit, targetUnitPositions, data, nextRoundData)
            }
        return Pair(true, nextRoundData)
    }

    fun findTargetPostions(id: AreaId, data: Map<Point,CombatUnit>): List<Point> {
        val targetId = if (id == AreaId.ELF) AreaId.GOBLIN else AreaId.ELF
        return data.entries.filter { e -> e.value.id == targetId }.map { it.key }
    }

    fun findTargetInRange(thisPosition: Point, targetPositions: List<Point>) =
        targetPositions.sorted().firstOrNull { thisPosition.adjacent(false).contains(it) }

    fun unitMoves(unitAPosition: Point, unitA: CombatUnit, enemyUnitsPositions: List<Point>, data: Map<Point,CombatUnit>, nextRound: MutableMap<Point,CombatUnit>) {
        val stepToNearestInRange = stepToNearestInRangePosition(unitAPosition, enemyUnitsPositions, data) ?: return
        nextRound[stepToNearestInRange] = CombatUnit.fromUnit(unitA)
        nextRound.remove(unitAPosition)
    }

    fun stepToNearestInRangePosition(unitAPosition: Point, enemyUnitsPositions: List<Point>, data: Map<Point,CombatUnit>): Point? {
        var points = mutableListOf<Point>()
        enemyUnitsPositions.forEach { point ->
            point.adjacent(false).forEach { adjPt -> if (data[adjPt] == null) points.add(adjPt) }
        }
        points = points.distinct().toMutableList()
        val graph = setupGraph(data, unitAPosition)
        if (DEBUG) {
            points.forEach { p -> grid.setDataPoint(p, AreaId.INRANGE) }; grid.print()
            println(Bfs<Point>().graphToString(graph, graph[unitAPosition]))
        }
        //val dijkstra = Dijkstra<Point>()
        val bfs = Bfs<Point>()
        val inRangeAndDistance = mutableListOf<Triple<Point,Int, Point>>()  // dest.point, distance, next step
        points.forEach { p ->
            //val minPath: MinCostPath<Point>
            /*try {
                minPath = dijkstra.runIt(graph[unitAPosition], graph[p])
                inRangeAndDistance.add(Triple(p, minPath.minCost, minPath.path[1].first))
                if (DEBUG) grid.setDataPoint(p, AreaId.REACHABLE)
            }
            catch (_: Exception){}*/
            val minPath: List<Vertex<Point>>
            try {
                minPath = bfs.shortestPath(graph[unitAPosition], graph[p])
                inRangeAndDistance.add(Triple(p, minPath.size, minPath[1].getId()))
            }
            catch (_: Exception) {}
        }
        if (inRangeAndDistance.isEmpty())
            return null
        inRangeAndDistance.sortWith { p1,p2 -> if (p1.second == p2.second) p1.first.compareTo(p2.first) else p1.second.compareTo(p2.second) }
        if (DEBUG) { grid.setDataPoint(inRangeAndDistance.first().first, AreaId.NEAREST); grid.print() }
        return inRangeAndDistance.first().third
    }

    fun unitAttacks(attackUnit: CombatUnit, enemyPOsition: Point) {
        // TODO
    }

    fun setupGraph(data: Map<Point,CombatUnit>, start: Point): Graph<Point> {
        val graph = Graph<Point>().also{ it.addNode(start)}
        (minY..maxY).forEach { y ->
            (minX..maxX).forEach { x ->
                val point = Point(x, y)
                if (data[point] == null)
                    graph.addNode(point)
            }
        }
        addNeighbours(graph)
        return graph
    }

    private fun addNeighbours(graph: Graph<Point>) {
        graph.getNodes().keys.forEach { point ->
            point.adjacent(false).forEach { neighbour ->
                if (graph.nodeExists(neighbour))
                    graph.connect(point, neighbour)
            }
        }
    }
}

data class CombatUnit(val id: AreaId, var attackPower: Int = 3, var hitPoints: Int = 200) {
    companion object {
        fun fromUnit(unit: CombatUnit) = CombatUnit(unit.id, unit.attackPower, unit.hitPoints)
    }
}

enum class AreaId(val value: Char) {
    WALL('#'),
    ELF('E'),
    GOBLIN('G'),
    INRANGE('?'),
    REACHABLE('@'),
    NEAREST('!'),
    STEP('x');
    companion object {
        val mapper: Map<Char, AreaId> = AreaId.values().associateBy { it.value }
    }
}