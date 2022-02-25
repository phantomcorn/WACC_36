package parse.stat

import ErrorHandler
import ErrorType
import parse.expr.Expr
import parse.symbols.Identifier
import parse.symbols.Pair
import parse.symbols.PairInstance
import parse.symbols.Type
import codegen.ASTVisitor
import codegen.instr.Instruction
import codegen.instr.loadable.Loadable

class PairElem(val text: String, val e: Expr) : Identifier(), AssignLhs, AssignRhs {
    var type: Type? = null

    init {
        if (!(e.type is Pair)) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Expected Pair, got " + e.type)
        } else if (!(e.type is PairInstance)) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Attempt to dereference pair literal")
        } else {
            if (text == "fst") {
                type = e.type.t1
            } else {
                type = e.type.t2
            }
        }
    }

    override fun type(): Type? = type

    override fun acceptLhs(v: ASTVisitor): Loadable {
        return v.visitPairElemLhs(this)
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitPairElemNode(this)
    }
}
