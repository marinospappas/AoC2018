package mpdev.springboot.aoc2018.solutions.day09

import mpdev.springboot.aoc2018.utils.AocException

class MarbleGame(input: List<String>) {

    var numberOfPlayers: Int
    var maxMarbleId: Int
    var highScore: Int
    val marbles: ListNode
    var current: ListNode

    init {
        val match = Regex("""(\d+) players; last marble is worth (\d+) points: high score is (\d+)""").find(input[0])
        try {
            val (players, lastMarble, score) = match!!.destructured
            numberOfPlayers = players.toInt()
            maxMarbleId = lastMarble.toInt()
            highScore = score.toInt()
        } catch (e: Exception) {
            throw AocException("bad input line ${input[0]}")
        }
        marbles = ListNode(0).also { it.previous = it; it.next = it }
        current = marbles
    }
}

data class ListNode(val id: Int, var previous: ListNode? = null, var next: ListNode? = null) {
    override fun toString() = "marble: $id, next: ${next?.id}, previous: ${previous?.id}"
}