package mpdev.springboot.aoc2018.solutions.day25

import mpdev.springboot.aoc2018.utils.PointND

class Constellations(input: List<String>) {

    var data = input.map { line -> PointND(line.split(',').map { s -> s.trim().toInt() }.toIntArray()) }

    var constList = mutableListOf<MutableList<PointND>>()

    fun identifyConstellations() {
        constList = mutableListOf(mutableListOf(data.first()))
        while (constPoints.isNotEmpty()) {
            for (c in constList) {
                var newPoint: PointND? = null
                for (p1 in c) {
                    for (p in constPoints) {
                        newPoint = p
                        if (p.manhattan(p1) <= 3) {
                            c.add(p)
                            constPoints.remove(p)
                            newPoint = null
                            break
                        }
                    }
                }
            }

            val constPoints = data.toMutableList().also { it.removeFirst() }
            while (constPoints.isNotEmpty()) {
                var addedToExisting = false
                constPoints.forEach { p ->
                    for (i in constList.indices) {
                        for (j in constList[i].indices) {
                            if (p.manhattan(constList[i][j]) <= 3) {
                                constList[i].add(p)
                                addedToExisting = true
                                break
                            }
                        }
                    }
                }
                if (!addedToExisting)
                    constList.add(mutableListOf(p))
            }
        }
    }
}