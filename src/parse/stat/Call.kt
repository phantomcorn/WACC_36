package parse.stat

import ErrorHandler
import ErrorType
import codegen.ASTNode
import codegen.ASTVisitor
import codegen.instr.Instruction
import parse.expr.Expr
import parse.func.FuncAST
import parse.func.FuncType
import parse.func.Function
import parse.semantics.SymbolTable
import parse.symbols.Type

class Call(val values: kotlin.Array<Expr>, val id: kotlin.String, st: SymbolTable<Function>) :
    ASTNode(), AssignRhs {
    var type: Type? = null

    init {
        val func = st.lookupAll(id)
        if (func == null) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Function $id is not defined in this scope"
            )
        } else if (!(func is FuncAST || func is FuncType)) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "$id is not a function"
            )
        } else if (func is FuncAST) {
            if (func.params.values.size != values.size) {
                ErrorHandler.printErr(
                    ErrorType.SEMANTIC,
                    "Incorrect number of parameters for $id (expected: " +
                            func.params.values.size + ", actual: " + values.size + ")"
                )
            } else {
                type = func.returnType
                for (i in 0..(func.params.values.size - 1)) {
                    if (values[i].type != func.params.values[i].paramType) {
                        ErrorHandler.printErr(
                            ErrorType.SEMANTIC,
                            "Incompatible type at " + values[i] +
                                    " (expected: " + func.params.values[i].paramType +
                                    ", actual: " + values[i].type + ")"
                        )
                    }
                }
            }
        } else {
            val ft = func as FuncType
            if (ft.params.size != values.size) {
                ErrorHandler.printErr(
                    ErrorType.SEMANTIC,
                    "Incorrect number of parameters for $id (expected: " +
                            ft.params.size + ", actual: " + values.size + ")"
                )

            } else {
                type = ft.returnType
                for (i in 0..(ft.params.size - 1)) {
                    if (values[i].type != ft.params[i]) {
                        ErrorHandler.printErr(
                            ErrorType.SEMANTIC,
                            "Incompatible type at " + values[i] + " (expected: " +
                                    ft.params[i] + ", actual: " + values[i].type + ")"
                        )
                    }
                }
            }
        }
    }


    override fun type(): Type? = type

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitCallNode(this)
    }

    override fun toString(): kotlin.String {
        var result = "call $id("
        if (values.size > 0) {
            result = result + values[0]
            for (i in 1..(values.size - 1)) {
                result = result + ", " + values[i]
            }
        }
        return result + ")"
    }
}
