@echo off
set CATALINA_HOME=C:\Users\sneha\Downloads\apache-tomcat-9.0.118-windows-x64\apache-tomcat-9.0.118
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_202

cd /d "%~dp0"
call compile.bat
if %ERRORLEVEL% NEQ 0 (
    echo Deploy failed - fix compile errors first.
    pause
    exit /b 1
)

xcopy "%~dp0" "%CATALINA_HOME%\webapps\SmartAQI\" /E /I /Y /Q
echo.
echo Done! Open: http://localhost:8080/SmartAQI/
pause
