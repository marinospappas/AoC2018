package mpdev.springboot.aoc2018.solutions.day24

import java.util.*
import kotlin.math.min

class ImmuneSystem(input: List<String>) {

    var debug = false
    val antibodies = mutableMapOf<Int,Army>()
    val infection = mutableMapOf<Int,Army>()

    init {
        Input24().processInput(input, antibodies, infection)
        antibodies.forEach { it.value.enemy = infection }
        infection.forEach { it.value.enemy = antibodies }
    }

    fun selectTargets() {
        val allUnits = mutableListOf<Army>().also { it.addAll(infection.values) }.also { it.addAll(antibodies.values) }
        val groupA = allUnits.filter { it.numOfUnits > 0 }. sortedWith { u1, u2 ->
            if (u2.effPwr() == u1.effPwr())
                u2.initiative.compareTo(u1.initiative)
            else
                u2.effPwr().compareTo(u1.effPwr())
        }
        groupA.forEach {
            grp -> grp.currentTarget = selectTgt(grp, grp.enemy.entries.filter { it.value.numOfUnits > 0 && !it.value.selectedAsTarget })
            if (debug) println("unit ${grp.name}.${grp.id} select target ${grp.enemy[grp.currentTarget]}")
            if (grp.currentTarget > 0)
                grp.enemy[grp.currentTarget]!!.selectedAsTarget = true
        }
    }

    fun battle(): Pair<GroupName,Int> {
        while(antibodies.count { it.value.numOfUnits > 0 } > 0 && infection.count { it.value.numOfUnits > 0 } > 0) {
            reset()
            selectTargets()
            attack()
        }
        return Pair((antibodies + infection).values.first { it.numOfUnits > 0 }.name,
            antibodies.values.sumOf { it.numOfUnits } + infection.values.sumOf { it.numOfUnits })
    }

    fun reset() {
        antibodies.forEach { it.value.selectedAsTarget = false; it.value.currentTarget = -1 }
        infection.forEach { it.value.selectedAsTarget = false; it.value.currentTarget = -1 }
    }

    private fun selectTgt(attackGrp: Army, enemy: List<Map.Entry<Int,Army>>): Int {
        val enemySorted = enemy.sortedWith { e1, e2 ->
            val damage1 = calcDamage(attackGrp, e1.value)
            val damage2 = calcDamage(attackGrp, e2.value)
            if (damage2 == damage1) {
                if (e2.value.effPwr() == e1.value.effPwr())
                    e2.value.initiative.compareTo(e1.value.initiative)
                else
                    (e2.value.effPwr()).compareTo(e1.value.effPwr())
            }
            else {
                damage2.compareTo(damage1)
            }
        }
        return if (enemySorted.isEmpty() || calcDamage(attackGrp, enemySorted.first().value) == 0) -1
        else  enemySorted.first().key
    }

    fun calcDamage(attackGrp: Army, defendGrp: Army): Int {
        if (defendGrp.immuneTo.contains(attackGrp.inflictsDamage.first))
            return 0
        val damage = attackGrp.numOfUnits * attackGrp.inflictsDamage.second
        return if (defendGrp.weakTo.contains(attackGrp.inflictsDamage.first))
            damage * 2
        else damage
    }

    fun attack() {
        mutableListOf<Army>()
            .also { it.addAll(antibodies.values) }
            .also { it.addAll(infection.values) }
            .filter { it.numOfUnits > 0 && it.currentTarget >= 0 }
            .sortedBy { it.initiative }.reversed().forEach { unit ->
                unitAttacks(unit, unit.enemy)
            }
    }

    private fun unitAttacks(unit: Army, enemyMap: Map<Int,Army>) {
        val enemyUnit = enemyMap[unit.currentTarget]!!
        val damage = calcDamage(unit, enemyUnit)
        val killUnits = min(calcDamage(unit, enemyUnit) / enemyUnit.hitPoints, enemyUnit.numOfUnits)
        enemyUnit.numOfUnits -= killUnits
        if (debug) println("unit ${unit.name}.${unit.id} attacks ${enemyUnit.name}.${unit.id} and deals $damage damage - killing $killUnits units (${enemyUnit.numOfUnits} left)")
    }
}

data class Army(var name: GroupName, var id: Int, var numOfUnits: Int, var hitPoints: Int, val weakTo: List<Immunity>, val immuneTo: List<Immunity>, val inflictsDamage: Pair<Immunity,Int>, val initiative: Int) {
    var currentTarget = -1
    var selectedAsTarget = false
    var enemy: Map<Int,Army> = emptyMap()
    fun effPwr() = numOfUnits * inflictsDamage.second
}

enum class GroupName {
    ImmuneSys, Infection
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