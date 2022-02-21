package parse.semantics

class SymbolTable<T>(private val parent: SymbolTable<T>?){

    val dict : MutableMap<String, T> = mutableMapOf<String, T>()

    fun getTable(): SymbolTable<T>? {
        return parent
    }

    fun add(name: String, elem: T) {
        dict[name] = elem
    }

    fun lookup(name: String): T? {
        return dict[name]
    }

    fun lookupAll(name: String): T? {
        var currTable: SymbolTable<T>? = this;
        while (currTable != null){
            var res: T? = currTable.lookup(name)
            if (res !== null) {
                return res
            }
            currTable = currTable.parent
        }
        return null
    }
}
