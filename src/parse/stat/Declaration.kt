package parse.stat

import ErrorHandler
import ErrorType
import codegen.ASTVisitor
import codegen.instr.Instruction
import parse.semantics.SymbolTable
import parse.symbols.Type
import parse.symbols.ArrayInstance
import parse.stat.ArrayLiteral

class Declaration(
    val t: Type,
    val id: String,
    val rhs: AssignRhs,
    st: SymbolTable<Type>
) : Stat() {
    init {
        if (rhs.type() != t) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible type at $rhs (expected: $t, actual: " + rhs.type() + ")"
            )
        } else if (st.lookup(id) != null) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "\"$id\" is already defined in this scope"
            )
        } else {
            st.add(id, t)
            if (t is ArrayInstance && rhs is ArrayLiteral) {
                t.setSize(rhs)
            }
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitDeclarationNode(this)
    }

    override fun toString(): String {
        return "$t $id = $rhs"
    }
}
