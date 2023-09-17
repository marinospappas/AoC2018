package mpdev.springboot.aoc2018.solutions.day19

import mpdev.springboot.aoc2018.solutions.vmcomputer.Program
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ProgramUtilsDay19(input: List<String>) {

    companion object {
        const val NUM_REGISTERS = 6
    }

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    var program = Program(input, NUM_REGISTERS)

    fun executeProgram(regInit: LongArray = LongArray(NUM_REGISTERS){0}, injectedCode: () -> Boolean = {false}) {
        program.run(regInit, injectedCode)
    }

    // part 2 bypass for inner loop
    fun innerLoopByPass(): Boolean {
        val registers = program.register
        val ip = registers[program.ipIndex].toInt()
        if (ip == 3) {
            if (registers[2] % registers[4] == 0L) {
                log.info("found divisor {}", registers[4])
                registers[0] += registers[4]
            }
            registers[program.ipIndex] = 12
            return true
        }
        else
            return false
    }

    fun getRegister() = program.register

}