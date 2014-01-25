@echo off
echo [INFO] ===========start tomcat7============== 
cd %~dp0
cd ../../../
call mvn tomcat7:run -Pmine
pause