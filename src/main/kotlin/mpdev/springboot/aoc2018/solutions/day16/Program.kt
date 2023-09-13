package mpdev.springboot.aoc2018.solutions.day16

class Program(input: List<String>) {

    val samples = mutableListOf<Triple<List<Int>, Instruction, List<Int>>>()
    val code = mutableListOf<Instruction>()
    val matchingOpCodes = mutableMapOf<Int,List<OpCode>>()
    var count3OrMore = 0

    init {
        processInput(input)
    }

    companion object {
        val register = IntArray(4) { 0 }
        const val NA = 99
    }

    fun executeStep(opCode: OpCode, params: List<Int>) {
        opCode.execute(params[0], params[1], params[2])
    }

    fun buildMapOfMatchingOpCodes() {
        samples.forEach { sample ->
            val matches = findMatches(sample)
            matchingOpCodes[sample.second.intCode] = matches
            if (matches.size >= 3)
                ++count3OrMore
        }
    }

    fun findMatches(sample: Triple<List<Int>, Instruction, List<Int>>): List<OpCode> {
        val (before, instr, after) = sample
        val matches = mutableListOf<OpCode>()
        OpCode.values().forEach { opcode ->
            before.toIntArray().copyInto(register)
            executeStep(opcode, instr.params)
            if (register.toList() == after)
                matches.add(opcode)
        }
        return matches
    }

    data class Instruction(val intCode: Int = 0, val params: List<Int> = listOf())

    enum class OpCode(var intCode: Int, val execute: (Int, Int, Int) -> Unit) {
        addr(NA, { a, b, c -> register[c] = register[a] + register[b] }),
        addi(NA, { a, b, c -> register[c] = register[a] + b }),
        mulr(NA, { a, b, c -> register[c] = register[a] * register[b] }),
        muli(NA, { a, b, c -> register[c] = register[a] * b }),
        banr(NA, { a, b, c -> register[c] = register[a] and register[b] }),
        bani(NA, { a, b, c -> register[c] = register[a] and b }),
        borr(NA, { a, b, c -> register[c] = register[a] or register[b] }),
        bori(NA, { a, b, c -> register[c] = register[a] or b }),
        setr(NA, { a, _, c -> register[c] = register[a] }),
        seti(NA, { a, _, c -> register[c] = a }),
        gtir(NA, { a, b, c -> register[c] = if (a > register[b]) 1 else 0}),
        gtri(NA, { a, b, c -> register[c] = if (register[a] > b) 1 else 0}),
        gtrr(NA, { a, b, c -> register[c] = if (register[a] > register[b]) 1 else 0}),
        eqir(NA, { a, b, c -> register[c] = if (a == register[b]) 1 else 0}),
        eqri(NA, { a, b, c -> register[c] = if (register[a] == b) 1 else 0}),
        eqrr(NA, { a, b, c -> register[c] = if (register[a] == register[b]) 1 else 0})
    }

    private fun processInput(input: List<String>) {
        var processSamples = true
        var sampleLine = 1
        var regBefore = listOf<Int>()
        var regAfter: List<Int>
        var instr = Instruction()
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
                        regBefore = registers.replace(" ","").split(',').map { it.toInt() }
                        ++sampleLine
                    }
                    2 -> {
                        val list = line.split(' ').map { it.toInt() }
                        instr = Instruction(list[0], list.subList(1, list.size))
                        ++sampleLine
                    }
                    3 -> {
                        // After: [3, 2, 1, 1]
                        val match = Regex("""After:  \[(.+)]""").find(line)
                        val (registers) = match!!.destructured
                        regAfter = registers.replace(" ","").split(',').map { it.toInt() }
                        sampleLine = 1
                        samples.add(Triple(regBefore, instr, regAfter))
                    }
                }
            }
            else {  // process program instructions
                val list = line.split(' ').map { it.toInt() }
                code.add(Instruction(list[0], list.subList(1, list.size)))
            }
        }
    }
}