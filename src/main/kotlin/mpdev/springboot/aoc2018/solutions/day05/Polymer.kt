package mpdev.springboot.aoc2018.solutions.day05

class Polymer(input: List<String>) {

    val data = input[0]

    lateinit var reducedData: String

    fun doReaction(s: String, debug: Boolean = false): String {
        var polymerData = s
        var index = 0
        if (debug) println(polymerData)
        while (true) {
            if (index >= polymerData.length-1)
                return polymerData
            if (polymerData[index].isLowerCase() && polymerData[index+1] == polymerData[index].uppercaseChar()
                || polymerData[index].isUpperCase() && polymerData[index+1] == polymerData[index].lowercaseChar()) {
                polymerData = polymerData.removeRange(index, index+2)
                if (debug) println(polymerData)
                if (index > 0)
                    --index
            }
            else
                ++index
        }
    }

    fun removeElementAndDoReaction(debug: Boolean = false): Map<Int,String> {
        val improvedPolymerMap = mutableMapOf<Int,String>()
        (0 until 26).forEach { i ->
            val polymerData = reducedData.replace("${'A'+i}", "").replace("${'a'+i}", "")
            improvedPolymerMap[i] = doReaction(polymerData, debug)
            if (debug) println("remove ${'A' + i}/${'a' + i} result $polymerData")
        }
        return improvedPolymerMap
    }
}