package mpdev.springboot.aoc2018.solutions.vmcomputer

class Program(var code: List<Instruction> = listOf()) {

    companion object {
        const val NUM_OF_REGISTERS = 5
        val register = IntArray(NUM_OF_REGISTERS) { 0 }
        var ip = register[NUM_OF_REGISTERS-1]
        const val NA = 99
    }

    private fun initRegisters() {
        IntArray(NUM_OF_REGISTERS){0}.copyInto(register)
        ip = register[NUM_OF_REGISTERS-1]
    }

    fun executeProgram() {
        initRegisters()
        while(ip < code.size) {
            val instr = code[ip]
            executeStep(instr.opCode, instr.params)
            ++ip
        }
    }

    fun executeStep(opCode: OpCode, params: List<Int>) {
        opCode.execute(params[0], params[1], params[2])
    }

    data class Instruction(val opCode: OpCode = OpCode.nop, val params: List<Int> = listOf())

    // opcodes can be identified either by name or the value of intCode (must be set before execution of pgm)
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
        eqrr(NA, { a, b, c -> register[c] = if (register[a] == register[b]) 1 else 0}),
        nop(NA, { _,_,_ -> });

        override fun toString() = "OpCode($name ${intCode})"

        companion object {
            fun getOpCodeFromInt(intCode: Int) =
                values().first { it.intCode == intCode }
        }
    }
}