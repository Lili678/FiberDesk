# 🔄 Cambio Automático WiFi / Datos Móviles

## ✅ Configuración Completada

Tu app ahora cambia **automáticamente** entre servidores:

- **📶 WiFi** → `http://192.168.12.208:3000`
- **📱 Datos móviles** → `https://busy-drinks-kiss.loca.lt`

---

## 🔧 Cómo Actualizar la URL Remota

Cuando la URL de localtunnel/ngrok cambie, solo tienes que actualizar **1 archivo**:

### Archivo: `FiberDeskApp/app/build.gradle.kts`

```kotlin
buildTypes {
    debug {
        // ... otras configuraciones ...
        buildConfigField("String", "REMOTE_URL", "\"https://TU-NUEVA-URL.loca.lt\"")
        //                                          👆 CAMBIA AQUÍ
    }
}
```

### Pasos:

1. **Abre:** `FiberDeskApp/app/build.gradle.kts`
2. **Busca:** `REMOTE_URL`
3. **Cambia:** la URL entre comillas
4. **Sincroniza:** Click en "Sync Now" en Android Studio
5. **Compila:** Build → Make Project
6. **Instala:** La app en tu dispositivo

---

## 📱 Cómo Funciona

### Con WiFi:
```
🔍 Detecta WiFi
📶 Usa: http://192.168.12.208:3000
✅ Conectado a servidor local
```

### Con Datos Móviles:
```
🔍 Detecta datos móviles
📱 Usa: https://busy-drinks-kiss.loca.lt
✅ Conectado a servidor remoto
```

**¡El cambio es automático! No necesitas hacer nada en la app.**

---

## 🚀 Opciones de Servidores Remotos

### Opción 1: Localtunnel (Actual - Temporal)
```bash
# Terminal 1: Backend
cd FiberDeskAPI
npm run dev

# Terminal 2: Tunnel
lt --port 3000
# Copia la URL que te da
```

**⚠️ La URL cambia cada vez que reinicias**

### Opción 2: Railway (Recomendado - Permanente)

1. Ve a: https://railway.app
2. Conecta tu repositorio GitHub
3. Despliega `FiberDeskAPI`
4. Obtienes URL permanente: `https://fiberdesk.up.railway.app`
5. Actualízala en `build.gradle.kts`
6. **¡Listo! Ya no cambia nunca**

### Opción 3: ngrok (Alternativa)
```bash
ngrok http 3000
# Copia la URL
```

---

## 🎯 Ejemplo de Actualización

### Paso 1: Iniciar localtunnel
```bash
lt --port 3000
```

**Output:**
```
your url is: https://funny-cats-smile.loca.lt
```

### Paso 2: Actualizar build.gradle.kts
```kotlin
buildConfigField("String", "REMOTE_URL", "\"https://funny-cats-smile.loca.lt\"")
```

### Paso 3: Sincronizar y compilar
- Sync Now
- Build → Make Project
- Instalar APK

**¡Listo! La app usa la nueva URL automáticamente con datos móviles**

---

## 📊 Verificar en Logcat

Filtra por `NetworkConfig` para ver qué URL usa:

**Con WiFi:**
```
📶 WiFi → Usando IP local: 192.168.12.208
🔗 URL seleccionada: http://192.168.12.208:3000/api/
```

**Con Datos:**
```
📱 DATOS MÓVILES detectados → Usando servidor remoto
🔗 URL seleccionada: https://busy-drinks-kiss.loca.lt/api/
```

---

## 💡 Consejos

1. **Para desarrollo:** Usa localtunnel, actualiza la URL cuando cambie
2. **Para producción:** Despliega en Railway y actualiza una sola vez
3. **URL incorrecta:** Verifica que NO tenga `/api/` al final
4. **Formato correcto:** `https://tu-url.com` (sin barra final)

---

## ❓ Preguntas Frecuentes

**P: ¿Tengo que cambiar la URL cada vez que uso la app?**
R: No, solo cuando la URL del servidor remoto cambie.

**P: ¿Puedo usar una URL permanente?**
R: Sí, despliega en Railway y actualiza una sola vez.

**P: ¿La app detecta automáticamente el tipo de conexión?**
R: Sí, cambia sola entre WiFi y datos móviles.

**P: ¿Funciona sin internet?**
R: No, necesitas WiFi o datos móviles activos.

---

## 🎉 Resumen

✅ WiFi → Servidor local automático
✅ Datos → Servidor remoto automático  
✅ Solo actualizas URL en 1 archivo cuando cambie
✅ Sin configuración manual en la app
✅ Cambio instantáneo al cambiar de red

**¡Tu app está lista para funcionar en cualquier lugar!** 🚀
