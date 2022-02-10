@echo off
Rem build web resources
call build-npm.bat

set targetJar=zeiterfassung-rest-application.jar
set targetPath=%zeiterfassungBaseDir%zeiterfassung-rest-application\build\libs\
set pathToJdk="C:\Program Files\Java\jdk-11"

Rem actual build
cd "%zeiterfassungBaseDir%zeiterfassung-zeiterfassung"
set GRADLE_OPTS=-Dfile.encoding=utf-8
set JAVA_HOME=%pathToJdk%
call gradlew clean build shadowJar

Rem copy file back
cd %initialPath%
cd..
xcopy "%targetPath%%targetJar%" /Y

Rem clean up the static stuff, since we don't need it any more
RMDIR %webTargetPath% /S /Q
pause