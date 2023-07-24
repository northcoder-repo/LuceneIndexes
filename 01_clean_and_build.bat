@echo off

REM compiles the source code, runs the unit tests,
REM and assembles the jar file:

REM note the deliberate lack of quotes surrounding the path:
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-20.0.1.9-hotspot

REM maven itself uses a batch file so each mvn command must 
REM be preceded by "call":
call C:\maven\apache-maven-3.8.5\bin\mvn.cmd clean install

pause
