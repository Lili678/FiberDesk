# 🚀 GUÍA RÁPIDA: Desplegar en Railway

## ⏱️ Tiempo estimado: 10 minutos

### 📋 Pre-requisitos
- [ ] Cuenta de GitHub
- [ ] Código en GitHub
- [ ] Cuenta de Railway (crear en railway.app)

---

## 🎯 Pasos Rápidos

### 1️⃣ Prepara tu código (2 min)

```bash
cd FiberDeskAPI
prepare-railway.bat
```

### 2️⃣ Sube a GitHub (3 min)

```bash
git remote add origin https://github.com/TU-USUARIO/FiberDesk.git
git branch -M main
git push -u origin main
```

### 3️⃣ Despliega en Railway (5 min)

1. Ve a: **https://railway.app**
2. Click: **"Start a New Project"**
3. Click: **"Deploy from GitHub repo"**
4. Selecciona: **FiberDesk** (tu repositorio)
5. Railway desplegará automáticamente

### 4️⃣ Configura Variables de Entorno

En Railway, pestaña **Variables**, agrega:

```
MONGO_URI=mongodb+srv://admin:admin@cluster0.y3q9qmd.mongodb.net/myDatabase?retryWrites=true&w=majority
JWT_SECRET=10912214
PORT=3000
```

### 5️⃣ Genera Dominio Público

1. Pestaña **Settings**
2. **Networking** → **Generate Domain**
3. Copia la URL: `tu-app.up.railway.app`

### 6️⃣ Prueba tu API

Abre en navegador:
```
https://tu-app.up.railway.app/api/health
```

### 7️⃣ Configura en App Android

En tu app:
- Servidor: `tu-app.up.railway.app`
- Puerto: `443`

---

## ✅ ¡Listo!

Tu API ahora está en la nube de forma permanente.

**URL permanente:** https://tu-app.up.railway.app  
**Actualizaciones:** Automáticas al hacer push a GitHub

---

📖 **Guía detallada:** [DEPLOY-RAILWAY.md](DEPLOY-RAILWAY.md)
