package mpdev.springboot.aoc2018.solutions.day04

import java.time.LocalTime
import java.time.temporal.ChronoUnit

class Timetable(input: List<String>) {

    var guardRecords: List<GuardRecord>
    var guardDataList: List<GuardData>

    init {
        guardRecords = input.map { GuardRecord(it) }.sortedBy { it.timestamp }
        guardDataList = processInput(guardRecords)
   }

    fun findGuardMostAsleep() =
        guardDataList.groupingBy { it.guardId }
            .aggregate { _, accumulator: Long?, element, _ ->
                (accumulator ?: 0) + element.sleepTimes.sumOf { ChronoUnit.MINUTES.between(it.first, it.second) + 1 } }
            .entries.maxBy { e -> e.value }.key

    fun findMinuteMostFrequentlyAsleep(id: Int) =
        (0..59).groupingBy { it }.aggregate { _, _: Int?, element, _ -> countDatesAsleepOnMinute(id, element) }
            .entries.maxBy { e -> e.value }.key

    fun findGuardMostFreqAsleepOnMinute(): Pair<Int,Int> {
        val guardIDs = guardDataList.map { it.guardId }.toSet()
        val guardsAsleepByMinuteMap = mutableMapOf<Pair<Int,Int>,Int>()
        (0 .. 59).forEach { minute ->
            guardIDs.forEach { id -> guardsAsleepByMinuteMap[Pair(id,minute)] = countDatesAsleepOnMinute(id, minute) }
        }
        return guardsAsleepByMinuteMap.entries.maxBy { e -> e.value }.key
    }

    private fun countDatesAsleepOnMinute(id: Int, minute: Int) =
        guardDataList.filter { it.guardId == id }.count { record ->
            var asleep = false
            val time = LocalTime.of(0, minute)
            for (sleepTime in record.sleepTimes) {
                if (!time.isBefore(sleepTime.first) && !time.isAfter(sleepTime.second))
                    asleep = true
            }
            asleep
        }

    private fun processInput(records: List<GuardRecord>): List<GuardData> {
        val guardDataList = mutableListOf<GuardData>()
        var guardData = GuardData()
        for (i in records.indices) {
            val record = records[i]
            when (record.event) {
                GuardEvent.BEGIN_SHIFT -> {
                    if (i > 0)
                        guardDataList.add(guardData)
                    guardData = GuardData(guardId = record.guardId)
                }
                GuardEvent.SLEEP -> {
                    guardData.date = record.timestamp.toLocalDate()
                    guardData.sleepTimes.add(Pair(record.timestamp.toLocalTime(), LocalTime.of(0, 59)))
                }
                GuardEvent.WAKE_UP -> guardData.sleepTimes[guardData.sleepTimes.lastIndex] =
                    Pair(guardData.sleepTimes.last().first, record.timestamp.toLocalTime().minusMinutes(1))
            }
        }
        guardDataList.add(guardData)
        return guardDataList
    }
}

