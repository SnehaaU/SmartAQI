@echo off
REM Build SmartAQI for Apache Tomcat 9
REM 1. Set CATALINA_HOME to your Tomcat folder
REM 2. Put mysql-connector-j-8.x.jar in backend\ folder

if "%CATALINA_HOME%"=="" (
    echo Set CATALINA_HOME first, e.g.:
    echo   set CATALINA_HOME=C:\apache-tomcat-9.0.96
    exit /b 1
)

if not exist "backend\mysql-connector-j-8.4.0.jar" (
    if not exist "backend\mysql-connector-j-9.7.0.jar" (
        echo Download MySQL Connector/J and save as backend\mysql-connector-j-8.4.0.jar
        echo https://dev.mysql.com/downloads/connector/j/
        exit /b 1
    )
    set MYSQL_JAR=backend\mysql-connector-j-9.7.0.jar
) else (
    set MYSQL_JAR=backend\mysql-connector-j-8.4.0.jar
)

if not exist "WEB-INF\lib" mkdir "WEB-INF\lib"
if not exist "WEB-INF\classes" mkdir "WEB-INF\classes"
copy /Y "%MYSQL_JAR%" "WEB-INF\lib\" >nul

if exist "backend\lib\servlet-api.jar" (
    set SERVLET_JAR=backend\lib\servlet-api.jar
) else (
    set SERVLET_JAR=%CATALINA_HOME%\lib\servlet-api.jar
)
set CP=WEB-INF\lib\mysql-connector-j-8.4.0.jar;%SERVLET_JAR%
if exist "WEB-INF\lib\mysql-connector-j-9.7.0.jar" set CP=WEB-INF\lib\mysql-connector-j-9.7.0.jar;%SERVLET_JAR%

dir /s /b backend\src\*.java > sources.txt
javac -encoding UTF-8 -cp "%CP%" -d WEB-INF\classes @sources.txt
del sources.txt

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo BUILD FAILED - fix errors above.
    echo If jar error: re-download mysql-connector-j - about 2.5 MB. Do not open .jar in editor.
    pause
    exit /b 1
)

echo.
echo Build OK. Copy this folder to %%CATALINA_HOME%%\webapps\SmartAQI
pause
