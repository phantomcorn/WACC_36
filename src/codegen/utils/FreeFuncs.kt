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
        val msg = WaccTreeVisitor.stringTable.add("NullReferenceError: dereference a null reference\\n\\0")
        instrs.add(Load(RegisterIterator.r0, msg, Cond(Condition.EQ)))
        instrs.add(BranchWithLink("p_throw_runtime_error", Cond(Condition.EQ)))
        codegen.Error.RUNTIME.visitError()
        instrs.add(BranchWithLink("free"))

        val f = FuncObj("")
        f.funcName = "p_free_array"
        f.funcBody = instrs
        WaccTreeVisitor.funcTable.add(f.funcName, f)
    }

    fun freePair() {
        if (WaccTreeVisitor.funcTable.lookup("p_free_pair") != null) {
            return
        }
        val instrs = mutableListOf<Instruction>()
        instrs.add(Compare(RegisterIterator.r0, Immediate(0)))
        val msg = WaccTreeVisitor.stringTable.add("NullReferenceError: dereference a null reference\\n\\0")
        instrs.add(Load(RegisterIterator.r0, msg, Cond(Condition.EQ)))
        instrs.add(Branch("p_throw_runtime_error", Cond(Condition.EQ)))
        codegen.Error.RUNTIME.visitError()
        instrs.add(Push(listOf<Register>(RegisterIterator.r0)))
        instrs.add(Load(RegisterIterator.r0, ZeroOffset(RegisterIterator.r0)))
        instrs.add(BranchWithLink("free"))
        instrs.add(Load(RegisterIterator.r0, ZeroOffset(SP)))
        instrs.add(Load(RegisterIterator.r0, ImmediateOffset(RegisterIterator.r0, Immediate(4))))
        instrs.add(BranchWithLink("free"))
        instrs.add(Pop(listOf<Register>(RegisterIterator.r0)))
        instrs.add(BranchWithLink("free"))

        val f = FuncObj("")
        f.funcName = "p_free_pair"
        f.funcBody = instrs
        WaccTreeVisitor.funcTable.add(f.funcName, f)
    }

    fun checkNullPointer() {
        if (WaccTreeVisitor.funcTable.lookup("p_check_null_pointer") != null) {
            return
        }
        val instrs = mutableListOf<Instruction>()
        instrs.add(Compare(RegisterIterator.r0, Immediate(0)))
        val msg = WaccTreeVisitor.stringTable.add("NullReferenceError: dereference a null reference\\n\\0")
        instrs.add(Load(RegisterIterator.r0, msg, Cond(Condition.EQ)))
        instrs.add(BranchWithLink("p_throw_runtime_error", Cond(Condition.EQ)))
        codegen.Error.RUNTIME.visitError()

        val f = FuncObj("")
        f.funcName = "p_check_null_pointer"
        f.funcBody = instrs
        WaccTreeVisitor.funcTable.add(f.funcName, f)
    }

    fun checkArrayBounds() {
        if (WaccTreeVisitor.funcTable.lookup("p_check_array_bounds") != null) {
            return
        }
        val instrs = mutableListOf<Instruction>()
        instrs.add(Compare(RegisterIterator.r0, Immediate(0)))
        val msg0 = WaccTreeVisitor.stringTable.add("ArrayIndexOutOfBoundsError: negative index\\n\\0")
        instrs.add(Load(RegisterIterator.r0, msg0, Cond(Condition.LT)))
        instrs.add(BranchWithLink("p_throw_runtime_error", Cond(Condition.LT)))
        codegen.Error.RUNTIME.visitError()
        instrs.add(Load(RegisterIterator.r1, ZeroOffset(RegisterIterator.r1)))
        instrs.add(Compare(RegisterIterator.r0, RegisterIterator.r1))
        val msg1 = WaccTreeVisitor.stringTable.add("ArrayIndexOutOfBoundsError: index too large\\n\\0")
        instrs.add(Load(RegisterIterator.r0, msg1, Cond(Condition.CS)))
        instrs.add(BranchWithLink("p_throw_runtime_error", Cond(Condition.CS)))

        val f = FuncObj("")
        f.funcName = "p_check_array_bounds"
        f.funcBody = instrs
        WaccTreeVisitor.funcTable.add(f.funcName, f)
    }
}
