@echo off
REM Script para preparar proyecto para Railway en Windows

echo.
echo Preparando proyecto para Railway...
echo.

REM Verificar que estamos en el directorio correcto
if not exist "server.js" (
    echo Error: Este script debe ejecutarse desde el directorio FiberDeskAPI
    exit /b 1
)

echo [OK] Directorio correcto

REM Verificar que git este inicializado
if not exist ".git" (
    echo Inicializando repositorio git...
    git init
    echo [OK] Git inicializado
) else (
    echo [OK] Git ya inicializado
)

REM Crear .gitignore si no existe
if not exist ".gitignore" (
    echo Creando .gitignore...
    (
        echo node_modules/
        echo .env
        echo npm-debug.log
        echo yarn-error.log
        echo *.log
        echo .DS_Store
        echo remote-config.json
    ) > .gitignore
    echo [OK] .gitignore creado
) else (
    echo [OK] .gitignore existe
)

REM Verificar package.json
findstr /C:"\"start\"" package.json >nul
if errorlevel 1 (
    echo [!] Advertencia: package.json no tiene script 'start'
    echo     Agrega: "start": "node server.js"
) else (
    echo [OK] Script 'start' configurado
)

REM Verificar server.js
findstr /C:"process.env.PORT" server.js >nul
if errorlevel 1 (
    echo [!] Advertencia: server.js debe usar process.env.PORT
) else (
    echo [OK] Puerto dinamico configurado
)

echo.
echo Agregando archivos al staging...
git add .

echo Creando commit...
git commit -m "Preparado para Railway deployment"

echo.
echo ============================================
echo [OK] Preparacion completada!
echo ============================================
echo.
echo Proximos pasos:
echo.
echo 1. Sube tu codigo a GitHub:
echo    git remote add origin https://github.com/TU-USUARIO/TU-REPO.git
echo    git branch -M main
echo    git push -u origin main
echo.
echo 2. Ve a https://railway.app
echo.
echo 3. Sigue la guia: DEPLOY-RAILWAY.md
echo.
echo ============================================
echo.
pause
