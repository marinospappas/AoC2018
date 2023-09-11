package mpdev.springboot.aoc2018.solutions.day14

class Recipes(input: List<String>) {

    companion object {
        const val RECIPE_1 = 3
        const val RECIPE_2 = 7
    }

    val numberOfRecipes = input[0].split(' ')[0].toInt()
    val scores = mutableListOf(RECIPE_1, RECIPE_2)
    val elfIndices = mutableListOf(0, 1)

    init {
        makeRecipe()
    }

    fun makeRecipe() {
        val sum = (scores[elfIndices[0]] + scores[elfIndices[1]]).toString()
        sum.toCharArray().forEach { c -> scores.add(c.digitToInt()) }
    }

    fun increaseIndices() {
        val increment = Array(2) { scores[elfIndices[it]] + 1 }
        increment.indices.forEach { i ->
            repeat(increment[i]) { elfIndices[i] = nextIndex(elfIndices[i]) }
        }
    }

    fun getNext10RecipesAfterRepeat(count: Int): String {
        while(scores.size < count + 10) {
            makeRecipe()
            increaseIndices()
        }
        return scores.subList(count, count+10).joinToString("")
    }

    fun getNumberOfRecipesBeforePattern(pattern: String): Int {
        makeRecipe()
        increaseIndices()
        var count = 0
        while (scores.subList(scores.size-pattern.length, scores.size).joinToString("") != pattern) {
            makeRecipe()
            increaseIndices()
            if (++count % 1000000 == 0)
                println("count ${count}")
        }
        return scores.size - pattern.length
    }

    fun part2() {
        val scores = mutableListOf(3, 7)
        var i = 0 to 1
        while (numberOfRecipes.toString() !in scores.takeLast(10).joinToString("")) {
            scores += (scores[i.first] + scores[i.second]).toString().map(Character::getNumericValue)
            i = ((i.first + scores[i.first] + 1) % scores.size) to ((i.second + scores[i.second] + 1) % scores.size)
        }
        println(scores.joinToString("").indexOf(numberOfRecipes.toString()))
    }

    private fun nextIndex(i: Int) = (i + 1) % scores.size
}