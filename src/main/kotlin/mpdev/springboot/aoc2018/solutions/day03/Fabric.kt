package mpdev.springboot.aoc2018.solutions.day03

import mpdev.springboot.aoc2018.utils.AocException
import mpdev.springboot.aoc2018.utils.Grid
import java.awt.Point

class Fabric(input: List<String>) {

    val claims = mutableMapOf<Int,Grid<FabricPoint>>()

    init {
        processInput(input)
    }

    fun findOverlappingPoints(): Set<Point> {
        val overlappingList = mutableListOf<Set<Point>>()
        val claimsGrids = claims.values.toList()
        for (i in 0 .. claimsGrids.lastIndex-1)
            for (j in i+1 .. claimsGrids.lastIndex) {
                val overlap = overlappingPoints(claimsGrids[i], claimsGrids[j])
                if (overlap.isNotEmpty())
                    overlappingList.add(overlap)
            }
        return overlappingList.fold(setOf()) { acc, points -> acc union points }
    }

    fun findNonOverlappingClaim(): Int {
        val claimsGrids = claims.values.toList()
        for (i in 0 .. claimsGrids.lastIndex) {
            var overlaps = false
            for (j in 0..claimsGrids.lastIndex) {
                if (i == j)
                    continue
                val overlap = overlappingPoints(claimsGrids[i], claimsGrids[j])
                if (overlap.isNotEmpty()) {
                    overlaps = true
                    break
                }
            }
            if (!overlaps)
                return claims.keys.toList()[i]
        }
        return -1
    }

    private fun overlappingPoints(grid1: Grid<FabricPoint>, grid2: Grid<FabricPoint>) =
        grid1.getDataPoints().keys intersect grid2.getDataPoints().keys

    private fun processInput(input: List<String>) {
        input.forEach { line -> createClaim(line) }
    }

    private fun createClaim(s: String) {
        // #1 @ 1,3: 4x4
        val match = Regex("""#(\d+) @ (\d+),(\d+): (\d+)x(\d+)""").find(s)
        try {
            val (id, x0, y0, width, height) = match!!.destructured
            val gridData = mutableMapOf<Point,FabricPoint>()
            (0 until width.toInt()).forEach { w ->
                (0 until height.toInt()).forEach { h ->
                    gridData[Point(x0.toInt()+w, y0.toInt()+h)] = FabricPoint.CLAIMED
                }
            }
            claims[id.toInt()] = Grid(gridData, FabricPoint.mapper)
        } catch (e: Exception) {
            throw AocException("bad input line $s")
        }
    }

}

enum class FabricPoint(val value: Char) {
    CLAIMED('#');
    //FREE('.');

    companion object {
        val mapper: Map<Char,FabricPoint> = values().associateBy { it.value }
    }
}