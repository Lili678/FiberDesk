# 🚀 Despliegue Permanente en Railway

## ¿Qué es Railway?
Railway es una plataforma de despliegue en la nube que ofrece:
- ✅ **$5 USD gratis al mes** (suficiente para desarrollo)
- ✅ **URL permanente** (no expira)
- ✅ **HTTPS automático**
- ✅ **Despliegue en 5 minutos**
- ✅ **Logs en tiempo real**

---

## 📋 Requisitos Previos

1. **Cuenta de GitHub** (para conectar tu código)
2. **Cuenta de Railway** (crear en https://railway.app)

---

## 🎯 Guía Paso a Paso

### Paso 1: Preparar el Repositorio

1. **Asegúrate de que tu código esté en GitHub:**
   ```bash
   cd FiberDeskAPI
   git add .
   git commit -m "Preparado para Railway"
   git push origin main
   ```

2. **Verifica que tengas estos archivos:**
   - ✅ `package.json` con scripts de inicio
   - ✅ `server.js` configurado para usar `process.env.PORT`
   - ✅ `.env` (NO subir a GitHub, configuraremos en Railway)

### Paso 2: Crear Proyecto en Railway

1. **Ve a:** https://railway.app

2. **Haz clic en "Start a New Project"**

3. **Selecciona "Deploy from GitHub repo"**

4. **Conecta tu cuenta de GitHub** (si es la primera vez)

5. **Selecciona el repositorio:** `FiberDesk`

6. **Selecciona la carpeta:** `FiberDeskAPI`

### Paso 3: Configurar Variables de Entorno

En el dashboard de Railway, ve a **Variables** y agrega:

```env
MONGO_URI=mongodb+srv://admin:admin@cluster0.y3q9qmd.mongodb.net/myDatabase?retryWrites=true&w=majority
JWT_SECRET=10912214
PORT=3000
NODE_ENV=production
```

⚠️ **IMPORTANTE:** Usa tus propias credenciales reales, no compartas las del ejemplo.

### Paso 4: Generar Dominio Público

1. En Railway, ve a la pestaña **Settings**

2. Sección **Networking** → **Generate Domain**

3. Railway generará un dominio como:
   ```
   fiberdesk-production.up.railway.app
   ```

4. **¡Esta es tu URL permanente!** 🎉

### Paso 5: Verificar Despliegue

1. **Verifica que el servicio esté corriendo:**
   - Ve a la pestaña **Deployments**
   - Debe mostrar "Deployed" en verde

2. **Prueba la API en tu navegador:**
   ```
   https://tu-app.up.railway.app/api/health
   ```
   
   Deberías ver:
   ```json
   {
     "status": "ok",
     "message": "FiberDesk API funcionando correctamente",
     "timestamp": "2025-12-14T..."
   }
   ```

### Paso 6: Configurar en tu App Android

1. **Abre tu app FiberDesk**

2. **Ve a Configuración de Servidor** o primer inicio

3. **Ingresa:**
   - Servidor: `tu-app.up.railway.app`
   - Puerto: `443` (para HTTPS)

4. **Guarda y prueba la conexión**

---

## 🔄 Actualizaciones Automáticas

Railway se actualiza automáticamente cuando haces push a GitHub:

```bash
# Haces cambios en tu código
git add .
git commit -m "Actualización"
git push origin main

# Railway detecta el cambio y redespliega automáticamente
```

---

## 📊 Monitoreo y Logs

### Ver Logs en Tiempo Real:
1. Ve a tu proyecto en Railway
2. Click en **View Logs**
3. Verás todos los console.log() de tu aplicación

### Métricas:
- CPU usage
- Memory usage
- Network traffic
- Todo visible en el dashboard

---

## 💰 Costos

### Plan Hobby (Gratuito):
- **$5 USD en créditos al mes** (gratis)
- Suficiente para:
  - ~100,000 requests/mes
  - 1 GB RAM
  - 1 GB almacenamiento
  - Perfecto para desarrollo y testing

### Si necesitas más:
- **Plan Pro:** $20/mes
- Más recursos y dominios personalizados

---

## 🔧 Solución de Problemas

### Error: "Application failed to start"

1. **Verifica las variables de entorno:**
   - Todas las variables necesarias están configuradas
   - MONGO_URI es correcto

2. **Revisa los logs:**
   - Click en "View Logs"
   - Busca mensajes de error

3. **Verifica package.json:**
   ```json
   "scripts": {
     "start": "node server.js"
   }
   ```

### Error: "Cannot connect from app"

1. **Verifica la URL en la app:**
   - Sin `http://` o `https://`
   - Solo el dominio: `tu-app.up.railway.app`
   - Puerto: `443`

2. **Prueba en navegador:**
   ```
   https://tu-app.up.railway.app/api/health
   ```

3. **Verifica CORS en server.js:**
   ```javascript
   app.use(cors()); // Debe estar antes de las rutas
   ```

### La API responde lento

1. **Railway "duerme" apps gratuitas después de inactividad**
   - Primera request puede tardar 10-30 segundos
   - Requests subsecuentes son rápidas

2. **Solución:** Hacer ping cada 10 minutos
   - Puedes usar un servicio como UptimeRobot (gratis)

---

## 🔒 Seguridad en Producción

### Variables de Entorno
✅ **NUNCA** subas `.env` a GitHub
✅ Usa variables de entorno de Railway
✅ Usa contraseñas fuertes para MongoDB

### MongoDB Atlas
✅ Configura IP whitelist (0.0.0.0/0 para Railway)
✅ Usa usuario/contraseña seguras
✅ Habilita autenticación

### JWT
✅ Usa `JWT_SECRET` largo y aleatorio
✅ No uses valores por defecto en producción

---

## 🎓 Comandos Útiles de Railway CLI (Opcional)

Si prefieres línea de comandos:

```bash
# Instalar Railway CLI
npm install -g @railway/cli

# Login
railway login

# Vincular proyecto existente
railway link

# Ver logs
railway logs

# Abrir en navegador
railway open

# Ejecutar comando en Railway
railway run node server.js
```

---

## 📱 Actualizar URL en la App

### Opción 1: Manualmente en cada dispositivo
Usuarios configuran en Settings de la app

### Opción 2: Hardcodear en código (no recomendado)
En `NetworkConfig.kt`:
```kotlin
private const val PRODUCTION_URL = "https://tu-app.up.railway.app/api/"
```

### Opción 3: Remote Config (avanzado)
Usar Firebase Remote Config para cambiar URLs sin actualizar app

---

## 🚀 Alternativas a Railway

Si Railway no te funciona, puedes usar:

### Render.com
- Similar a Railway
- $0 gratis con limitaciones
- Deployment desde GitHub

### Fly.io
- Más técnico pero muy potente
- Free tier generoso

### Heroku
- Ya no tiene tier gratuito
- $5-7/mes mínimo

---

## ✅ Checklist Final

Antes de usar en producción:

- [ ] API desplegada en Railway
- [ ] URL pública funcionando
- [ ] Health endpoint responde: `/api/health`
- [ ] MongoDB Atlas accesible desde Railway
- [ ] Variables de entorno configuradas
- [ ] App Android configurada con nueva URL
- [ ] Login funciona desde app
- [ ] Inventario funciona desde app
- [ ] Pagos funciona desde app

---

## 🆘 Soporte

Si tienes problemas:
1. Revisa los logs en Railway
2. Verifica las variables de entorno
3. Prueba endpoints en navegador/Postman
4. Verifica MongoDB Atlas está accesible

¿Todo funcionando? ¡Felicidades! 🎉 Tu app ya está en la nube.
