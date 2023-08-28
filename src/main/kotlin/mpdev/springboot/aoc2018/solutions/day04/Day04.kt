package mpdev.springboot.aoc2018.solutions.day04

import mpdev.springboot.aoc2018.model.PuzzlePartSolution
import mpdev.springboot.aoc2018.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class Day04: PuzzleSolver() {

    final override fun setDay() {
        day = 4
    }

    init {
        setDay()
    }

    var result = 0
    lateinit var timetable: Timetable

    override fun initSolver(): Pair<Long,String> {
        val elapsed = measureTimeMillis {
            timetable = Timetable(inputData)
        }
        return Pair(elapsed, "milli-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            val guardId = timetable.findGuardMostAsleep()
            val minuteMostLikely = timetable.findMinuteMostFrequentlyAsleep(guardId)
            log.info("part 1. guard most asleep $guardId most likely at minute $minuteMostLikely")
            result = guardId * minuteMostLikely
        }
        return PuzzlePartSolution(1, result.toString(), elapsed)
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureTimeMillis {
            val (guardMostAsleep, minuteMostAsleep) = timetable.findGuardMostFreqAsleepOnMinute()
            log.info("part 1. guard most asleep $guardMostAsleep most likely at minute $minuteMostAsleep")
            result = guardMostAsleep * minuteMostAsleep
        }
        return PuzzlePartSolution(2, result.toString(), elapsed)
    }

}