@echo off
set JAVA=bundle\jre\bin\java.exe
for %%i in (.\jars\*.jar) do call ".\classpath.bat" %%i

%JAVA% com.github.mtzw.filecleaner.unsplit.FileCleaner