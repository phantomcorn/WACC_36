package stat

import codegen.ASTVisitor
import expr.Expr
import codegen.instr.Instruction
import symbols.Boolean
import symbols.Type
import visitor.SymbolTable

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
