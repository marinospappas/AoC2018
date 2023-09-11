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
        while (!scores.takeLast(7).joinToString("").contains(pattern)) {
            makeRecipe()
            increaseIndices()
        }
        return scores.joinToString("").indexOf(pattern)
    }

    private fun nextIndex(i: Int) = (i + 1) % scores.size
}