package net.jorjai

import java.io.File

/**
 * The entry point of the interpreter program.
 *
 * This function:
 * - Accepts a script file as a command-line argument.
 * - Reads and executes the script using the [Interpreter].
 * - If an error occurs during execution (e.g., invalid file, parsing issues), the exception is caught
 *   and the stack trace is printed to provide details about the error.
 *
 * @param args The command-line arguments where the first argument should be the path to the script file.
 */
fun main(args: Array<String>) {
    try {
        // Open the file specified as argument
        var file = File(args[0])

        // Execute the interpreter
        var interpreter = Interpreter(file)
        interpreter.execute()

    } catch (e: Exception) {
        // If an exception is caught, the error is printed to let the scripter know where the error is
        println(e.message)
    }
}