package mpdev.springboot.aoc2018.solutions.day21

import mpdev.springboot.aoc2018.solutions.day19.ProgramUtilsDay19
import mpdev.springboot.aoc2018.solutions.vmcomputer.Program

class ProgramUtilsDay21(input: List<String>) {

    companion object {
        const val NUM_REGISTERS = 6
    }

    var program = Program(input, ProgramUtilsDay19.NUM_REGISTERS)

    val r1values = mutableListOf<Long>()

    fun executeProgram(regInit: LongArray = LongArray(NUM_REGISTERS){0}, injectedCode: () -> Boolean = {false}) {
        program.run(regInit, injectedCode)
    }

    // this breakpoint will stop the program at the first instance r0 is checked against r1 to determine if it must stop
    fun breakPointP1() =
        program.ip == 28    // instr: eqrr 1 0 5

    // this breakpoint will stop the program when the r1 value has been seen before at instr 28 - means that we are looping
    fun breakPointP2() =
        program.ip == 28 && r1values.contains(program.register[1])

    fun injectedCodePart2(): Boolean {
        when (program.ip) {
            // at instr 28 collect all the r1 values
            28 -> r1values.add(getRegister()[1])
            // optimisation for steps 17 - 25 where effectively r5 = r4 / 256
            17 -> {
                program.register[5] = program.register[4] / 256
                program.register[3] = 26
                return true      // skip execution of the 256-loop section
            }
        }
        return false    // execute instruction as normal
    }

    fun getRegister() = program.register

}