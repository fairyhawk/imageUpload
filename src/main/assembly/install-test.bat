@echo off
echo [INFO] ===========Install test.===========
cd %~dp0
cd ../../../
call mvn compile war:war -Ptest -Dmaven.test.skip=true
pause