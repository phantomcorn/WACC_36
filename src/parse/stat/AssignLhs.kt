package parse.stat

import parse.symbols.Type

interface AssignLhs {
    fun type(): Type?
}
