package codegen.utils

import codegen.WaccTreeVisitor
import codegen.instr.*
import codegen.instr.register.*
import codegen.instr.operand2.*

object FreeFuncs {
    fun freeArray() {
        if (WaccTreeVisitor.funcTable.lookup("p_free_array") != null) {
            return
        }
        val instrs = mutableListOf<Instruction>()
        instrs.add(Compare(RegisterIterator.r0, Immediate(0)))
        val msg = WaccTreeVisitor.stringTable.add("NullReferenceError: dereference a null reference.\\n\\0")
        instrs.add(Load(RegisterIterator.r0, msg, Cond(Condition.EQ)))
        instrs.add(Branch("p_throw_runtime_error", Cond(Condition.EQ)))
        codegen.Error.RUNTIME.visitError()
        instrs.add(BranchWithLink("free"))

        val f = FuncObj("")
        f.funcName = "p_free_array"
        f.funcBody = instrs
        WaccTreeVisitor.funcTable.add(f.funcName, f)
    }
}
