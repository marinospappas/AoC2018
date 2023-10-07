package mpdev.springboot.aoc2018.solutions.day24

import mpdev.springboot.aoc2018.utils.AocException

class Input24 {

    var immuneGrpId = 0
    var infectionGrpId = 0

    fun processInput(input: List<String>, immuneSystem: MutableList<Army>, infection: MutableList<Army>) {
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
                    processGroup(state, line, immuneSystem, infection)
                2 -> processGroup(state, line, immuneSystem, infection)
            }
        }
    }

    private fun processGroup(state: Int, line: String, immuneSystem: MutableList<Army>, infection: MutableList<Army>) {
        // 148 units each with 31914 hit points (immune to radiation, cold, fire; weak to bludgeoning)
        //  with an attack that does 416 cold damage at initiative 4
        try {
            val match = Regex("""(\d+) units each with (\d+) hit points (.+)""").find(line)
            val (units, hitPoints, remaining) = match!!.destructured
            val (weak, immune) = processWeakImmune(remaining)
            val line1 = remaining.replace(Regex("""\(.*\)"""), "")
            val match1 = Regex(""" ?with an attack that does (\d+) ([a-z]+) damage at initiative (\d+)""").find(line1)
            val (damage, kindOfDamage, initiative) = match1!!.destructured
            if (state == 1)
                immuneSystem.add(Army(GroupName.ImmuneSys, immuneGrpId++, units.toInt(), hitPoints.toInt(), weak, immune, Pair(Immunity.fromString(kindOfDamage), damage.toInt()), initiative.toInt()))
            else
                infection.add(Army(GroupName.Infection, infectionGrpId++, units.toInt(), hitPoints.toInt(), weak, immune, Pair(Immunity.fromString(kindOfDamage), damage.toInt()), initiative.toInt()))
        }
        catch(e: Exception) {
            throw AocException("invalid line [$line]")
        }
    }

    private fun processWeakImmune(s: String): Pair<List<Immunity>,List<Immunity>> {
        val weak = mutableListOf<Immunity>()
        val immune = mutableListOf<Immunity>()
        if (s.first() == '(') {
            val weakImm = s.substring(1, s.indexOf(")")).split(';')
            weakImm.forEach { grp ->
                val arr = grp.removePrefix(" ").split(' ')
                for (i in 2 .. arr.lastIndex)
                    if (arr[0] == "weak")
                        weak.add(Immunity.fromString(arr[i].removeSuffix(",")))
                    else
                        immune.add(Immunity.fromString(arr[i].removeSuffix(",")))
            }
        }
        return Pair(weak, immune)
    }
}