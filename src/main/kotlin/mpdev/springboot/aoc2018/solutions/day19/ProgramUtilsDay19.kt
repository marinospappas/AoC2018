package mpdev.springboot.aoc2018.solutions.day19

import mpdev.springboot.aoc2018.solutions.vmcomputer.Program
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ProgramUtilsDay19(input: List<String>) {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    val codeDay19 = mutableListOf<Program.Instruction>()
    val program = Program(codeDay19, 6)
    var ipIndex = 0

    init {
        processInput(input)
        program.ipIndex = ipIndex
    }

    fun executeProgram() {
        program.run()
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