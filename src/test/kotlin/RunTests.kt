import net.jorjai.Interpreter
import java.io.File
import java.io.PrintStream

fun main() {
    val inputDirectory = "src/test/resources/input"
    val outputDirectory = "src/test/resources/output"

    // Get the files
    val testInput: List<File> = getFilesInDirectory(inputDirectory)
    val testOutput: List<File> = getFilesInDirectory(outputDirectory)

    // Check the input and the output files match in number
    if (testInput.size != testOutput.size) {
        println("Error: Number of input and output does not match.")
        return
    }

    // Run the tests
    testInput.zip(testOutput).forEach { (inputFile, outputFile) ->
        println("Running test: ${inputFile.name}")
        val result = try {runInterpreter(inputFile).trim()}
                     catch (e: Exception) {e.message ?: ""}
        val expected = outputFile.readText().trim()

        if (result == expected) {
            println("Test ${inputFile.name} passed.")
        } else {
            println("\nTest ${inputFile.name} failed.")
            println("Expected:\n$expected")
            println("Got:\n$result\n")
        }
    }
}

fun getFilesInDirectory(directoryPath: String): List<File> {
    val folder = File(directoryPath)
    return if (folder.exists() && folder.isDirectory) {
        folder.listFiles()?.filter { it.isFile }?.toList() ?: emptyList()
    } else {
        emptyList()
    }
}

fun runInterpreter(inputFile: File): String {
    // Ejecuta el intérprete con el archivo de entrada y captura la salida
    val interpreter = Interpreter(inputFile)
    val output = buildString {
        val originalOut = System.out
        try {
            val mockOut = PrintStream(object : java.io.OutputStream() {
                override fun write(b: Int) {
                    append(b.toChar())
                }
            })
            System.setOut(mockOut)
            interpreter.execute()
        } finally {
            System.setOut(originalOut)
        }
    }
    return output
}
