package mpdev.springboot.aoc2018.solutions.day04

import mpdev.springboot.aoc2018.utils.AocException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class GuardData(var date: LocalDate = LocalDate.now(),
                     val guardId: Int = -1,
                     val sleepTimes: MutableList<Pair<LocalTime, LocalTime>> = mutableListOf())

enum class GuardEvent {
    BEGIN_SHIFT, SLEEP, WAKE_UP
}

data class GuardRecord(val s: String) {
    val timestamp: LocalDateTime
    val guardId: Int
    val event: GuardEvent
    init {
        // [1518-08-21 00:01] Guard #1301 begins shift
        // [1518-07-21 00:36] falls asleep
        // [1518-04-08 00:59] wakes up
        val match = Regex("""\[([\d -:]{16})] (.+)""").find(s)
        try {
            val (timestampStr, remainingStr) = match!!.destructured
            timestamp = LocalDateTime.parse(timestampStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            when (remainingStr) {
                "falls asleep" -> { guardId = -1; event = GuardEvent.SLEEP }
                "wakes up" -> { guardId = -1; event = GuardEvent.WAKE_UP }
                else ->  {
                    val match1 = Regex("""Guard #(\d+) begins shift""").find(remainingStr)
                    val (idStr) = match1!!.destructured
                    guardId = idStr.toInt()
                    event = GuardEvent.BEGIN_SHIFT
                }
            }
        } catch (e: Exception) {
            throw AocException("bad input line $s")
        }
    }
    override fun toString() = "Record: guard: $guardId, timestamp: $timestamp, event: $event"
}