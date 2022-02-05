package symbols

import SymbolTable
import kotlin.Array

class Function(
    returnType: Type,
    formals: Array<Parameters>,
    symbolTable: SymbolTable
) : Identifier()
