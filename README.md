
# 📘 Compiler Project – Full Implementation

This repository contains the full implementation of my **mini-Java compiler**, developed as part of the *Compilation Course*.  
The compiler translates a custom Java-like language into **SPIM/MIPS assembly**, going through all classic compilation stages.

## 🚀 Features

### ✔ 1. Lexical Analysis  
Implemented using **JFlex** (`.lex` file).  
The lexer converts raw text into validated tokens and handles:
- keywords  
- identifiers  
- operators  
- literals  
- comments  
- whitespace  

### ✔ 2. Parsing (Syntax Analysis)  
Implemented using **CUP** (`.cup` file).  
The parser:
- validates syntax  
- detects grammar errors  
- constructs the **Abstract Syntax Tree (AST)**  

### ✔ 3. AST (Abstract Syntax Tree)  
Custom Java classes represent every language construct, including:
- expressions  
- statements  
- declarations  
- control flow  
- methods  
- classes  

Each AST node implements an `SemantMe()` and `IRme()` function for later phases.

### ✔ 4. Semantic Analysis  
Uses a hierarchical **symbol table** with lexical scopes.  
Checks:
- type correctness  
- method calls  
- variable declarations  
- inheritance rules  
- function overloading  
- return-type correctness  
- forbidden operations  

Errors are reported with line numbers and messages.

### ✔ 5. IR (Intermediate Representation) Generation  
AST nodes are lowered into a platform-independent IR that consists of:
- temporary registers  
- arithmetic commands  
- control flow commands  
- function calls  
- memory loads/stores  

This IR makes code generation cleaner and easier.

### ✔ 6. MIPS/SPIM Code Generation  
The compiler finally generates:
- `.text` section  
- `.data` section  
- function prolog/epilog  
- heap & stack management  
- class method tables (v-tables)  
- object allocation logic  

The output is compatible with **SPIM/MARS**.

## 📂 Project Structure

```
Compiler-Project/
 ├── jflex/                 # JFlex scanner specification (.lex)
 ├── cup/                   # CUP grammar file (.cup)
 ├── src/
 │    ├── AST/              # All AST node classes
 │    ├── SYMBOL_TABLE/     # Scoping + symbol table logic
 │    ├── TYPES/            # Type system for semantic checks
 │    ├── IR/               # Intermediate Representation classes
 │    ├── MIPS/             # Final code generation
 │    ├── UTILS/            # Error/utility classes
 │    └── Main.java         # Compiler entry point
 ├── output/                # Compiled output (.spim)
 ├── Makefile               # Build and run instructions
 └── README.md              # This file
```

## 🏗️ Build Instructions

### 1. Install dependencies
You need:
- Java (JDK 8+)
- JFlex
- CUP
- SPIM / MARS

### 2. Build the compiler

```
make all
```

### 3. Run the compiler on a test file

```
make run in=<input file>
```

### 4. Run the generated program in SPIM

```
spim -file output/code.s
```

## 📡 Example

Input program:

```java
class A {
    int x;

    int getX() {
        return x;
    }
}
```

Output (simplified IR):

```
LABEL A_getX:
MOVE R1, this.x
RETURN R1
```

Output (MIPS):

```
A_getX:
    lw $v0, 0($a0)
    jr $ra
```

## 👤 Author

**Mohamad**  
GitHub:  
https://github.com/mohamad99988765434567

## 📜 License

This project is for educational use in the **Compilation Course**.
