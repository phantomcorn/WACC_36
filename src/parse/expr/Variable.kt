package parse.expr

import ErrorHandler
import ErrorType
import codegen.ASTVisitor
import codegen.instr.Instruction
import codegen.instr.loadable.Loadable
import parse.semantics.SymbolTable
import parse.stat.AssignLhs
import parse.symbols.Type

class Variable(
    val text: String,
    st: SymbolTable<Type>
) : Expr(st.lookupAll(text), 1), AssignLhs {
    init {
        if (st.lookupAll(text) == null) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Variable $text not defined in this scope")
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitVariableNode(this)
    }

    override fun toString(): String = text

    override fun acceptLhs(v: ASTVisitor): Pair<List<Instruction>, Loadable> {
        return v.visitVariableLhs(this)
    }
}
