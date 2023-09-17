package mpdev.springboot.aoc2018.solutions.vmcomputer

class Program(input: List<String> = listOf(), private val numRegisters: Int = 4) {

    companion object {
        const val NA = 99
    }

    var code: MutableList<Instruction> = mutableListOf()
    lateinit var register: LongArray
    var ip: Int = 0
    var ipIndex: Int = -1   // by default the ip is not linked to any of the main registers
    var DEBUG = false
    var breakpoint: () -> Boolean = {false}
    var numberOfExecutedInstructions = 0L

    init {
        processInput(input)
        initRegisters(LongArray(numRegisters){0})
    }

    private fun initRegisters(init: LongArray) {
        register = LongArray(numRegisters){0}
        init.copyInto(register)
        ip = 0
        numberOfExecutedInstructions = 0
    }

    fun run(initReg: LongArray = LongArray(numRegisters){0}, injectedCode: () -> Boolean = {false}) {
        initRegisters(initReg)
        while(ip < code.size) {
            if (breakpoint()) {    // breakpoint for debugging
                println("ip = $ip, ${code[ip].opCode.name} ${code[ip].params}, registers ${register.toList()}, exec.instr $numberOfExecutedInstructions")
                return
            }    // the injected code may adjust the registers and the ip, bypassing parts of the code
            // if it returns true then skip execution of ths instruction and continue to next
            if (injectedCode()) {
                ip = register[ipIndex].toInt()
                continue
            }
            val instr = code[ip]
            if (ipIndex >= 0)
                register[ipIndex] = ip.toLong()
            executeStep(instr.opCode, instr.params)
            if (ipIndex >= 0)
                ip = register[ipIndex].toInt()
            ++ip
        }
        if (DEBUG)
            println("ip = $ip, end of program")
    }

    fun executeStep(opCode: OpCode, params: List<Int>) {
        opCode.execute(params[0], params[1], params[2], register)
        ++numberOfExecutedInstructions
        if (DEBUG)
            println("ip = $ip, executed ${opCode.name} $params, registers after exec. ${register.toList()}, exec.instr $numberOfExecutedInstructions\"")
    }

    data class Instruction(val opCode: OpCode = OpCode.nop, val params: List<Int> = listOf())

    // opcodes can be identified either by name or the value of intCode (must be set before execution of pgm)
    enum class OpCode(var intCode: Int, val execute: (Int, Int, Int, LongArray) -> Unit) {
        addr(NA, { a, b, c, reg -> reg[c] = reg[a] + reg[b] }),
        addi(NA, { a, b, c, reg -> reg[c] = reg[a] + b }),
        mulr(NA, { a, b, c, reg -> reg[c] = reg[a] * reg[b] }),
        muli(NA, { a, b, c, reg -> reg[c] = reg[a] * b }),
        banr(NA, { a, b, c, reg -> reg[c] = reg[a] and reg[b] }),
        bani(NA, { a, b, c, reg -> reg[c] = reg[a] and b.toLong() }),
        borr(NA, { a, b, c, reg -> reg[c] = reg[a] or reg[b] }),
        bori(NA, { a, b, c, reg -> reg[c] = reg[a] or b.toLong() }),
        setr(NA, { a, _, c, reg -> reg[c] = reg[a] }),
        seti(NA, { a, _, c, reg -> reg[c] = a.toLong() }),
        gtir(NA, { a, b, c, reg -> reg[c] = if (a > reg[b]) 1 else 0}),
        gtri(NA, { a, b, c, reg -> reg[c] = if (reg[a] > b) 1 else 0}),
        gtrr(NA, { a, b, c, reg -> reg[c] = if (reg[a] > reg[b]) 1 else 0}),
        eqir(NA, { a, b, c, reg -> reg[c] = if (a == reg[b].toInt()) 1 else 0}),
        eqri(NA, { a, b, c, reg -> reg[c] = if (reg[a].toInt() == b) 1 else 0}),
        eqrr(NA, { a, b, c, reg -> reg[c] = if (reg[a] == reg[b]) 1 else 0}),
        nop(NA,  { _,_,_, _ -> });

        override fun toString() = "OpCode($name ${intCode})"

        companion object {
            fun getOpCodeFromInt(intCode: Int) =
                values().first { it.intCode == intCode }
        }
    }

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
                    code.add(Instruction(OpCode.valueOf(list[0]),
                        list.subList(1, list.size).map { it.toInt() })
                    )
                }
            }
        }
    }
}