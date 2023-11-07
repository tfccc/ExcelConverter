@echo off

SET JAVA_HOME=.\jre

SET Path=%JAVA_HOME%\bin;

java -jar ./ExcelConverter.jar

pause
