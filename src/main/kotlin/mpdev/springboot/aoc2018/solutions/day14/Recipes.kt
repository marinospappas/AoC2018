package mpdev.springboot.aoc2018.solutions.day14

class Recipes(input: List<String>) {

    companion object {
        const val RECIPE_1 = 3
        const val RECIPE_2 = 7
    }
    val numberOfRecipes = input[0].split(' ')[0].toInt()
    val recipes = mutableListOf(RECIPE_1, RECIPE_2)
    val elfScore = mutableListOf(RECIPE_1, RECIPE_2)

    fun makeRecipe() {

    }

}