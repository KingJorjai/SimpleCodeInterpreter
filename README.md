# Simple Code Interpreter

A lightweight interpreter for a custom scripting language that supports variable assignment, scopes, and simple print functionality. This project is implemented in Kotlin as part of my application for the **Distributed Persistent Project Indexing** internship in **Jetbrains**

## Table of Contents

1. [Features](#features)
2. [Example Script](#example-script)


## Features

- **Variable Assignment**: Assign integer values to variables or assign the value of another variable using the operand `=`.
- **Scope Management**: Manage local and global scopes using `scope {` and `}`.
- **Print Statement**: Print the value of a variable to the console using the `print` keyword.

## Example Script

### Input
```plaintext
x = 1
print x
scope {
 x = 2
 print x
 scope {
   x = 3
   y = x
   print x
   print y
 }
 print x
 print y
}
print x
```
### Output
```plaintext
1
2
3
3
2
null
1
```
