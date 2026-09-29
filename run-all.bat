@echo off
REM One click: deploy + start Tomcat + open browser
set CATALINA_HOME=C:\Users\sneha\Downloads\apache-tomcat-9.0.118-windows-x64\apache-tomcat-9.0.118
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_202

cd /d "%~dp0"
echo [1/3] Building and deploying...
call deploy.bat
if %ERRORLEVEL% NEQ 0 exit /b 1

echo [2/3] Starting Tomcat...
call "%CATALINA_HOME%\bin\startup.bat"

echo [3/3] Opening browser in 5 seconds...
timeout /t 5 /nobreak >nul
start http://localhost:8080/SmartAQI/
echo Done!
