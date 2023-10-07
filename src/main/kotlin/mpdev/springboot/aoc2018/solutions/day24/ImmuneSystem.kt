package mpdev.springboot.aoc2018.solutions.day24

import java.util.*
import kotlin.math.min

class ImmuneSystem(input: List<String>) {

    var debug = false
    var antibodies = mutableListOf<Army>()
    var infection = mutableListOf<Army>()

    init {
        Input24().processInput(input, antibodies, infection)
    }

    fun battle(): Pair<GroupName,Int> {
        while(antibodies.any { it.numOfUnits > 0 } && infection.any { it.numOfUnits > 0 }) {
            resetTargets()
            selectTargets()
            if (antibodies.none { it.currentTarget >= 0 } && infection.none { it.currentTarget >= 0 })
                break
            val numOfUnitsBefore = (antibodies + infection).sumOf { it.numOfUnits }
            attack()
            val numOfUnitsAfter = (antibodies + infection).sumOf { it.numOfUnits }
            if (numOfUnitsBefore == numOfUnitsAfter)     // check for no more kills (draw)
                break
        }
        val winner = if ((antibodies + infection).filter { it.numOfUnits > 0 }.map { it.name }.distinct().size == 1)
            (antibodies + infection).first { it.numOfUnits > 0 }.name
        else GroupName.NA
        return Pair(winner, (antibodies + infection).sumOf { it.numOfUnits })
    }

    fun resetTargets() {
        antibodies.forEach { it.selectedAsTarget = false; it.currentTarget = -1; it.enemy = infection }
        infection.forEach { it.selectedAsTarget = false; it.currentTarget = -1; it.enemy = antibodies }
    }

    fun selectTargets() {
        val allUnits = mutableListOf<Army>().also { it.addAll(infection) }.also { it.addAll(antibodies) }
        val groupA = allUnits.filter { it.numOfUnits > 0 }.sortedWith { u1, u2 ->
            if (u2.effPwr() == u1.effPwr())
                u2.initiative.compareTo(u1.initiative)
            else
                u2.effPwr().compareTo(u1.effPwr())
        }
        groupA.forEach {
            grp -> grp.currentTarget = selectTgt(grp, grp.enemy.filter { it.numOfUnits > 0 && !it.selectedAsTarget })
            if (debug) println("unit ${grp.name}.${grp.id} select target ${if(grp.currentTarget >= 0) grp.enemy[grp.currentTarget] else null}")
            if (grp.currentTarget >= 0)
                grp.enemy[grp.currentTarget].selectedAsTarget = true
        }
    }

    private fun selectTgt(attackGrp: Army, enemy: List<Army>): Int {
        val enemySorted = enemy.sortedWith { e1, e2 ->
            val damage1 = calcDamage(attackGrp, e1)
            val damage2 = calcDamage(attackGrp, e2)
            if (damage2 == damage1) {
                if (e2.effPwr() == e1.effPwr())
                    e2.initiative.compareTo(e1.initiative)
                else
                    (e2.effPwr()).compareTo(e1.effPwr())
            }
            else {
                damage2.compareTo(damage1)
            }
        }
        return if (enemySorted.isEmpty() || calcDamage(attackGrp, enemySorted.first()) == 0) -1
        else  enemySorted.first().id
    }

    fun calcDamage(attackGrp: Army, defendGrp: Army): Int {
        if (defendGrp.immuneTo.contains(attackGrp.inflictsDamage.first))
            return 0
        val damage = attackGrp.effPwr()
        return if (defendGrp.weakTo.contains(attackGrp.inflictsDamage.first))
            damage * 2
        else damage
    }

    fun attack() {
        mutableListOf<Army>()
            .also { it.addAll(antibodies) }
            .also { it.addAll(infection) }
            .filter { it.numOfUnits > 0 && it.currentTarget >= 0 }
            .sortedBy { it.initiative }.reversed().forEach { unit ->
                unitAttacks(unit, unit.enemy)
            }
    }

    private fun unitAttacks(unit: Army, enemyMap: List<Army>) {
        if (unit.numOfUnits <= 0)   // check in case this group's units have been killed in the meantime
            return
        val enemyUnit = enemyMap[unit.currentTarget]
        val damage = calcDamage(unit, enemyUnit)
        val killUnits = min(damage / enemyUnit.hitPoints, enemyUnit.numOfUnits)
        enemyUnit.numOfUnits -= killUnits
        if (debug) println("unit ${unit.name}.${unit.id} attacks ${enemyUnit.name}.${unit.id} and deals $damage damage - killing $killUnits units (${enemyUnit.numOfUnits} left)")
    }

    ///////// part 2

    fun findMinBoost(): Triple<GroupName,Int,Int> {
        var high = 4096
        var low = 0
        val origAntibodies = antibodies.map { it.clone() }
        val origInfection = infection.map { it.clone() }
        while (true) {
            val boost = (high + low) / 2
            val (winning, _) = tryBoost(boost, origAntibodies, origInfection)
            if (winning == GroupName.ImmuneSys)
                high = boost
            else
                low = boost
            if (low + 1 >= high) {
                val (winningFinal, remUnitsFinal) = tryBoost(high, origAntibodies, origInfection)
                return Triple(winningFinal, remUnitsFinal, high)
            }
        }
    }

    private fun tryBoost(boost: Int, immuneArmy: List<Army>, infectionArmy: List<Army>): Pair<GroupName,Int> {
        antibodies = immuneArmy.map { it.clone() }.toMutableList()
        infection = infectionArmy.map { it.clone() }.toMutableList()
        boostImmuneUnits(boost)
        val (winning, remUnits) = battle()
        if (debug) println("immune boost of $boost: $winning won $remUnits units remaining")
        return Pair(winning, remUnits)
    }

    private fun boostImmuneUnits(boost: Int) {
        antibodies.forEach { it.boost = boost }
    }
}

data class Army(var name: GroupName, var id: Int, var numOfUnits: Int, var hitPoints: Int, val weakTo: List<Immunity>, val immuneTo: List<Immunity>,
                val inflictsDamage: Pair<Immunity,Int>, val initiative: Int) {
    var currentTarget = -1
    var selectedAsTarget = false
    var enemy: List<Army> = emptyList()
    var boost = 0
    fun effPwr() = numOfUnits * (inflictsDamage.second + boost)
    fun clone() = Army(name,id,numOfUnits,hitPoints,weakTo.toList(),immuneTo.toList(),Pair(inflictsDamage.first,inflictsDamage.second),initiative)
}

enum class GroupName {
    ImmuneSys, Infection, NA
}

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