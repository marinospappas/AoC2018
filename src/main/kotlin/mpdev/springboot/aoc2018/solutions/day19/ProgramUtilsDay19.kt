package mpdev.springboot.aoc2018.solutions.day19

import mpdev.springboot.aoc2018.solutions.vmcomputer.Program
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ProgramUtilsDay19(input: List<String>) {

    companion object {
        const val NUM_REGISTERS = 6
    }

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    val codeDay19 = mutableListOf<Program.Instruction>()
    val program = Program(codeDay19, NUM_REGISTERS)
    var ipIndex = 0

    init {
        processInput(input)
        program.ipIndex = ipIndex
    }

    fun executeProgram(regInit: LongArray = LongArray(NUM_REGISTERS){0}, injectedCode: () -> Boolean = {false}) {
        program.run(regInit, injectedCode)
    }

    // part 2 bypass for inner loop
    fun innerLoopByPass(): Boolean {
        val registers = program.register
        val ip = registers[ipIndex].toInt()
        if (ip == 3) {
            if (registers[2] % registers[4] == 0L) {
                log.info("found divisor {}", registers[4])
                registers[0] += registers[4]
            }
            registers[ipIndex] = 12
            return true
        }
        else
            return false
    }

    fun getRegister() = program.register

    private fun processInput(input: List<String>) {
        var firstLine = true
        input.forEach { line ->
            when (firstLine) {
                true -> {
                    // #ip 0
                    val match = Regex("""#ip (\d+)""").find(line)
                    val (index) = match!!.destructured
                    ipIndex = index.toInt()
                    firstLine = false
                }
                false -> {
                    // seti 5 0 1
                    val list = line.split(' ').toList()
                    codeDay19.add(Program.Instruction(Program.OpCode.valueOf(list[0]),
                        list.subList(1, list.size).map { it.toInt() })
                    )
                }
            }
        }
    }
}