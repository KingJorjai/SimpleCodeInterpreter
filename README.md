# Simple Code Interpreter

A lightweight interpreter for a custom scripting language that supports variable
assignment, scopes, and simple print functionality. This project is implemented
in Kotlin as part of my application for the **Distributed Persistent Project Indexing**
internship in **JetBrains**.

---

## Table of Contents

1. [Introduction](#introduction)
2. [Features](#features)
3. [Example Script](#example-script)
4. [Building the Project](#building-the-project)
5. [Running the Interpreter](#running-the-interpreter)

---

## Introduction

I had never used Kotlin before, but I thought this project would be a great opportunity
to learn it. I have been using Java for two years, and transitioning to Kotlin has felt
smooth and intuitive. I'm still adjusting to the paradigm shift, but features like type
inference and null safety have really caught my attention. I’m excited to continue
exploring Kotlin and look forward to using it in future projects.

I’m particularly interested in the Distributed Persistent Project Indexing internship
because I have always been fascinated by resource efficiency. Recently, I started
exploring the world of competitive programming, and I’ve enjoyed learning various
algorithms to solve complex problems.

This internship excites me because I see it as an incredible opportunity to learn new
things while contributing to a real-world project alongside experienced professionals.
Whenever I work on personal projects, I often wonder, *“How is this done in the real
world?”* For example, with GitHub repositories, I’ve taught myself how to write meaningful
commit messages, use branches effectively, handle pull requests, write comprehensive
README files, and choose appropriate licenses. I’ve learned these practices by watching
others on YouTube or Reddit. However, what truly excites me about this internship is
the chance to learn how professionals approach and execute these tasks in a real work
environment.

**TL;DR**:<br>
Switched from Java to Kotlin like it was nothing, and I’m loving it. Can’t wait
to join the internship, get better at making things efficient, and see how the pros
actually get stuff done.

---

## Features

- **Variable Assignment**: Assign integer values to variables or assign the value of another variable using the operator `=`.
- **Scope Management**: Manage inner and outer scopes using `scope {` and `}`.
- **Print Statement**: Print the value of a variable to the console using the `print` keyword.

---

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

---

## Building the Project

### 1. Clone the repository

``` bash
git clone https://github.com/KingJorjai/SimpleCodeInterpreter.git
cd SimpleCodeInterpreter/
```

### 2. Build the project

``` bash
./gradlew build
```

The compiled JAR file will be located in the `build/libs/` directory.

```bash
cd build/libs/
```

---

## Running the Interpreter
To run a program through the interpreter, use the following command,
substituting `<text_file>` for the file to interpret.

```bash
java -jar SimpleCodeInterpreter-1.0-SNAPSHOT.jar <text_file>
```
Example scripts can be found in `src/test/resources/input/`.
To run them from the `build/libs/`, execute the following command.

```bash
java -jar SimpleCodeInterpreter-1.0-SNAPSHOT.jar ../../src/test/resources/input/general_test.txt
```

