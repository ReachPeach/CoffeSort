cd %~dp0
cd ..\..\..\
set libs=lib\*
set src=src\main\java
rd /s /q build
mkdir build
javac -d build -cp src;%libs% %src%\app\*.java %src%\common\*.java %src%\domain\*.java %src%\exceptions\*.java %src%\gui\*.java %src%\models\*.java %src%\persistence\*.java %src%\validator\*.java src\utility\*.java
xcopy resources\META-INF\persistence.xml build\META-INF\
java -cp build;%libs% utility.CreateCoffeeSortsDatabase
java -cp build;%libs% main.java.app.ApplicationStarter