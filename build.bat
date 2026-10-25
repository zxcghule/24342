@echo off
chcp 65001 >nul
where gradle >nul 2>nul
if errorlevel 1 (
  echo Gradle не найден. Установи Gradle 8.5+ и JDK 17, потом запусти этот файл снова.
  pause
  exit /b 1
)
gradle build
echo.
echo Готово! Файл мода: build\libs\bodymarket-1.0.0.jar
pause
