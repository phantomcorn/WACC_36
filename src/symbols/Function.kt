package symbols

import func.Parameter
import myvisitor.SymbolTable
import kotlin.Array

class Function(
    val returnType: Type,
    val params: Array<Parameter>,
    val symbolTable: SymbolTable
) : Type()
