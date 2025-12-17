#!/bin/bash

echo "🚀 Preparando proyecto para Railway..."
echo ""

# Verificar que estamos en el directorio correcto
if [ ! -f "server.js" ]; then
    echo "❌ Error: Este script debe ejecutarse desde el directorio FiberDeskAPI"
    exit 1
fi

echo "✅ Directorio correcto"

# Verificar que git esté inicializado
if [ ! -d ".git" ]; then
    echo "📦 Inicializando repositorio git..."
    git init
    echo "✅ Git inicializado"
else
    echo "✅ Git ya inicializado"
fi

# Crear .gitignore si no existe
if [ ! -f ".gitignore" ]; then
    echo "📝 Creando .gitignore..."
    cat > .gitignore << EOF
node_modules/
.env
npm-debug.log
yarn-error.log
*.log
.DS_Store
remote-config.json
EOF
    echo "✅ .gitignore creado"
else
    echo "✅ .gitignore existe"
fi

# Verificar package.json tiene el script start
if ! grep -q '"start"' package.json; then
    echo "⚠️  Advertencia: package.json no tiene script 'start'"
    echo "   Agrega: \"start\": \"node server.js\""
else
    echo "✅ Script 'start' configurado"
fi

# Verificar que server.js use process.env.PORT
if ! grep -q "process.env.PORT" server.js; then
    echo "⚠️  Advertencia: server.js debe usar process.env.PORT"
else
    echo "✅ Puerto dinámico configurado"
fi

# Agregar todos los archivos
echo ""
echo "📦 Agregando archivos al staging..."
git add .

# Crear commit
echo "💾 Creando commit..."
git commit -m "Preparado para Railway deployment" || echo "ℹ️  No hay cambios para commitear"

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "✅ ¡Preparación completada!"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "📋 Próximos pasos:"
echo ""
echo "1. Sube tu código a GitHub:"
echo "   git remote add origin https://github.com/TU-USUARIO/TU-REPO.git"
echo "   git branch -M main"
echo "   git push -u origin main"
echo ""
echo "2. Ve a https://railway.app"
echo ""
echo "3. Sigue la guía: DEPLOY-RAILWAY.md"
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
