# 🌐 Configuración de Acceso Remoto - FiberDesk

## Opción 1: ngrok (Recomendado para Desarrollo)

### Instalación de ngrok

**Opción A - npm (Recomendado):**
```bash
npm install -g ngrok
```

**Opción B - Descarga directa:**
1. Ve a https://ngrok.com/download
2. Descarga el ejecutable para Windows
3. Extrae y añade al PATH de Windows

### Uso Rápido

1. **Ejecuta el script automático:**
   ```bash
   node setup-remote.js
   ```

2. **Obtendrás una URL pública como:**
   ```
   https://abc123.ngrok.io
   ```

3. **Configura en tu app Android:**
   - Abre la app FiberDesk
   - Ve a Configuración de Servidor
   - Ingresa: `abc123.ngrok.io` (sin http:// ni https://)
   - Puerto: `443` (para HTTPS) o `80` (para HTTP)

### Uso Manual

Si prefieres ejecutar manualmente:

```bash
# Terminal 1 - Servidor
npm start

# Terminal 2 - ngrok
ngrok http 3000
```

### ⚠️ Limitaciones de ngrok (versión gratuita):
- Túnel expira después de 2 horas (debes reiniciar)
- URL cambia cada vez que reinicias
- Límite de conexiones simultáneas

---

## Opción 2: Desplegar en la Nube (Permanente)

### Railway (Gratis + Fácil)

1. **Crea cuenta en Railway.app**

2. **Despliega el proyecto:**
   ```bash
   npm install -g @railway/cli
   railway login
   railway init
   railway up
   ```

3. **Añade variables de entorno:**
   ```bash
   railway variables set MONGO_URI="tu_mongodb_uri"
   railway variables set JWT_SECRET="tu_secret"
   railway variables set PORT=3000
   ```

4. **Obtendrás una URL permanente como:**
   ```
   https://fiberdesk-production.up.railway.app
   ```

### Render (Alternativa)

1. Ve a https://render.com
2. Conecta tu repositorio de GitHub
3. Configura las variables de entorno
4. Deploy automático

---

## Opción 3: Servidor VPS (Avanzado)

Si tienes tu propio servidor o VPS:

1. **Instala Node.js y MongoDB**
2. **Configura firewall:**
   ```bash
   sudo ufw allow 3000/tcp
   ```
3. **Usa PM2 para mantener el servidor:**
   ```bash
   npm install -g pm2
   pm2 start server.js --name fiberdesk-api
   pm2 startup
   pm2 save
   ```

---

## 📱 Configuración en la App Android

Después de obtener tu URL pública:

### En la App:
1. Abre FiberDesk
2. Ve a **Configuración** o al primer inicio
3. Ingresa la URL del servidor
4. Guarda

### En el Código (alternativa):
Edita `NetworkConfig.kt` y añade tu URL:

```kotlin
// En lugar de IP local, usa tu URL pública
val REMOTE_URL = "https://tu-url-publica.com"
```

---

## 🔒 Consideraciones de Seguridad

- ✅ Ya estás usando JWT para autenticación
- ✅ MongoDB Atlas ya está en la nube
- ⚠️ Para producción, considera:
  - Usar HTTPS obligatorio
  - Rate limiting
  - Validación de origen (CORS específico)
  - Firewall de aplicación web (WAF)

---

## 🆘 Solución de Problemas

### Error: "Cannot connect to server"
1. Verifica que el túnel esté activo
2. Revisa la URL en la app (sin espacios)
3. Prueba la URL en el navegador: `https://tu-url.com/api/health`

### Error: "Connection timeout"
1. Verifica tu conexión a internet
2. Intenta con WiFi diferente
3. Revisa que no haya firewall bloqueando

### ngrok no inicia
```bash
# Cierra instancias anteriores
taskkill /F /IM ngrok.exe

# Reinicia
ngrok http 3000
```

---

## 📊 Monitoreo

### Ver logs de ngrok:
Abre en tu navegador: http://127.0.0.1:4040

### Ver logs del servidor:
```bash
# Si usas PM2
pm2 logs fiberdesk-api

# Si ejecutas directamente
# Los logs aparecen en la consola
```
