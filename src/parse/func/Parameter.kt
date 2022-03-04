package parse.func

import parse.symbols.Identifier
import parse.symbols.Type

class Parameter(val paramType: Type?, val paramName: String) : Identifier()
