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
        val data = grid.getDataPoints().entries
            .groupingBy { it.key }
            .aggregate { _, _: CombatUnit?, element, _ -> CombatUnit(element.value)  }
            .toMutableMap()
        while (true) {
            if (!doRound(data))
                break
        }
    }

    fun doRound(data: MutableMap<Point,CombatUnit>): Boolean {
        //val nextRoundData = data.toMutableMap()
        val allUnitsPositions = data.filterNot { e -> e.value.id == AreaId.WALL }.keys.sorted()
        allUnitsPositions.forEach { thisUnitPosition ->
                val thisUnit = data[thisUnitPosition] ?: return@forEach
                val targetUnitPositions = findTargetPostions(thisUnit.id, data).also { if (it.isEmpty()) return false }
                var targetInRangePosition: Point?
                if (findTargetInRange(thisUnitPosition, targetUnitPositions, data).also { targetInRangePosition = it } != null)
                    unitAttacks(thisUnit, targetInRangePosition!!, data)
                else
                    unitMoves(thisUnitPosition, thisUnit, targetUnitPositions, data)
            }
        return true
    }

    fun findTargetPostions(id: AreaId, data: Map<Point,CombatUnit>): List<Point> {
        val targetId = if (id == AreaId.ELF) AreaId.GOBLIN else AreaId.ELF
        return data.entries.filter { e -> e.value.id == targetId }.map { it.key }
    }

    fun findTargetInRange(thisPosition: Point, targetPositions: List<Point>, data: Map<Point,CombatUnit>) =
        targetPositions.sortedWith{ p1, p2 -> if (data[p1]!!.hitPoints == data[p2]!!.hitPoints) p1.compareTo(p2) else
            data[p1]!!.hitPoints.compareTo(data[p2]!!.hitPoints) }
            .firstOrNull { thisPosition.adjacent(false).contains(it) }

    fun unitMoves(unitAPosition: Point, unitA: CombatUnit, enemyUnitsPositions: List<Point>, data: MutableMap<Point,CombatUnit>) {
        val stepToNearestInRange = stepToNearestInRangePosition(unitAPosition, enemyUnitsPositions, data) ?: return
        data[stepToNearestInRange] = CombatUnit.fromUnit(unitA)
        data.remove(unitAPosition)
    }

    fun stepToNearestInRangePosition(unitAPosition: Point, enemyUnitsPositions: List<Point>, data: Map<Point,CombatUnit>): Point? {
        var points = mutableListOf<Point>()
        enemyUnitsPositions.forEach { point ->
            point.adjacent(false).forEach { adjPt -> if (data[adjPt] == null) points.add(adjPt) }
        }
        points = points.distinct().toMutableList()
        if (DEBUG) { points.forEach { grid.setDataPoint(it, AreaId.INRANGE) } }
        val graph = setupGraph(data, unitAPosition)
        val bfs = Bfs<Point>()
        val inRangeAndDistance = mutableListOf<Triple<Point,Int, Point>>()  // dest.point, distance, next step
        points.forEach { p ->
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
        if (DEBUG) {
            inRangeAndDistance.forEach { grid.setDataPoint(it.first, AreaId.REACHABLE) }
            grid.setDataPoint(inRangeAndDistance.first().first, AreaId.NEAREST)
        }
        return inRangeAndDistance.first().third
    }

    fun unitAttacks(attackUnit: CombatUnit, enemyPosition: Point, data: MutableMap<Point,CombatUnit>) {
        val enemyUnit = data[enemyPosition]!!
        enemyUnit.hitPoints -= attackUnit.attackPower
        if (enemyUnit.hitPoints <= 0)
            data.remove(enemyPosition)
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