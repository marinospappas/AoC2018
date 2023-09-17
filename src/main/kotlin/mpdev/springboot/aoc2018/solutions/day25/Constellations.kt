package mpdev.springboot.aoc2018.solutions.day25

import mpdev.springboot.aoc2018.utils.PointND

class Constellations(input: List<String>) {

    var data = input.map { line -> PointND(line.split(',').map { s -> s.trim().toInt() }.toIntArray()) }

    var constList = mutableListOf<MutableList<PointND>>()

    fun identifyConstellations() {
        constList = mutableListOf(mutableListOf(data.first()))
        var toProcess = data.toMutableList().also{ it.removeFirst() }
        while (toProcess.isNotEmpty()) {
            val newListToProcess = mutableListOf<PointND>()
            for (p in toProcess) {
                var pProcessed = false
                outerloop@ for (constPts in constList) {
                    for (p1 in constPts) {
                        if (p.manhattan(p1) <= 3) {
                            constPts.add(p)
                            pProcessed = true
                            break@outerloop
                        }
                    }
                }
                if (!pProcessed)
                    newListToProcess.add(p)
            }
            if (newListToProcess == toProcess)  // this is the case where none of the remaining points belong to any identified constellation
                constList.add(mutableListOf(newListToProcess.removeFirst()))    // in this case add a new constellation
            toProcess = newListToProcess
        }
    }
}