@echo off
REM Script para iniciar el backend FiberDesk

echo ====================================
echo Iniciando FiberDesk Backend
echo ====================================
echo.

cd /d "%~dp0"

echo Verificando Node.js...
node --version
if errorlevel 1 (
    echo ERROR: Node.js no esta instalado
    pause
    exit /b 1
)

echo.
echo Verificando dependencias...
if not exist "node_modules" (
    echo Instalando dependencias...
    npm install
)

echo.
echo Iniciando servidor en http://localhost:3000
echo.
echo IMPORTANTE: Deja esta ventana ABIERTA
echo.

npm run dev

pause
