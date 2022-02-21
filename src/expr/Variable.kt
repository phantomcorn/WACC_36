package expr

import ErrorHandler
import codegen.ASTVisitor
import instr.Instruction
import stat.AssignLhs
import symbols.Type
import visitor.SymbolTable

class Variable(
    val text: String,
    st: SymbolTable<Type>
) : Expr(st.lookupAll(text)), AssignLhs {
    init {
        if (st.lookupAll(text) == null) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Variable $text not defined in this scope")
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitVariableNode(this)
    }

    override fun toString(): String = text
}
