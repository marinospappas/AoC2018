package mpdev.springboot.aoc2018.solutions.day15

import mpdev.springboot.aoc2018.utils.AocException
import mpdev.springboot.aoc2018.utils.Grid
import mpdev.springboot.aoc2018.utils.Point

class Combat(input: List<String>) {

    var grid = Grid(input, AreaId.mapper, border = 0)

    fun run() {
        val data = mutableMapOf<Point, CombatUnit>()
        grid.getDataPoints().forEach { (k,v) -> data[k] = CombatUnit(v) }
        while (true)
            if(!doRound(data))
                break
    }

    fun doRound(data: Map<Point,CombatUnit>): Boolean {
        for (x in grid.getMinMaxXY().x1 .. grid.getMinMaxXY().x2)
            for (y in grid.getMinMaxXY().x3 .. grid.getMinMaxXY().x4) {
                val thisUnit = data[Point(x,y)] ?: throw AocException("unit not found at ($x,$y)")
                if (thisUnit.id == AreaId.WALL)
                    continue
                val targetUnit = findTarget(thisUnit.id, x, y) ?: return false
            }
        return true
    }

    fun findTarget(id: AreaId, x: Int, y: Int): Point? {
        return null
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