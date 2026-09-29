# Run in VS Code terminal:  .\start-tomcat.ps1
$env:CATALINA_HOME = "C:\Users\sneha\Downloads\apache-tomcat-9.0.118-windows-x64\apache-tomcat-9.0.118"
$env:JAVA_HOME = "C:\Program Files\Java\jdk1.8.0_202"

if (-not (Test-Path "$env:CATALINA_HOME\bin\startup.bat")) {
    Write-Host "ERROR: Tomcat not found at CATALINA_HOME. Edit start-tomcat.ps1 with your Tomcat path." -ForegroundColor Red
    exit 1
}

Write-Host "Starting Tomcat..." -ForegroundColor Green
& "$env:CATALINA_HOME\bin\startup.bat"
