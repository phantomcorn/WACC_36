package parse.stat

import ErrorHandler
import ErrorType
import codegen.ASTVisitor
import codegen.instr.Instruction
import parse.expr.Expr
import parse.semantics.SymbolTable
import parse.symbols.Boolean
import parse.symbols.Type

class If(
    val e: Expr,
    val s1: Stat,
    val s2: Stat,
    val st1: SymbolTable<Type>,
    val st2: SymbolTable<Type>
) : Stat() {
    init {
        if (!(e.type is Boolean)) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible type at $e (expected: BOOL, actual: ${e.type}"
            )
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitIfNode(this)
    }
}
