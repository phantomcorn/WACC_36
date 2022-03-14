package tests

abstract class Test {
    abstract fun test()

    protected fun assert(b: Boolean) {
        if (!b) {
            throw TestFailureException()
        }
    }

    protected fun run(f: () -> Unit) {
        try {
            f()
        } catch (e: TestFailureException) {
            failed += 1
            println(e.stackTrace[1].getMethodName() + " failed.")
        }
    }

    private class TestFailureException : Throwable()

    companion object {
        var failed: Int = 0
    }
}
