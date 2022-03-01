package codegen.utils

import codegen.WaccTreeVisitor
import codegen.instr.*
import codegen.instr.register.*
import codegen.instr.operand2.*

object PrintFuncs {
    fun printString() {
        if (WaccTreeVisitor.funcTable.lookup("p_print_string") != null) {
            return
        }
        val instrs = mutableListOf<Instruction>()
        instrs.add(Load(RegisterIterator.r1, ZeroOffset(RegisterIterator.r0)))
        instrs.add(Add(RegisterIterator.r2, RegisterIterator.r0, Immediate(4)))
        val format = WaccTreeVisitor.stringTable.add("%.*s\\0")
        instrs.add(Load(RegisterIterator.r0, format))
        instrs.add(Add(RegisterIterator.r0, RegisterIterator.r0, Immediate(4)))
        instrs.add(BranchWithLink("printf"))
        instrs.add(Move(RegisterIterator.r0, Immediate(0)))
        instrs.add(BranchWithLink("fflush"))
        val f = FuncObj("")
        f.funcName = "p_print_string"
        f.funcBody = instrs
        WaccTreeVisitor.funcTable.add(f.funcName, f)
    }

    fun printInt() {
        if (WaccTreeVisitor.funcTable.lookup("p_print_int") != null) {
            return
        }
        val instrs = mutableListOf<Instruction>()
        instrs.add(Move(RegisterIterator.r1, RegisterIterator.r0))
        val format = WaccTreeVisitor.stringTable.add("%d\\0")
        instrs.add(Load(RegisterIterator.r0, format))
        instrs.add(Add(RegisterIterator.r0, RegisterIterator.r0, Immediate(4)))
        instrs.add(BranchWithLink("printf"))
        instrs.add(Move(RegisterIterator.r0, Immediate(0)))
        instrs.add(BranchWithLink("fflush"))
        val f = FuncObj("")
        f.funcName = "p_print_int"
        f.funcBody = instrs
        WaccTreeVisitor.funcTable.add(f.funcName, f)
    }

    fun printReference() {
        if (WaccTreeVisitor.funcTable.lookup("p_print_reference") != null) {
            return
        }
        val instrs = mutableListOf<Instruction>()
        instrs.add(Move(RegisterIterator.r1, RegisterIterator.r0))
        val format = WaccTreeVisitor.stringTable.add("%p\\0")
        instrs.add(Load(RegisterIterator.r0, format))
        instrs.add(Add(RegisterIterator.r0, RegisterIterator.r0, Immediate(4)))
        instrs.add(BranchWithLink("printf"))
        instrs.add(Move(RegisterIterator.r0, Immediate(0)))
        instrs.add(BranchWithLink("fflush"))   
        val f = FuncObj("")
        f.funcName = "p_print_reference"
        f.funcBody = instrs
        WaccTreeVisitor.funcTable.add(f.funcName, f)
    }

    fun printBoolean() {
        if (WaccTreeVisitor.funcTable.lookup("p_print_bool") != null) {
            return
        }
        val trueMsg = WaccTreeVisitor.stringTable.add("true")
        val falseMsg = WaccTreeVisitor.stringTable.add("false")
        val instrs = mutableListOf<Instruction>()
        instrs.add(Compare(RegisterIterator.r0, Immediate(0)))
        instrs.add(Load(RegisterIterator.r0, trueMsg, Cond(Condition.NE)))
        instrs.add(Load(RegisterIterator.r0, falseMsg, Cond(Condition.EQ)))
        instrs.add(Add(RegisterIterator.r0, RegisterIterator.r0, Immediate(4)))
        instrs.add(BranchWithLink("printf"))
        instrs.add(Move(RegisterIterator.r0, Immediate(0)))
        instrs.add(BranchWithLink("fflush"))
        val f = FuncObj("")
        f.funcName = "p_print_bool"
        f.funcBody = instrs
        WaccTreeVisitor.funcTable.add(f.funcName, f)
    }

    fun printLn() {
        if (WaccTreeVisitor.funcTable.lookup("p_print_ln") != null) {
            return
        }
        val instrs = mutableListOf<Instruction>()
        val format = WaccTreeVisitor.stringTable.add("\\0")
        instrs.add(Load(RegisterIterator.r0, format))
        instrs.add(Add(RegisterIterator.r0, RegisterIterator.r0, Immediate(4)))
        instrs.add(BranchWithLink("printf"))
        instrs.add(Move(RegisterIterator.r0, Immediate(0)))
        instrs.add(BranchWithLink("fflush"))
        val f = FuncObj("")
        f.funcName = "p_print_ln"
        f.funcBody = instrs
        WaccTreeVisitor.funcTable.add(f.funcName, f)
    }
}
