package mpdev.springboot.aoc2018.day04

import mpdev.springboot.aoc2018.input.InputDataReader
import mpdev.springboot.aoc2018.solutions.day04.Day04
import mpdev.springboot.aoc2018.solutions.day04.Timetable
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import java.time.temporal.ChronoUnit

class Day04Test {

    private val day = 4                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day04()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
    private var inputLines: List<String> = inputDataReader.read(day)

    @BeforeEach
    fun setup() {
        puzzleSolver.setDay()
        puzzleSolver.inputData = inputLines
        puzzleSolver.initSolver()
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(puzzleSolver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Reads Input and sets Guards Records`() {
        val timetable = Timetable(inputLines)
        timetable.guardRecords.forEach { println(it) }
        timetable.guardDataList.forEach { println(it) }
        assertThat(timetable.guardRecords.size).isEqualTo(17)
        assertThat(timetable.guardDataList.size).isEqualTo(5)
        assertThat(timetable.guardDataList[0].sleepTimes.size).isEqualTo(2)
        assertThat(timetable.guardDataList[1].sleepTimes.size).isEqualTo(1)
        assertThat(timetable.guardDataList[2].sleepTimes.size).isEqualTo(1)
        assertThat(timetable.guardDataList[3].sleepTimes.size).isEqualTo(1)
        assertThat(timetable.guardDataList[4].sleepTimes.size).isEqualTo(1)
    }

    @Test
    @Order(3)
    fun `Finds Guard with most minutes asleep`() {
        val timetable = Timetable(inputLines)
        val minutesAsleepMap = timetable.guardDataList
            .groupingBy { it.guardId }
            .aggregate { _, accumulator: Long?, element, _ ->
                (accumulator ?: 0) + element.sleepTimes.sumOf { ChronoUnit.MINUTES.between(it.first, it.second) + 1 } }
        val maxMinutesAsleep = minutesAsleepMap.values.max()
        val guardId = timetable.findGuardMostAsleep()
        println("total minutes asleep asleep (each guard: $minutesAsleepMap")
        println("guard most asleep: $guardId, $maxMinutesAsleep")
        assertThat(guardId).isEqualTo(10)
        assertThat(maxMinutesAsleep).isEqualTo(50)
        val minuteMostLikely = timetable.findMinuteMostFrequentlyAsleep(guardId)
        println("minute most likely asleep: $minuteMostLikely")
        assertThat(minuteMostLikely).isEqualTo(24)
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("240")
    }

    @Test
    @Order(5)
    fun `Finds Guard most frequently asleep on minute N`() {
        val timetable = Timetable(inputLines)
        val guardMostAsleep = timetable.findGuardMostFreqAsleepOnMinute()
        println("guard most asleep: $guardMostAsleep")
        assertThat(guardMostAsleep.first).isEqualTo(99)
        assertThat(guardMostAsleep.second).isEqualTo(45)
    }

    @Test
    @Order(6)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("4455")
    }
}
