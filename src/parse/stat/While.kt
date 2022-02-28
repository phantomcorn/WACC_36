package parse.stat

import ErrorHandler
import ErrorType
import codegen.ASTVisitor
import codegen.instr.Instruction
import parse.expr.Expr
import parse.semantics.SymbolTable
import parse.symbols.Boolean
import parse.symbols.Type

class While(val e: Expr, val s: Stat, val st: SymbolTable<Type>) : Stat() {
    init {
        if (e.type !is Boolean) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible type at $e (expected: BOOL, actual: ${e.type})"
            )
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitWhileNode(this)
    }

    override fun toString(): String = "while $e do $s done"
}
