@echo off
set CATALINA_HOME=C:\Users\sneha\Downloads\apache-tomcat-9.0.118-windows-x64\apache-tomcat-9.0.118
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_202
call "%CATALINA_HOME%\bin\startup.bat"
echo.
echo  Tomcat started! Wait 5 seconds, then open:
echo  http://localhost:8080/SmartAQI/
echo.
echo  Or run:  open-app.bat
echo.
timeout /t 5 /nobreak >nul
start http://localhost:8080/SmartAQI/
