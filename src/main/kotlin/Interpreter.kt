package net.jorjai

import java.io.File
import java.util.LinkedList
import java.util.Scanner

class Interpreter(file: File) {
    /**
     * The current word selected by the interpreter, which is updated by the [parser] as it reads the script.
     */
    var word = ""

    /**
     * An iterator that parses the script, reading it word by word.
     * The [parser] is used to tokenize the script for processing.
     */
    val parser = Scanner(file)

    /**
     * A memory store for the variables in the script.
     * It allows variable values to be stored and retrieved within different scopes.
     */
    val memory = Memory<String, Int>()

    /**
     * A set of reserved keywords that cannot be used as variable names in the script.
     * This includes control structures and operators such as `print`, `scope`, `{`, `}`, and `=`.
     */
    val keywords: HashSet<String> = hashSetOf(
        "print", "scope", "{", "}", "="
    )


    /**
     * A class that manages variables across multiple scopes using a layered memory structure.
     * Each scope is represented by a `HashMap` stored in a `LinkedList`.
     * The first element in the list corresponds to the innermost (local) scope,
     * while the last element corresponds to the outermost (global) scope.
     *
     * @param K the type of the keys (variable names).
     * @param V the type of the values (variable values).
     */
    class Memory<K,V> {
        /**
         * A list of `HashMap` objects that represent the stack of scopes in the memory.
         *
         * - Each `HashMap<K, V>` corresponds to a single scope, where:
         *   - Keys (`K`) are the variable names.
         *   - Values (`V`) are the corresponding variable values.
         * - The list is ordered such that:
         *   - The **first element** is the innermost (local) scope.
         *   - The **last element** is the outermost (global) scope.
         *
         * This structure allows access to variables across scopes, prioritizing
         * variables in the closest (most local) scope.
         */
        private val source = LinkedList<HashMap<K,V>>()

        /**
         * Searches for the value of a variable across multiple scopes, starting from the innermost (local) scope.
         * If the variable is not found in the current scope, it continues searching in the outer scopes (global).
         * The search is performed in a layered manner, checking each scope in the order they were added.
         *
         * @param key the name of the variable whose value is being retrieved.
         * @return the value associated with the variable if found, or `null` if the variable is not initialized
         *         in any of the scopes.
         */
        fun get(key: K): V? {
            for (scope in source) {
                // Check if the variable has a value, layer by layer
                if (scope.contains(key))
                    // Variable found
                    return scope.get(key)
            }
            return null // Variable not in memory
        }

        /**
         * Stores a value in the innermost scope (local scope) for a given variable.
         * If the variable already exists in the local scope, its value is updated.
         *
         * @param key the name of the variable to be stored.
         * @param value the value to be assigned to the variable.
         */
        fun put(key: K, value: V) = source.first().put(key, value)

        /**
         * Checks if there are any open scopes in the memory.
         * This indicates whether there is a current scope in which variables can be stored.
         * If scopes are open, it may suggest that the script is missing closing brackets
         * (`}`) to properly close all previously opened scopes.
         *
         * @return `true` if there are open scopes, `false` otherwise.
         */
        fun isScopeOpened(): Boolean {
            return !source.isEmpty()
        }

        /**
         * Inserts a new memory layer (scope) at the start of the list.
         * This represents the opening of a new block of code where new variables can be declared
         * and existing ones can be modified within the scope.
         *
         * This operation adds a new `HashMap` to the beginning of the `source` list,
         * allowing for a new local scope.
         */
        fun openScope() = source.add(0,HashMap())

        /** Remove the first memory layer of the list*/
        fun closeScope() = source.removeAt(0)

    }

    /**
     * Executes the interpreter logic by parsing the input file word by word and executing corresponding actions.
     *
     * The method performs the following tasks:
     * - Opens the global scope where variables will be stored.
     * - Iterates through each word in the input script, checking for keywords such as `print`, `scope`, or `}`.
     *    - For `print`, it calls `parsePrint()` to output a variable's value.
     *    - For `scope`, it opens a new scope (i.e., a new block of code).
     *    - For `}`, it closes the current scope.
     *    - Any other word is treated as a variable, and `parseVariable()` is called to handle variable assignment or lookups.
     * - After parsing all the words, the global scope is closed.
     * - Finally, the method checks if there are any open scopes left, throwing an exception if there are any unclosed scopes (missing closing `}`).
     *
     * @throws RuntimeException if there are open scopes left after parsing, indicating that a closing `}` is missing.
     */
    fun execute() {
        // Open global scope
        memory.openScope()

        // Parse word by word
        while (parser.hasNext()) {
            word = parser.next()
            // Check if the word is a key-word
            when (word) {
                "print" -> parsePrint()
                "scope" -> openScope() // The function checks if there is a "{"
                "}" -> closeScope()
                else -> parseVariable() // Treat it as a variable
            }
        }

        // Close global scope
        memory.closeScope()

        // Check there are not opened scopes
        if (memory.isScopeOpened()) throw RuntimeException("Closing '}' expected")
    }

    /**
     * Parses a variable assignment in the script, where a variable is either assigned an integer value
     * or the value of another variable.
     *
     * The method performs the following steps:
     * - The current word is treated as the name of the variable to be assigned a value.
     * - It checks for the next word, expecting an assignment operator (`=`).
     * - If the assignment operator is found, it parses the value to be assigned:
     *    - If the value is an integer, it is stored in memory.
     *    - If the value is a variable, it retrieves the value of that variable from memory and assigns it.
     * - If the variable name is a keyword, an exception is thrown.
     * - If an unrecognized operand is encountered, a `RuntimeException` is thrown.
     *
     * @throws RuntimeException if the end of the file is reached unexpectedly or if an unrecognized operand
     *         or invalid variable assignment is encountered.
     */
    private fun parseVariable() {
        val varName: String = word // Save the name of the variable

        // Check EOF
        if (!parser.hasNext()) throw RuntimeException("Reached end of file while parsing.")

        // Check the operand type
        word = parser.next()
        when (word) {
            "=" -> {
                // Check EOF
                if (!parser.hasNext()) throw RuntimeException("Reached end of file while parsing.")
                word = parser.next()
                try {
                    // Try to assign it as integer
                    memory.put(varName, word.toInt())
                } catch (e: Exception) {
                    // Check variable name
                    if (isKeyWord(word)) throw RuntimeException("Variable name expected but found '$word'")
                    // Assign the value of the second variable to the first one
                    val value: Int? = memory.get(word)
                    if (value != null) {
                        memory.put(varName, value)
                    }
                }
            }
            else -> throw RuntimeException("Unrecognized operand: $word")
        }
    }

    /**
     * A wrapper for [Memory.openScope] that opens a new scope in memory.
     *
     * It checks for EOF and expects the next token to be `{`. Throws a `RuntimeException` if not found.
     *
     * @throws RuntimeException if EOF is reached or if `{` is missing.
     * @see Memory.openScope
     */
    private fun openScope() {
        // Check EOF
        if (!parser.hasNext()) throw RuntimeException("Reached end of file while parsing.")
        // Throw exception if the next token is not '{'
        if (!parser.next().equals("{")) throw RuntimeException("'{' expected.")
        memory.openScope()
    }

    /**
     * A wrapper for [Memory.closeScope], which closes the current scope in memory.
     *
     * This method calls [Memory.closeScope] to remove the most recently opened scope from memory.
     *
     * @see Memory.closeScope
     */
    private fun closeScope() = memory.closeScope()

    private fun parsePrint() {
        // Check EOF
        if (!parser.hasNext()) throw RuntimeException("Reached end of file while parsing.")
        // Save the variable and its value
        word = parser.next()
        // Check variable name
        if (isKeyWord(word)) throw RuntimeException("Variable name expected but found '$word'")
        // Output the value of the variable
        println(memory.get(word))
    }

    /**
     * Checks if the variable name is a keyword.
     *
     * @param varName the name of the variable
     * @return `true` if the variable name is a keyword, `false` otherwise
     * @see keywords
     */
    private fun isKeyWord(varName: String): Boolean = keywords.contains(varName)
}