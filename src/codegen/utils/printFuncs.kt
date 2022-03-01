package codegen.utils

import codegen.WaccTreeVisitor
import codegen.instr.*
import codegen.instr.register.*
import codegen.instr.operand2.*

object printFuncs {
    fun printString() {
        if (WaccTreeVisitor.funcTable.lookup("p_print_string") != null) {
            return
        }
        val instrs = mutableListOf<Instruction>()
        instrs.add(Push(listOf<Register>(LR)))
        instrs.add(Load(RegisterIterator.r1, ZeroOffset(RegisterIterator.r0)))
        instrs.add(Add(RegisterIterator.r2, RegisterIterator.r0, Immediate(4)))
        val format = WaccTreeVisitor.stringTable.add("%.*s\\0")
        instrs.add(Load(RegisterIterator.r0, format))
        instrs.add(Add(RegisterIterator.r0, RegisterIterator.r0, Immediate(4)))
        instrs.add(BranchWithLink("printf"))
        instrs.add(Move(RegisterIterator.r0, Immediate(0)))
        instrs.add(BranchWithLink("fflush"))
        instrs.add(Pop(listOf<Register>(PC)))
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
        instrs.add(Push(listOf<Register>(LR)))
        instrs.add(Move(RegisterIterator.r1, RegisterIterator.r0)) 
        val format = WaccTreeVisitor.stringTable.add("%d\\0")
        instrs.add(Load(RegisterIterator.r0, format))
        instrs.add(Add(RegisterIterator.r0, RegisterIterator.r0, Immediate(4)))
        instrs.add(BranchWithLink("printf"))
        instrs.add(Move(RegisterIterator.r0, Immediate(0)))
        instrs.add(BranchWithLink("fflush"))
        instrs.add(Pop(listOf<Register>(PC)))
    }

    fun printReference() {
        if (WaccTreeVisitor.funcTable.lookup("p_print_reference") != null) {
            return
        }
        val instrs = mutableListOf<Instruction>()
        instrs.add(Push(listOf<Register>(LR)))
        instrs.add(Move(RegisterIterator.r1, RegisterIterator.r0))
        val format = WaccTreeVisitor.stringTable.add("%p\\0")
        instrs.add(Load(RegisterIterator.r0, format))
        instrs.add(Add(RegisterIterator.r0, RegisterIterator.r0, Immediate(4)))
        instrs.add(BranchWithLink("printf"))
        instrs.add(Move(RegisterIterator.r0, Immediate(0)))
        instrs.add(BranchWithLink("fflush"))   
        instrs.add(Pop(listOf<Register>(PC)))
    }
}
