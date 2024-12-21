package net.jorjai

import java.io.File

fun main(args: Array<String>) {
    // Open the file specified as argument 1


    var file = File("D:\\Repositorios\\SimpleCodeInterpreter\\src\\test\\resources\\input.txt")

    // Execute the interpreter
    var interpreter = Interpreter(file)
    interpreter.execute()
}