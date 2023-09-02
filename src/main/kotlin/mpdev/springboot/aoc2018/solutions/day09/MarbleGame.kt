package mpdev.springboot.aoc2018.solutions.day09

import mpdev.springboot.aoc2018.utils.AocException

class MarbleGame(input: List<String>) {

    var numberOfPlayers: Int
    var maxMarbleId: Int
    var highScore = 0
    val marbles: ListNode
    var current: ListNode
    var idPlayed: Int
    val scoreMap: MutableMap<Int,Long>
    var curPlayer: Int

    init {
        val match = Regex("""(\d+) players; last marble is worth (\d+) points(.*)?""").find(input[0])
        try {
            val (players, lastMarble, s) = match!!.destructured
            numberOfPlayers = players.toInt()
            maxMarbleId = lastMarble.toInt()
            if (s.isNotEmpty()) {
                val match1 = Regex(""": high score is (\d+)?""").find(s)
                val (score) = match1!!.destructured
                highScore = score.toInt()
            }
        } catch (e: Exception) {
            throw AocException("bad input line ${input[0]}")
        }
        val start = Array(3) { ListNode(it) }
        start[0].next = start[2]
        start[2].previous = start[0]
        start[2].next = start[1]
        start[1].previous = start[2]
        start[1].next = start[0]
        start[0].previous = start[1]
        marbles = start[0]
        current = start[2]
        idPlayed = 2
        curPlayer = 2
        scoreMap = (1..numberOfPlayers).groupingBy { it }.aggregate { _, _:Long?, _, _ -> 0L }.toMutableMap()
    }

    fun playMarble() {
        if (++curPlayer > numberOfPlayers)
            curPlayer = 1
        if (++idPlayed % 23 == 0)
            playMarble23()
        else
            playNormalMarble()
    }

    private fun playNormalMarble() {
        val nextMarble = ListNode(idPlayed)
        val next1 = current.next
        val next2 = next1?.next
        next1?.next = nextMarble
        nextMarble.previous = next1
        nextMarble.next = next2
        next2?.previous = nextMarble
        current = nextMarble
    }

    private fun playMarble23() {
        scoreMap[curPlayer] = scoreMap[curPlayer]?.plus(idPlayed) ?: throw AocException("could not update score for player $curPlayer")
        var previous7 = current
        repeat(7) { previous7 = previous7.previous ?: throw AocException("node $previous7 has no previous") }
        scoreMap[curPlayer] = scoreMap[curPlayer]?.plus(previous7.id) ?: throw AocException("could not update score for player $curPlayer")
        val previous = previous7.previous ?: throw AocException("node $previous7 has no previous")
        val next = previous7.next ?: throw AocException("node $previous7 has no next")
        previous.next = next
        next.previous = previous
        current = next
    }

    fun print() {
        var marble = marbles
        do {
            println(marble)
            marble = marble.next ?: throw AocException("marble $marble has no next")
        } while (marble != marbles)
        println("current: $current")
    }
}

data class ListNode(val id: Int, var previous: ListNode? = null, var next: ListNode? = null) {
    override fun toString() = "marble: $id, next: ${next?.id}, previous: ${previous?.id}"
}