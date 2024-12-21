package net.jorjai

import java.io.File
import java.util.LinkedList
import java.util.Scanner
import kotlin.collections.LinkedHashMap

class Interpreter(file: File) {
    var word = ""
    val parser: Scanner = Scanner(file)
    val memory = Memory<String, Int>()

    class Memory<K,V> {
        /**
         * The first element of the list stores the variables local to the current scope.
         * The last element stores the variables global to the program.
         */
        val source = LinkedList<LinkedHashMap<K,V>>()

        /**
         * Looks for the value of the variable in the local scope. If not found, it recursively looks for it
         * in the immediate next scope.
         *
         * @param key the variable whose value will be returned.
         * @return The value stored in the accessed variable or null if the variable is not initialised.
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
         * It stores the value in a variable.
         * @param value the value to be saved.
         * @param key the name of the variable.
         */
        fun put(key: K, value: V) = source.first().put(key, value)

        /**
         * Checks whether there are opened scopes or not.
         *
         * @return ``true`` if there are opened scopes, ``false`` otherwise.
         */
        fun isScopeOpened(): Boolean {
            return !source.isEmpty()
        }

        /** Insert a new memory layer at the start of the list */
        fun openScope() = source.add(0,LinkedHashMap())

        /** Remove the first memory layer of the list*/
        fun closeScope() = source.removeAt(0)

    }

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

    private fun parseVariable() {
        val varName: String = word // Save the name of the variable

        // Check the operand type
        if (!parser.hasNext()) throw RuntimeException("Reached end of file while parsing.")
        word = parser.next()
        when (word) {
            "=" -> {
                if (!parser.hasNext()) throw RuntimeException("Reached end of file while parsing.")
                word = parser.next()
                try {
                    memory.put(varName, word.toInt())
                } catch (e: Exception) {
                    memory.put(varName, memory.get(word)!!)
                }
            }
            else -> throw RuntimeException("Unrecognized operand: $word")
        }
    }

    /** Wrapper for [Memory.openScope]*/
    private fun openScope() {
        if (!parser.hasNext()) throw RuntimeException("Reached end of file while parsing.")
        if (!parser.next().equals("{")) throw RuntimeException("'{' expected.")
        memory.openScope()
    }

    /** Wrapper for [Memory.closeScope] */
    private fun closeScope() = memory.closeScope()

    private fun parsePrint() {

        // Save the variable and its value
        word = parser.next()
        val value = memory.get(word)

        // TODO("Check the word is not a key-word")

        println(value) // Output the value of the variable
    }
}