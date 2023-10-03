package mpdev.springboot.aoc2018.solutions.day15

import mpdev.springboot.aoc2018.utils.*

class Combat(input: List<String>) {

    companion object {
        var DEBUG = false
    }

    var grid = Grid(input, AreaId.mapper, border = 0)
    private var battleData = grid.getDataPoints().entries
        .groupingBy { it.key }
        .aggregate { _, _: CombatUnit?, element, _ -> CombatUnit(element.value)  }
        .toMutableMap()
    private val numOfElfs = grid.countOf(AreaId.ELF)
    val minX = grid.getMinMaxXY().x1
    val maxX = grid.getMinMaxXY().x2
    val minY = grid.getMinMaxXY().x3
    val maxY = grid.getMinMaxXY().x4

    fun run(print: Boolean = true): Pair<Int,Int> {
        var count = 0
        while (++count > 0) {
            if (!doRound(battleData))
                break
        }
        if (print) {
            grid = Grid(battleData.entries.groupingBy { it.key }
                .aggregate { _, _: AreaId?, element, _ -> element.value.id }, AreaId.mapper, border = 0)
            println("after $count round(s)")
            grid.print()
            println(battleData.values.filterNot { it.id == AreaId.WALL }.sumOf { it.hitPoints })
        }
        return Pair(count-1, battleData.values.filterNot { it.id == AreaId.WALL }.sumOf { it.hitPoints })
    }

    fun doRound(data: MutableMap<Point,CombatUnit>): Boolean {
        val allUnitsPositions = data.filterNot { e -> e.value.id == AreaId.WALL }.keys.sorted()
        allUnitsPositions.forEach { point ->
            var thisUnitPosition = point
                val thisUnit = data[thisUnitPosition] ?: return@forEach
                val targetUnitPositions = findTargetPostions(thisUnit.id, data).also { if (it.isEmpty()) return false }
                var targetInRangePosition: Point?
                if (findTargetInRange(thisUnitPosition, targetUnitPositions, data).also { targetInRangePosition = it } == null)
                    thisUnitPosition = unitMoves(thisUnitPosition, thisUnit, targetUnitPositions, data) ?: return@forEach
                if (findTargetInRange(thisUnitPosition, targetUnitPositions, data).also { targetInRangePosition = it } != null)
                    unitAttacks(thisUnit, targetInRangePosition!!, data)
            }
        return true
    }

    fun findTargetPostions(id: AreaId, data: Map<Point,CombatUnit>): List<Point> {
        val targetId = if (id == AreaId.ELF) AreaId.GOBLIN else AreaId.ELF
        return data.entries.filter { e -> e.value.id == targetId }.map { it.key }
    }

    private fun findTargetInRange(thisPosition: Point, targetPositions: List<Point>, data: Map<Point,CombatUnit>): Point? {
        val targets = targetPositions.filter { thisPosition.adjacent(false).contains(it) }.map { Pair(data[it]!!, it) }
        if (targets.isEmpty())
            return null
        return targets.sortedWith { t1, t2 ->
                   if (t1.first.hitPoints == t2.first.hitPoints) t1.second.compareTo(t2.second) else
                      t1.first.hitPoints.compareTo(t2.first.hitPoints)
            }
            .map{ it.second }
            .first()
    }

    private fun unitMoves(unitAPosition: Point, unitA: CombatUnit, enemyUnitsPositions: List<Point>, data: MutableMap<Point,CombatUnit>): Point? {
        val stepToNearestInRange = stepToNearestInRangePosition(unitAPosition, enemyUnitsPositions, data) ?: return null
        data[stepToNearestInRange] = CombatUnit.fromUnit(unitA)
        data.remove(unitAPosition)
        return stepToNearestInRange
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

    private fun unitAttacks(attackUnit: CombatUnit, enemyPosition: Point, data: MutableMap<Point,CombatUnit>) {
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

    /////////////// part 2

    fun findMinElfAttackPwr(): Int {
        val initialData = battleData.toMap()
        var high = 256
        var low = 0
        while (true) {
            battleData = initialData.toMutableMap()
            val attackPower = (high + low) / 2
            setElfsAttackPwr(attackPower, battleData)
            val (rounds, hitPts) = run(false)
            if (elfCount(battleData) == numOfElfs)
                high = attackPower
            else
                low = attackPower
            if (low + 1 >= high) {
                grid = Grid(battleData.entries.groupingBy { it.key }
                    .aggregate { _, _: AreaId?, element, _ -> element.value.id }, AreaId.mapper, border = 0)
                println("attack power: $attackPower rounds: $rounds, hit points: $hitPts")
                grid.print()
                return rounds * hitPts
            }
        }
    }

    private fun elfCount(data: MutableMap<Point,CombatUnit>) = data.values.count { it.id == AreaId.ELF }

    private fun setElfsAttackPwr(x: Int, data: MutableMap<Point,CombatUnit>) {
        data.filter { (_,unit) -> unit.id == AreaId.ELF }.forEach { (_,unit) -> unit.attackPower = x }
        data.forEach { (_,unit) -> unit.hitPoints = 200 }
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