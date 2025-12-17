# 🚀 GUÍA COMPLETA: Configurar Datos Móviles sin Instalar Nada

## ✅ Opción 1: Usar Railway (RECOMENDADO - Permanente)

Esta es la **mejor opción** porque no necesitas instalar nada y la URL es permanente.

### Paso 1: Crear cuenta en Railway

1. Ve a: **https://railway.app**
2. Click en **"Login"** → **"Login with GitHub"**
3. Autoriza Railway en tu cuenta de GitHub

### Paso 2: Subir tu código a GitHub

**Si NO tienes tu código en GitHub:**

```powershell
# En la carpeta del proyecto
cd "C:\Users\artur\OneDrive\Desktop\proyecto FiberDesk\FiberDesk"
git add .
git commit -m "Preparado para Railway"
git branch -M main
git remote add origin https://github.com/TU_USUARIO/FiberDesk.git
git push -u origin main
```

**Si ya está en GitHub:** continúa al siguiente paso.

### Paso 3: Desplegar en Railway

1. En Railway, click **"New Project"**
2. Selecciona **"Deploy from GitHub repo"**
3. Busca y selecciona tu repositorio **FiberDesk**
4. Railway detectará automáticamente que es Node.js
5. Configura la **Root Directory**: `FiberDeskAPI`
6. Click **"Deploy"**

### Paso 4: Configurar Variables de Entorno

En Railway:
1. Click en tu proyecto → **"Variables"**
2. Agrega estas variables:

```
MONGO_URI=mongodb+srv://admin:admin@cluster0.y3q9qmd.mongodb.net/myDatabase?retryWrites=true&w=majority
PORT=3000
JWT_SECRET=10912214
```

### Paso 5: Obtener tu URL

1. Ve a **"Settings"** → **"Networking"**
2. Click **"Generate Domain"**
3. Railway te dará una URL como: `https://fiberdesk-production.up.railway.app`

### Paso 6: Configurar en tu App Android

1. Copia la URL de Railway
2. Abre FiberDesk en tu dispositivo
3. Ve a **Configuración del Servidor**
4. En **"📱 Servidor para Datos Móviles"**:
   - ✅ Activa el switch
   - Pega tu URL: `https://fiberdesk-production.up.railway.app`
5. Guarda

**¡LISTO! Ya funciona con datos móviles permanentemente** 🎉

---

## ⚡ Opción 2: Usar Localtunnel (Rápido - Sin registro)

Esta opción NO requiere instalación de ngrok ni cuenta.

### Paso 1: Instalar localtunnel (una sola vez)

```powershell
npm install -g localtunnel
```

### Paso 2: Iniciar tu backend

**Terminal 1:**
```powershell
cd FiberDeskAPI
npm run dev
```

### Paso 3: Exponer con localtunnel

**Terminal 2:**
```powershell
lt --port 3000
```

Verás:
```
your url is: https://random-name-123.loca.lt
```

### Paso 4: Configurar en la app

Copia esa URL y pégala en la app (sección Datos Móviles).

**⚠️ Nota:** La URL cambia cada vez que reinicias localtunnel.

---

## 🔧 Opción 3: Usar Render (Gratis - Permanente)

### Paso 1: Crear cuenta

1. Ve a: **https://render.com**
2. Click **"Get Started"** → **"Sign Up with GitHub"**

### Paso 2: Crear Web Service

1. Click **"New +"** → **"Web Service"**
2. Conecta tu repositorio de GitHub
3. Selecciona **FiberDesk**
4. Configura:
   - **Name**: `fiberdesk-api`
   - **Root Directory**: `FiberDeskAPI`
   - **Build Command**: `npm install`
   - **Start Command**: `npm start`
   - **Plan**: Free

### Paso 3: Agregar variables

En **"Environment"**, agrega:
```
MONGO_URI=mongodb+srv://admin:admin@cluster0.y3q9qmd.mongodb.net/myDatabase
PORT=3000
JWT_SECRET=10912214
```

### Paso 4: Deploy

Click **"Create Web Service"**

Render te dará una URL como: `https://fiberdesk-api.onrender.com`

---

## 📱 Configuración Final en la App

Para **cualquier opción** que elijas:

1. Abre FiberDesk
2. **⚙️ Configuración** → **Configuración del Servidor**
3. En **"📱 Servidor para Datos Móviles"**:
   - ✅ Activa: "Usar servidor remoto con datos móviles"
   - Pega tu URL (Railway, Localtunnel o Render)
   - Formato: `https://tu-url.com` (sin `/api/` al final)
4. **Guardar**
5. **Probar Conexión**

---

## 🎯 Comparación de Opciones

| Opción | Instalación | URL Permanente | Gratis | Tiempo Setup |
|--------|-------------|----------------|--------|--------------|
| **Railway** | ❌ No | ✅ Sí | ✅ Sí ($5/mes) | 5 min |
| **Localtunnel** | ⚠️ npm | ❌ No | ✅ Sí | 2 min |
| **Render** | ❌ No | ✅ Sí | ✅ Sí | 5 min |
| **ngrok** | ✅ Sí | ❌ No | ✅ Sí | 2 min |

**Recomendación:** Usa **Railway** para algo permanente o **Localtunnel** para pruebas rápidas.

---

## ❓ Preguntas Frecuentes

**P: ¿Cuál es la más fácil?**
R: Railway (no instalas nada, solo conectas GitHub)

**P: ¿Cuál es la más rápida para probar ahora?**
R: Localtunnel (solo un comando npm)

**P: ¿Necesito cambiar algo en el código?**
R: No, nada. Solo la URL en la app.

**P: ¿Puedo cambiar entre opciones?**
R: Sí, solo cambia la URL en la configuración de la app.

---

## 🚀 Mi Recomendación

1. **Para desarrollo/pruebas:** Usa **Localtunnel** ahora mismo
2. **Para producción:** Despliega en **Railway** después

**¿Cuál opción quieres que te ayude a configurar primero?**
