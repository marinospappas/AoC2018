package mpdev.springboot.aoc2018.solutions.day14

import java.lang.StringBuilder

class Recipes(input: List<String>) {

    companion object {
        const val RECIPE_1 = 3
        const val RECIPE_2 = 7
    }

    val numberOfRecipes = input[0].split(' ')[0].toInt()
    val scores: StringBuilder = StringBuilder().append(RECIPE_1).append(RECIPE_2)
    val elfIndices = mutableListOf(0, 1)

    init {
        makeRecipe()
    }

    fun makeRecipe() {
        val sum = (scores[elfIndices[0]].digitToInt() + scores[elfIndices[1]].digitToInt()).toString()
        scores.append(sum)
    }

    fun increaseIndices() {
        val increment = Array(2) { scores[elfIndices[it]].digitToInt() + 1 }
        increment.indices.forEach { i -> elfIndices[i] = (elfIndices[i] + increment[i]) % scores.length }
    }

    fun getNext10RecipesAfterRepeat(count: Int): String {
        while(scores.length < count + 10) {
            makeRecipe()
            increaseIndices()
        }
        return scores.toString().substring(count, count+10)
    }

    fun getNumberOfRecipesBeforePattern(pattern: String): Int {
        makeRecipe()
        increaseIndices()
        while (!scores.takeLast(7).contains(pattern)) {
            makeRecipe()
            increaseIndices()
        }
        return scores.indexOf(pattern)
    }
}