# SASL Compiler
Compiler for the functional programming language [SASL](https://en.wikipedia.org/wiki/SASL_(programming_language)) written in Java. See `Books/sasl.ps.gz` for a theoretical foundation of the implementation and for general information on the programming language SASL. The subdirectory `code-samples` contains programming samples in SASL for reference. The code was tested with Java version 13.0.2 and runs without any thirdparty dependencies.

# Build
```bash
javac -sourcepath compiler/src -d compiler/bin compiler/src/Driver/Driver.java
```

# Usage

## General
```bash
java -cp compiler/bin Driver.Driver CODE-FILE.sasl
```

## Example
```bash
java -cp compiler/bin Driver.Driver code-samples/quicksort.sasl
```
