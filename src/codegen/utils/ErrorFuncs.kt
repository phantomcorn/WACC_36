package codegen.utils

import codegen.WaccTreeVisitor
import codegen.instr.*
import codegen.instr.operand2.Immediate
import codegen.instr.register.GP

object ErrorFuncs {
    fun visitOverflowError() {
        if (WaccTreeVisitor.funcTable.lookup("p_throw_overflow_error") != null) {
            return
        }
        val instr = mutableListOf<Instruction>()
        val overflowMsg = WaccTreeVisitor.stringTable.add("OverflowError: the result is too small/large to store in a 4-byte signed-integer.\\n\\0")
        instr.add(Load(GP(0), overflowMsg))
        instr.add(BranchWithLink("p_throw_overflow_error"))

        val overflowFuncObj = FuncObj("")
        //Have to manually set name because errors do not begin with "f_"
        overflowFuncObj.funcName = "p_throw_overflow_error"
        overflowFuncObj.funcBody.addAll(instr)
        WaccTreeVisitor.funcTable.add("p_throw_overflow_error", overflowFuncObj)
        visitRuntimeError()
    }

    fun visitRuntimeError() {
        if (WaccTreeVisitor.funcTable.lookup("p_throw_runtime_error") != null) {
            return
        }
        val instr = mutableListOf<Instruction>()
        instr.add(BranchWithLink("p_print_string"))
        instr.add(Move((GP(0)), Immediate(-1)))
        instr.add(BranchWithLink("exit"))

        val runtimeFuncObj = FuncObj("")
        //Have to manually set name because errors do not begin with "f_"
        runtimeFuncObj.funcName = "p_throw_runtime_error"
        runtimeFuncObj.funcBody.addAll(instr)
        WaccTreeVisitor.funcTable.add("p_throw_runtime_error", runtimeFuncObj)
        PrintFuncs.printString()
    }

    fun visitDivideByZeroError() {
        if (WaccTreeVisitor.funcTable.lookup("p_check_divide_by_zero") != null) {
            return
        }

        val instr = mutableListOf<Instruction>()
        instr.add(Compare(GP(1), Immediate(0)))
        val msg = WaccTreeVisitor.stringTable.add("DivideByZeroError: divide or modulo by zero\\n\\0")
        instr.add(Load(GP(0), msg, Cond(Condition.EQ)))
        instr.add(BranchWithLink("p_throw_runtime_error", Cond(Condition.EQ)))

        val divideFuncObj = FuncObj("")
        //Have to manually set name because errors do not begin with "f_"
        divideFuncObj.funcName = "p_check_divide_by_zero"
        divideFuncObj.funcBody.addAll(instr)
        WaccTreeVisitor.funcTable.add("p_check_divide_by_zero", divideFuncObj)
        visitRuntimeError()
    }
}