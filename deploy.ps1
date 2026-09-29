# Run in VS Code terminal:  .\deploy.ps1
$env:CATALINA_HOME = "C:\Users\sneha\Downloads\apache-tomcat-9.0.118-windows-x64\apache-tomcat-9.0.118"
$env:JAVA_HOME = "C:\Program Files\Java\jdk1.8.0_202"

Set-Location $PSScriptRoot
cmd /c "compile.bat < nul"
xcopy "$PSScriptRoot" "$env:CATALINA_HOME\webapps\SmartAQI\" /E /I /Y /Q

Write-Host "Deployed! Open: http://localhost:8080/SmartAQI/" -ForegroundColor Green
