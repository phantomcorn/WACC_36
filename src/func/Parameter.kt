package func

import symbols.Identifier
import symbols.Type

class Parameter(val paramType : Type, val paramName : Identifier) : Identifier() {
}