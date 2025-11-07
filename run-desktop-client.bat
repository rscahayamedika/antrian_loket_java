@echo off
setlocal enableextensions

set "CLIENT_JAR=desktop-client\target\counter-desktop-client-0.1.0-SNAPSHOT-jar-with-dependencies.jar"

if not exist "%CLIENT_JAR%" (
    echo File jar klien desktop belum ditemukan.
    echo Jalankan: mvn -f desktop-client/pom.xml clean package
    goto :end
)

echo === Panggilan Loket Desktop ===
set "SERVER_URL=http://localhost:8080"
set /p "SERVER_URL=Masukkan URL server [http://localhost:8080]: "
if "%SERVER_URL%"=="" set "SERVER_URL=http://localhost:8080"

set "COUNTER_ID="
set /p "COUNTER_ID=Masukkan ID loket (mis. A): "

echo.
echo Menjalankan Counter Desktop Client...
if "%COUNTER_ID%"=="" (
    java -Dpanggilan.server="%SERVER_URL%" -jar "%CLIENT_JAR%"
) else (
    java -Dpanggilan.server="%SERVER_URL%" -Dpanggilan.counter="%COUNTER_ID%" -jar "%CLIENT_JAR%"
)

:end
echo.
pause
endlocal
