package mpdev.springboot.aoc2018.solutions.day07

import mpdev.springboot.aoc2018.utils.AocException
import java.util.PriorityQueue

class Instructions(input: List<String>) {

    companion object {
        val TIME_SPENT_TEST = IntArray(26) { it + 1 }
        const val WORKERS_TEST = 2
    }

    val graph = mutableMapOf<Char,Step>()
    val revDependenciesMap = mutableMapOf<Char,MutableList<Char>>()
    val dependentSteps: Set<Char>

    var timeSpent = IntArray(26) { 60 + it + 1 }
    var workers = 5

    init {
        processInput(input)
        dependentSteps = graph.map { it.value.next }.flatten().toSet()
        // also add nodes that have no next steps to complete the graph
        dependentSteps.forEach { if (!graph.keys.contains(it)) graph[it] = Step(it) }
    }

    fun findFirstSteps(): List<Char> {
        return graph.keys.filter { !dependentSteps.contains(it) }
    }

    private fun allDependenciesComplete(id: Char) =
        revDependenciesMap[id] == null || revDependenciesMap[id]!!.all { graph[it]!!.completed }

    // part 1
    fun topologicalSortBfs(start: List<Char>): String {
        val result = mutableListOf<Char>()
        val queue = PriorityQueue<Char>().also { it.addAll(start) }
        val visited = mutableListOf<Char>().also { it.addAll(start) }
        while (queue.isNotEmpty()) {
            val current = queue.poll()
            result.add(current)
            graph[current]!!.completed = true
            graph[current]!!.next.forEach { connection ->
                if (!visited.contains(connection) && allDependenciesComplete(connection)) {
                    visited.add(connection)
                    queue.add(connection)
                }
            }
        }
        return result.joinToString("")
    }

    // part 2
    fun topologicalSortBfsMultiTasking(start: List<Char>): Pair<String,Int> {
        val IDLE = ' '
        val result = mutableListOf<Char>()
        val queue = PriorityQueue<Char>().also { it.addAll(start) }
        val visited = mutableListOf<Char>().also { it.addAll(start) }
        val current = CharArray(workers) { IDLE }
        val timeToComplete = IntArray(workers) { 0 }
        var clock = 0
        while (current.any { it != IDLE } || queue.isNotEmpty()) {
            // if a worker is idle then check if there is anything to pick up
            (0 until workers).forEach { i ->
                if (current[i] == IDLE && queue.isNotEmpty()) {
                    current[i] = queue.poll()
                    timeToComplete[i] = clock + timeSpent[ current[i] - 'A' ] - 1
                }
            }
            // if a task has been completed then mark it and take the necessary actions
            (0 until workers).forEach { i ->
                if (current[i] != IDLE && timeToComplete[i] == clock) {
                    val completedId = current[i]
                    current[i] = IDLE
                    result.add(completedId)
                    graph[completedId]!!.completed = true
                    graph[completedId]!!.next.forEach { connection ->
                        if (!visited.contains(connection) && allDependenciesComplete(connection)) {
                            visited.add(connection)
                            queue.add(connection)
                        }
                    }
                }
            }
            ++clock
        }
        return Pair(result.joinToString(""), clock)
    }

    private fun processInput(input: List<String>) {
        input.forEach { s ->
            //Step I must be finished before step Q can begin.
            val match = Regex("""Step ([A-Z]) must be finished before step ([A-Z]) can begin.""").find(s)
            try {
                val (step1Id, step2Id) = match!!.destructured
                if (graph[step1Id.first()] == null)
                    graph[step1Id.first()] = Step(step1Id.first(), next = mutableListOf(step2Id.first()))
                else
                    graph[step1Id.first()]!!.next.add(step2Id.first())
                // also update reverse dependencies
                if (revDependenciesMap[step2Id.first()] == null)
                    revDependenciesMap[step2Id.first()] = mutableListOf(step1Id.first())
                else
                    revDependenciesMap[step2Id.first()]!!.add(step1Id.first())
            } catch (e: Exception) {
                throw AocException("bad input line $s")
            }
        }
    }
}

data class Step(val id: Char, var completed: Boolean = false, var order: Int = 0, val next: MutableList<Char> = mutableListOf())