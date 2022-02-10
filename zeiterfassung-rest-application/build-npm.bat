@echo off
set zeiterfassungBaseDir=C:\Users\std\Documents\programmierung\IdeaProjects\Zeiterfassung\
set zeiterfassungUiBaseDir=%zeiterfassungBaseDir%zeiterfassung-web\src\main\ui\
set webTargetPath=%zeiterfassungBaseDir%zeiterfassung-rest-application\src\main\resources\static
set targetBuildOutput=%zeiterfassungUiBaseDir%dist\
set initialPath=%cd%

Rem actual build
cd "%zeiterfassungUiBaseDir%
call npm run build

Rem copy file back
if not exist "%webTargetPath%" mkdir "%webTargetPath%"
xcopy "%targetBuildOutput%" "%webTargetPath%" /S /Y
RMDIR %targetBuildOutput% /S /Q