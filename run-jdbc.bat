@echo off
REM Compilation
javac -cp "lib\mysql-connector-j-9.5.0.jar" -d bin src\jdbc\Main.java

REM Exécution
java -cp "bin;lib\mysql-connector-j-9.5.0.jar" src.jdbc.Main
pause
