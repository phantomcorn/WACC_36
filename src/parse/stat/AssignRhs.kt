package parse.stat

import parse.symbols.Type

interface AssignRhs {
    fun type(): Type?
}
