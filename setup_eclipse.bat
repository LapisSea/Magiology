@echo off
goto Yes

:yes
call gradlew setupDecompWorkspace eclipse --refresh-dependencies
if %errorlevel%==1 goto que

:no
exit

:que

set /p menu="Do you want to retry? (Y/N): "
if %menu%==Y goto Yes
if %menu%==y goto Yes
if %menu%==N goto No
if %menu%==n goto No
goto que