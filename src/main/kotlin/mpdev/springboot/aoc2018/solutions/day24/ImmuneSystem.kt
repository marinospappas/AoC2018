package mpdev.springboot.aoc2018.solutions.day24

class ImmuneSystem(input: List<String>) {

    val immuneSystem = listOf<Pair<Int,ImmuneGroup>>()
    val infection = listOf<Pair<Int,InfectionGroup>>()

    init {
        processInput(input)
    }

    private fun processInput(input: List<String>) {

    }
}

data class ImmuneGroup(var hitPoints: Int, val weakTo: List<Immunity>, val immuneTo: List<Immunity>, val inflictsDamage: Pair<Immunity,Int>, val initiative: Int)

data class InfectionGroup(var hitPoints: Int, val weakTo: List<Immunity>, val immuneTo: List<Immunity>, val inflictsDamage: Pair<Immunity,Int>, val initiative: Int)

enum class Immunity {
    Radiation,
    Fire,
    Slashing,
    Bludgeoning,
    Cold
}