package mpdev.springboot.aoc2018.solutions.day24

import java.util.*

class ImmuneSystem(input: List<String>) {

    val immuneSystem = mutableListOf<Pair<Int,ImmuneGroup>>()
    val infection = mutableListOf<Pair<Int,InfectionGroup>>()

    init {
        processInput(input)
    }

    private fun processInput(input: List<String>) {
        var state = 0
        input.forEach { line ->
            if (line.isEmpty())
                return@forEach
            when (state) {
                0 -> if (line == "Immune System:")
                        ++state
                1 -> if (line == "Infection:")
                        ++state
                    else
                        processGroup(state, line)
                2 -> processGroup(state, line)

            }
        }
    }

    private fun processGroup(state: Int, line: String) {
        // 148 units each with 31914 hit points (immune to radiation, cold, fire; weak to bludgeoning)
        //  with an attack that does 416 cold damage at initiative 4
        val match = Regex("""(\d+) units each with (\d+) hit points (.+)""").find(line)
        val (units, hitPoints, remaining) = match!!.destructured
        val (weak, immune) = processWeakImmune(remaining)
        val line1 = if (remaining.first() == '(')
            remaining.replace(Regex("""\(.*\)"""), "")
        else
            remaining
        val match1 = Regex(""" with an attack that does (\d+) ([a-z]+) damage at initiative (\d+)""").find(line1)
        val (damage, kindOfDamage, initiative) = match1!!.destructured
        if (state == 1)
            immuneSystem.add(Pair(units.toInt(), ImmuneGroup(hitPoints.toInt(), weak, immune, Pair(Immunity.fromString(kindOfDamage), damage.toInt()), initiative.toInt())))
        else
            infection.add(Pair(units.toInt(), InfectionGroup(hitPoints.toInt(), weak, immune, Pair(Immunity.fromString(kindOfDamage), damage.toInt()), initiative.toInt())))
    }

    private fun processWeakImmune(s: String): Pair<List<Immunity>,List<Immunity>> {
        val weak = mutableListOf<Immunity>()
        val immune = mutableListOf<Immunity>()
        if (s.first() == '(') {
            //TODO weak/immune
        }
        return Pair(weak, immune)
    }
}

data class ImmuneGroup(var hitPoints: Int, val weakTo: List<Immunity>, val immuneTo: List<Immunity>, val inflictsDamage: Pair<Immunity,Int>, val initiative: Int)

data class InfectionGroup(var hitPoints: Int, val weakTo: List<Immunity>, val immuneTo: List<Immunity>, val inflictsDamage: Pair<Immunity,Int>, val initiative: Int)

enum class Immunity {
    Radiation,
    Fire,
    Slashing,
    Bludgeoning,
    Cold;
    companion object {
        fun fromString(s: String) = Immunity.valueOf(s.lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
    }
}