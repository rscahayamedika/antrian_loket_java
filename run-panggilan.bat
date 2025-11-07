@echo off
rem Jalankan aplikasi panggilan loket dengan Maven Spring Boot
setlocal
set "SCRIPT_DIR=%~dp0"
cd /d "%SCRIPT_DIR%"

where mvn >nul 2>&1
if errorlevel 1 (
    echo Maven tidak ditemukan di PATH. Pastikan Maven terpasang dan variabel PATH sudah diset.
    exit /b 1
)

mvn spring-boot:run
endlocal
