# 📱 Configuración para Datos Móviles

## 🎯 Cómo funciona ahora

Tu app FiberDesk ahora detecta **automáticamente** el tipo de conexión y cambia entre servidores:

### 📶 Con WiFi
- Usa el servidor local (IP de tu PC: `192.168.x.x`)
- Perfecto para desarrollo y uso en casa/oficina

### 📱 Con Datos Móviles
- Usa el servidor remoto (URL en la nube)
- Permite trabajar desde cualquier lugar

---

## ⚙️ Configuración en la App

### Paso 1: Abrir Configuración
1. Abre la app FiberDesk
2. Ve a **Configuración** → **Configuración del Servidor**

### Paso 2: Configurar Servidor Local (WiFi)
En la sección "Configuración del Servidor":
- **IP del Servidor**: `192.168.12.208` (tu IP actual)
- **Puerto**: `3000`
- Puedes activar "Detectar servidor automáticamente"

### Paso 3: Configurar Servidor Remoto (Datos Móviles)
En la sección "📱 Servidor para Datos Móviles":
- Activa: **"Usar servidor remoto con datos móviles"** ✅
- Ingresa la URL de tu servidor en la nube

**Ejemplo de URLs válidas:**
```
https://fiberdesk.railway.app
https://abc123.ngrok.io
http://98.123.45.67:3000
```

### Paso 4: Guardar
- Presiona **"Guardar Configuración"**
- Presiona **"Probar Conexión"** para verificar

---

## 🚀 Opciones para Servidor Remoto

### Opción 1: Railway (Recomendado - Permanente) ⭐

**Ventajas:**
- ✅ Gratis ($5 USD/mes de crédito)
- ✅ URL permanente
- ✅ HTTPS automático
- ✅ No caduca

**Pasos rápidos:**
1. Ve a https://railway.app y crea una cuenta
2. Conecta tu repositorio de GitHub
3. Selecciona la carpeta `FiberDeskAPI`
4. Railway generará una URL como: `https://fiberdesk-production.up.railway.app`
5. Copia esa URL y pégala en la app

**📖 Guía detallada:** Ver archivo `FiberDeskAPI/DEPLOY-RAILWAY.md`

---

### Opción 2: ngrok (Rápido - Temporal) ⚡

**Ventajas:**
- ✅ Setup en 2 minutos
- ✅ No requiere cuenta GitHub
- ⚠️ URL cambia cada vez que reinicias

**Pasos:**

1. **Descarga ngrok:**
   - Ve a: https://ngrok.com/download
   - Descarga para Windows
   - Descomprime en cualquier carpeta

2. **Inicia tu servidor local:**
   ```bash
   cd FiberDeskAPI
   npm start
   ```

3. **Exponer con ngrok:**
   Abre otra terminal y ejecuta:
   ```bash
   ngrok http 3000
   ```

4. **Copia la URL:**
   ngrok te mostrará algo como:
   ```
   Forwarding    https://abc123.ngrok.io -> http://localhost:3000
   ```
   Copia: `https://abc123.ngrok.io`

5. **Pégala en la app:**
   - Abre configuración en la app
   - Pega la URL en "URL del Servidor Remoto"
   - Guarda

**⚠️ Importante:** Cada vez que reinicies ngrok, la URL cambiará y tendrás que actualizarla en la app.

---

### Opción 3: Render (Gratis - Permanente)

**Ventajas:**
- ✅ Completamente gratis
- ✅ URL permanente
- ✅ HTTPS automático

**Pasos:**
1. Ve a https://render.com y crea una cuenta
2. Click en "New +" → "Web Service"
3. Conecta tu repositorio de GitHub
4. Configuración:
   - **Root Directory**: `FiberDeskAPI`
   - **Build Command**: `npm install`
   - **Start Command**: `npm start`
5. Click "Create Web Service"
6. Render te dará una URL como: `https://fiberdesk.onrender.com`
7. Copia y pega en la app

---

## 🧪 Probando la Configuración

### Verificar conexión WiFi:
1. Conéctate a WiFi
2. Abre la app
3. Debería conectarse a `192.168.12.208:3000`
4. Revisa los logs: `Logcat` → busca `NetworkConfig`

### Verificar conexión Datos Móviles:
1. Desactiva WiFi
2. Activa datos móviles
3. Abre la app
4. Debería conectarse a tu URL remota
5. Revisa los logs: `Logcat` → busca `📱 DATOS MÓVILES detectados`

---

## 📊 Logs de Depuración

La app muestra logs detallados en Logcat con el tag `NetworkConfig`:

```
=========================================
📡 Tipo de conexión: WiFi 📶
🖥️  Entorno: DISPOSITIVO FÍSICO
🔗 URL seleccionada: http://192.168.12.208:3000/api/
=========================================
```

O con datos móviles:

```
=========================================
📡 Tipo de conexión: DATOS MÓVILES 📱
🖥️  Entorno: DISPOSITIVO FÍSICO
🔗 URL seleccionada: https://fiberdesk.railway.app/api/
=========================================
```

---

## ❓ Preguntas Frecuentes

**P: ¿Necesito configurar ambos servidores (local y remoto)?**
R: No es obligatorio. Si solo usas WiFi, solo configura el local. Si quieres usar datos móviles, configura el remoto.

**P: ¿Puedo usar solo el servidor remoto para todo?**
R: Sí, puedes desactivar "Usar servidor remoto con datos móviles" y poner la URL remota en "IP del Servidor". Funcionará para WiFi y datos.

**P: ¿La app cambia automáticamente entre servidores?**
R: Sí, si tienes ambos configurados, la app detecta tu conexión y cambia automáticamente.

**P: ¿Qué pasa si no configuro servidor remoto?**
R: La app seguirá funcionando, pero solo con WiFi. Con datos móviles no podrá conectarse.

**P: ¿Railway es realmente gratis?**
R: Sí, dan $5 USD/mes gratis, suficiente para una app de desarrollo. Si se acaba, puedes cambiar a otra opción.

---

## 🛠️ Solución de Problemas

### Error: "No se puede conectar con datos móviles"
✅ Verifica que hayas configurado la URL remota en la app
✅ Asegúrate de que el servidor remoto esté corriendo
✅ Prueba la URL en un navegador móvil

### Error: "La URL remota no funciona"
✅ Verifica que empiece con `http://` o `https://`
✅ No incluyas `/api/` al final, la app lo agrega automáticamente
✅ Prueba acceder a `tu-url/api/health` en un navegador

### La app no detecta el cambio de red
✅ Cierra y vuelve a abrir la app
✅ Ve a Configuración → Guardar (esto invalida el caché)

---

## 📞 Soporte

Si tienes problemas, revisa los logs de la app con el filtro `NetworkConfig` en Android Studio Logcat.

---

**¡Listo! Ahora tu app funciona con WiFi y datos móviles sin instalar nada adicional.** 🎉
