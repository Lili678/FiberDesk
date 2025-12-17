# 📱 Guía Rápida: Usar Datos Móviles con ngrok

## 🎯 Problema
Tu servidor backend corre localmente (`192.168.12.208:3000`) y solo funciona con WiFi. Para usar datos móviles necesitas una URL pública.

---

## ⚡ Solución con ngrok (2 minutos)

### Paso 1: Descargar ngrok (si no lo tienes)

1. Ve a: https://ngrok.com/download
2. Descarga **ngrok para Windows**
3. Descomprime el archivo `ngrok.exe` en una carpeta (ejemplo: `C:\ngrok\`)

### Paso 2: Exponer tu servidor

**EN UNA TERMINAL NUEVA** (deja el servidor corriendo en la otra):

```powershell
# Si pusiste ngrok en C:\ngrok\
cd C:\ngrok
.\ngrok.exe http 3000
```

O si está en tu PATH:
```powershell
ngrok http 3000
```

### Paso 3: Copiar la URL pública

Verás algo como esto:

```
Session Status    online
Forwarding        https://abc123xyz.ngrok.io -> http://localhost:3000
```

**Copia la URL:** `https://abc123xyz.ngrok.io`

---

## 📱 Configurar en la App Android

### Paso 4: Abrir configuración en la app

1. Abre FiberDesk en tu dispositivo
2. Ve a **⚙️ Configuración** → **Configuración del Servidor**

### Paso 5: Configurar URL remota

En la sección **"📱 Servidor para Datos Móviles"**:

1. ✅ Activa: **"Usar servidor remoto con datos móviles"**
2. En **"URL del Servidor Remoto"**, pega: `https://abc123xyz.ngrok.io`
3. Presiona **"Guardar Configuración"**

### Paso 6: Probar

1. **Desactiva WiFi** en tu dispositivo
2. **Activa datos móviles**
3. Abre la app FiberDesk
4. Presiona **"Probar Conexión"**
5. ✅ Debería conectarse exitosamente

---

## 🔍 Verificar en Logcat

En Android Studio → Logcat, filtra por `NetworkConfig`:

**Con datos móviles verás:**
```
📱 DATOS MÓVILES detectados → Usando servidor remoto
🔗 URL seleccionada: https://abc123xyz.ngrok.io/api/
```

**Con WiFi verás:**
```
📶 WiFi → Usando IP local: 192.168.12.208
🔗 URL seleccionada: http://192.168.12.208:3000/api/
```

---

## ⚠️ IMPORTANTE sobre ngrok

### ❌ Desventaja:
- La URL cambia cada vez que reinicias ngrok
- Tendrás que actualizar la URL en la app cada vez

### ✅ Solución permanente:
Si quieres una URL que **NO cambie**, usa **Railway** (gratis):
- Sigue la guía: `FiberDeskAPI/DEPLOY-RAILWAY.md`
- Obtendrás una URL permanente como: `https://fiberdesk.railway.app`

---

## 🛠️ Comandos Útiles

### Iniciar todo:

**Terminal 1 (Backend):**
```powershell
cd FiberDeskAPI
npm run dev
```

**Terminal 2 (ngrok):**
```powershell
ngrok http 3000
```

### Detener:
- Backend: `Ctrl + C`
- ngrok: `Ctrl + C`

---

## ❓ Solución de Problemas

### Error: "No se puede conectar con datos móviles"
✅ Verifica que ngrok esté corriendo
✅ Verifica que la URL en la app sea correcta (copia/pega desde ngrok)
✅ Asegúrate de que empiece con `https://`

### Error: "ngrok no se reconoce como comando"
✅ Usa la ruta completa: `C:\ngrok\ngrok.exe http 3000`
✅ O agrega ngrok al PATH de Windows

### La URL de ngrok no funciona
✅ Asegúrate de que tu backend esté corriendo primero
✅ Prueba acceder a la URL en un navegador: `https://tu-url.ngrok.io/api/health`

---

## 🎉 Resumen

1. **Backend corriendo**: `npm run dev` en Terminal 1
2. **ngrok corriendo**: `ngrok http 3000` en Terminal 2
3. **App configurada**: URL de ngrok en "Servidor para Datos Móviles"
4. **Listo**: Cambia entre WiFi y datos móviles automáticamente

**La app detectará automáticamente qué conexión usas y cambiará el servidor.**
