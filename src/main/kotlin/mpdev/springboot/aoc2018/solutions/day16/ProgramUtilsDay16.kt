package mpdev.springboot.aoc2018.solutions.day16

import mpdev.springboot.aoc2018.solutions.vmcomputer.Program
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ProgramUtilsDay16(input: List<String>) {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    val samples = mutableListOf<Triple<List<Long>, InstrDay16, List<Long>>>()
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

    fun findMatches(sample: Triple<List<Long>, InstrDay16, List<Long>>): MutableList<Program.OpCode> {
        val (before, instr, after) = sample
        val matches = mutableListOf<Program.OpCode>()
        Program.OpCode.values().filterNot { it == Program.OpCode.nop }.forEach { opcode ->
            before.toLongArray().copyInto(program.register)
            program.executeStep(opcode, instr.params)
            if (program.register.toMutableList() == after)
                matches.add(opcode)
        }
        return matches
    }

    fun executeProgram() {
        program.code = codeDay16.map { Program.Instruction(Program.OpCode.getOpCodeFromInt(it.intCode),
            it.params) }
        program.run()
    }

    fun getRegister() = program.register

    private fun processInput(input: List<String>) {
        var processSamples = true
        var sampleLine = 1
        var regBefore = listOf<Long>()
        var regAfter: List<Long>
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
                        regBefore = registers.replace(" ", "").split(',').map { it.toLong() }
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
                        regAfter = registers.replace(" ", "").split(',').map { it.toLong() }
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

    data class InstrDay16(val intCode: Int = 0, val params: List<Int> = listOf())
}