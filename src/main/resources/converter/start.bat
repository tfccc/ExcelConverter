@echo off

SET JAVA_HOME=.\jre

SET Path=%JAVA_HOME%\bin;

java -Dloader.path=./lib -jar ./ExcelConverter.jar

pause
