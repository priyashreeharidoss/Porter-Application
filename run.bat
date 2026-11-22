@echo off
REM Run script for Porter Application (Windows)

set PROJECT_ROOT=%~dp0
set LIB_JAR=%PROJECT_ROOT%PorterApp\lib\mysql-connector-j-8.0.29.jar
set BIN_DIR=%PROJECT_ROOT%PorterApp\bin
set SRC_FILE=%PROJECT_ROOT%PorterApp\src\Porterapp.java

if not exist "%BIN_DIR%" mkdir "%BIN_DIR%"

echo ==========================================
echo        Starting Porter Application        
echo ==========================================

echo Compiling Java sources...
javac -cp "%LIB_JAR%" -d "%BIN_DIR%" "%SRC_FILE%"
if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed. Please check Java compiler errors above.
    pause
    exit /b %ERRORLEVEL%
)

echo Running Porter Application...
java -cp "%LIB_JAR%;%BIN_DIR%" Porterapp
pause

