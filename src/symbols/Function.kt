package symbols

import myvisitor.SymbolTable
import kotlin.Array

class Function(
    returnType: Type,
    formals: Array<Parameters>,
    symbolTable: SymbolTable
) : Identifier()
