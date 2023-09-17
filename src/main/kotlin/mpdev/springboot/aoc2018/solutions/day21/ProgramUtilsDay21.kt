package mpdev.springboot.aoc2018.solutions.day21

import mpdev.springboot.aoc2018.solutions.day19.ProgramUtilsDay19
import mpdev.springboot.aoc2018.solutions.vmcomputer.Program
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ProgramUtilsDay21(input: List<String>) {

    companion object {
        const val NUM_REGISTERS = 6
    }

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    var program = Program(input, ProgramUtilsDay19.NUM_REGISTERS)

    fun executeProgram(regInit: LongArray = LongArray(NUM_REGISTERS){0}, injectedCode: () -> Boolean = {false}) {
        program.run(regInit, injectedCode)
    }

    // this breakpoint will stop the program at the first instance r0 is checked against r1 to determine if it must stop
    fun breakPointP1() =
        program.ip == 28    // instr: eqrr 1 0 5

    fun breakPointP2() =
        program.ip == 28

    fun injectedCodePart2(): Boolean {
        if (program.ip == 28)
            println("ip = ${program.ip}, ${program.code[program.ip].opCode.name} ${program.code[program.ip].params}, registers ${program.register.toList()}, exec.instr ${program.numberOfExecutedInstructions}")
        return false
    }

    fun getRegister() = program.register

}