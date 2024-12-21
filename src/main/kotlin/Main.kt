package net.jorjai

import java.io.File

fun main(args: Array<String>) {
    try {
        // Open the file specified as argument 1
        var file = File(args[0])
        // Execute the interpreter
        var interpreter = Interpreter(file)
        interpreter.execute()
    } catch (e: Exception) {
        // If an exception is caught, the error is printed to let the scripter know where the error is
        println(e.message)
    }

}