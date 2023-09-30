package mpdev.springboot.aoc2018.solutions.day15

import mpdev.springboot.aoc2018.utils.*

class Combat(input: List<String>) {

    var grid = Grid(input, AreaId.mapper, border = 0)

    fun run() {
        val data = grid.getDataPoints().entries.groupingBy { it.key }.aggregate { _, _: CombatUnit?, element, _ -> CombatUnit(element.value)  }
        while (true)
            if(!doRound(data))
                break
    }

    fun doRound(data: Map<Point,CombatUnit>): Boolean {
        for (x in grid.getMinMaxXY().x1 .. grid.getMinMaxXY().x2)
            for (y in grid.getMinMaxXY().x3 .. grid.getMinMaxXY().x4) {
                val thisUnitPosition = Point(x, y)
                val thisUnit = data[thisUnitPosition] ?: throw AocException("unit not found at ($x,$y)")
                if (thisUnit.id == AreaId.WALL)
                    continue
                val targetUnitPositions = findTargetPostions(thisUnit.id, data).also { if (it.isEmpty()) return false }
                val nearestInRange = findNearestInRangePosition(thisUnitPosition, targetUnitPositions, data) ?: return false
            }
        return true
    }

    fun findTargetPostions(id: AreaId, data: Map<Point,CombatUnit>): List<Point> {
        val targetId = if (id == AreaId.ELF) AreaId.GOBLIN else AreaId.ELF
        return data.entries.filter { e -> e.value.id == targetId }.map { it.key }
    }

    fun findNearestInRangePosition(unitAPosition: Point, enemyUnitsPositions: List<Point>, data: Map<Point,CombatUnit>): Point? {
        var points = mutableListOf<Point>()
        enemyUnitsPositions.forEach { point ->
            point.adjacent(false).forEach { adjPt -> if (data[adjPt] == null) points.add(adjPt) }
        }
        points = points.distinct().toMutableList()
        val graph = setupGraph(data)
        val dijkstra = Dijkstra<Point>()
        val inRangeAndDistance = mutableListOf<Pair<Point,Int>>()
        points.forEach { p ->
            val minPath: MinCostPath<Point>
            try {
                minPath = dijkstra.runIt(graph[unitAPosition], graph[p])
                inRangeAndDistance.add(Pair(p, minPath.minCost))
            }
            catch (_: Exception){}
        }
        return if (inRangeAndDistance.isEmpty()) null else
            inRangeAndDistance.sortedWith {
                    p1,p2 -> if (p1.second == p2.second) p1.first.compareTo(p2.first) else p1.second.compareTo(p2.second)
            }.first().first
    }

    fun setupGraph(data: Map<Point,CombatUnit>): Graph<Point> {
        val graph = Graph<Point>()

        return graph
    }
}

data class CombatUnit(val id: AreaId, var attackPower: Int = 3, var hitPoints: Int = 200)

enum class AreaId(val value: Char) {
    WALL('#'),
    ELF('E'),
    GOBLIN('G');
    companion object {
        val mapper: Map<Char, AreaId> = AreaId.values().associateBy { it.value }
    }
}