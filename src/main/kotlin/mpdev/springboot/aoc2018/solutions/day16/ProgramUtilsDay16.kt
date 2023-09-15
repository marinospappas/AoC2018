package mpdev.springboot.aoc2018.solutions.day16

import mpdev.springboot.aoc2018.solutions.intcodecomputer.Program
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ProgramUtilsDay16(input: List<String>) {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    val samples = mutableListOf<Triple<List<Int>, InstrDay16, List<Int>>>()
    val matchingOpCodes = mutableListOf<Pair<Int,MutableList<Program.OpCode>>>()
    val program = Program()
    val codeDay16 = mutableListOf<InstrDay16>()

    init {
        processInput(input)
    }

    fun identifyOpCodes() {
        while (matchingOpCodes.count { it.second.size > 1 } > 0) {
            val matching1 = matchingOpCodes.filter { it.second.size == 1 }.distinct()
            val matchingMoreThan1 = matchingOpCodes.filter { it.second.size > 1 }.toMutableList()
            matching1.map { it.second }
                .forEach { singleMatch -> matchingMoreThan1.forEach { it.second.remove(singleMatch.first()) } }
        }
        matchingOpCodes.filter { it.second.isNotEmpty() }.distinct().forEach { it.second.first().intCode = it.first }
        Program.OpCode.values().forEach { log.info("{}", it) }
    }

    fun buildListOfMatchingOpCodes() {
        samples.forEach { sample -> matchingOpCodes.add(Pair(sample.second.intCode, findMatches(sample))) }
    }

    fun findMatches(sample: Triple<List<Int>, InstrDay16, List<Int>>): MutableList<Program.OpCode> {
        val (before, instr, after) = sample
        val matches = mutableListOf<Program.OpCode>()
        Program.OpCode.values().forEach { opcode ->
            before.toIntArray().copyInto(Program.register)
            program.executeStep(opcode, instr.params)
            // only the first 4 registers are compares - the 5th one is the ip
            if (Program.register.toMutableList().also { it.removeLast() } == after)
                matches.add(opcode)
        }
        return matches
    }

    private fun processInput(input: List<String>) {
        var processSamples = true
        var sampleLine = 1
        var regBefore = listOf<Int>()
        var regAfter: List<Int>
        var instr =InstrDay16()
        var prevLine = ""
        input.forEach { line ->
            if (line.isEmpty()) {
                if (prevLine.isEmpty())
                    processSamples = false
                prevLine = line
                return@forEach
            }
            prevLine = line
            if (processSamples) {
                when (sampleLine) {
                    1 -> {
                        // Before: [3, 2, 1, 1]
                        val match = Regex("""Before: \[(.+)]""").find(line)
                        val (registers) = match!!.destructured
                        regBefore = registers.replace(" ", "").split(',').map { it.toInt() }
                        ++sampleLine
                    }

                    2 -> {
                        val list = line.split(' ').map { it.toInt() }
                        instr = InstrDay16(list[0], list.subList(1, list.size))
                        ++sampleLine
                    }

                    3 -> {
                        // After: [3, 2, 1, 1]
                        val match = Regex("""After:  \[(.+)]""").find(line)
                        val (registers) = match!!.destructured
                        regAfter = registers.replace(" ", "").split(',').map { it.toInt() }
                        sampleLine = 1
                        samples.add(Triple(regBefore, instr, regAfter))
                    }
                }
            } else {  // process program instructions
                val list = line.split(' ').map { it.toInt() }
                codeDay16.add(InstrDay16(list[0], list.subList(1, list.size)))
            }
        }
    }

    data class InstrDay16(val intCode:Int = 0, val params: List<Int> = listOf())
}