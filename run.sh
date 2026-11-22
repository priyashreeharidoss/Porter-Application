#!/usr/bin/env bash
# Run script for Porter Application (macOS & Linux)

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
LIB_JAR="$PROJECT_ROOT/PorterApp/lib/mysql-connector-j-8.0.29.jar"
BIN_DIR="$PROJECT_ROOT/PorterApp/bin"
SRC_FILE="$PROJECT_ROOT/PorterApp/src/Porterapp.java"

mkdir -p "$BIN_DIR"

echo "=========================================="
echo "       Starting Porter Application        "
echo "=========================================="

echo "Compiling Java sources..."
javac -cp "$LIB_JAR" -d "$BIN_DIR" "$SRC_FILE"
if [ $? -ne 0 ]; then
    echo "Compilation failed. Please check Java compiler errors above."
    exit 1
fi

echo "Running Porter Application..."
java -cp "$LIB_JAR:$BIN_DIR" Porterapp

