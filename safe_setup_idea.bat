@echo off
echo Cleaning up workspace!
echo ...
echo ...
call clean_up_workspace.bat
pause
echo ...
echo ...
echo Setting up workspace!
echo ...
echo ...
call setup_idea.bat
pause