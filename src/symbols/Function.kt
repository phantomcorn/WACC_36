package symbols

import myvisitor.SymbolTable
import kotlin.Array

class Function(
    val returnType: Type,
    val params: Array<Parameters>,
    val symbolTable: SymbolTable
) : Type()
