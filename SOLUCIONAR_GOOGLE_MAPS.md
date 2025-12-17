# 🗺️ Solución para Google Maps - Pantalla en Gris/Blanco

## ⚠️ PROBLEMA COMÚN
Si el mapa aparece gris o blanco, significa que la API Key no está funcionando correctamente.

## 🔑 API KEY ACTUAL
```
AIzaSyC2wz1VFJRewBf8YdHUFS4uRh4MfFmxd10
```

## ✅ PASOS PARA VERIFICAR/CREAR NUEVA API KEY

### 1. Ir a Google Cloud Console
```
https://console.cloud.google.com/
```

### 2. Verificar la API Key Actual
1. Ve a **APIs y servicios** → **Credenciales**
2. Busca la API Key: `AIzaSyC2wz1VFJRewBf8YdHUFS4uRh4MfFmxd10`
3. Haz clic en ella para ver sus detalles

### 3. Verificar APIs Habilitadas
La API Key DEBE tener habilitadas estas APIs:

✅ **Maps SDK for Android** (OBLIGATORIO)
✅ **Geocoding API** (para convertir direcciones a coordenadas)
✅ **Places API** (opcional, para búsqueda de lugares)

**Cómo habilitar:**
1. Ve a **APIs y servicios** → **Biblioteca**
2. Busca "Maps SDK for Android"
3. Haz clic en **HABILITAR**
4. Repite para "Geocoding API"

### 4. Verificar Restricciones de la API Key

**IMPORTANTE:** La API Key debe estar configurada para tu aplicación.

#### Opción A: Sin Restricciones (Para Testing - NO RECOMENDADO para producción)
1. En la API Key, ve a **Restricciones de aplicación**
2. Selecciona **Ninguna**
3. Guarda

#### Opción B: Con Restricciones (RECOMENDADO)
1. En la API Key, ve a **Restricciones de aplicación**
2. Selecciona **Aplicaciones de Android**
3. Agrega:
   - **Nombre del paquete:** `com.example.fiberdesk_app`
   - **Huella digital SHA-1:** (ver sección siguiente)

### 5. Obtener SHA-1 de la Aplicación

#### Para Debug (Emulador/Testing):
```bash
# En PowerShell desde la carpeta FiberDeskApp:
cd FiberDeskApp
.\gradlew signingReport
```

Busca en el output:
```
Variant: debug
Config: debug
Store: C:\Users\TU_USUARIO\.android\debug.keystore
Alias: AndroidDebugKey
SHA1: XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX
```

Copia ese SHA-1 y agrégalo a las restricciones de la API Key.

### 6. Crear Nueva API Key (Si la actual no funciona)

1. Ve a **APIs y servicios** → **Credenciales**
2. Haz clic en **+ CREAR CREDENCIALES** → **Clave de API**
3. Se creará una nueva API Key
4. Haz clic en **RESTRINGIR CLAVE**
5. Configura:
   - **Nombre:** `FiberDesk Android Maps Key`
   - **Restricciones de aplicación:** Aplicaciones de Android
   - **Agregar nombre de paquete:** `com.example.fiberdesk_app`
   - **Agregar huella digital SHA-1:** (usa el comando de arriba)
6. En **Restricciones de API**, selecciona:
   - ✅ Maps SDK for Android
   - ✅ Geocoding API
7. Haz clic en **GUARDAR**
8. **Copia la nueva API Key**
9. Reemplázala en `AndroidManifest.xml` línea 141

## 🔍 DIAGNÓSTICO

### Ver Logs en Android Studio
1. Abre **Logcat** en Android Studio
2. Filtra por **MapaPicker**
3. Deberías ver:
   ```
   ✅ onMapReady llamado - El mapa está listo
   ✅ Mapa centrado en CDMX
   ```

### Si ves errores como:
```
AUTHORIZATION_FAILURE
API key not valid
```
= La API Key no está correctamente configurada

```
Map failed to load
```
= No hay conexión o la API no está habilitada

## 📝 DESPUÉS DE CREAR/ACTUALIZAR LA API KEY

1. **Limpia y reconstruye el proyecto:**
   ```bash
   cd FiberDeskApp
   .\gradlew clean
   .\gradlew build
   ```

2. **Desinstala la app del dispositivo/emulador**

3. **Instala la app nuevamente**

4. **Espera 5-10 minutos** (a veces Google tarda en activar la API Key)

## ⚡ SOLUCIÓN RÁPIDA (TESTING)

Si necesitas que funcione YA para testing:

1. Ve a tu API Key en Google Cloud Console
2. **Restricciones de aplicación:** Selecciona **Ninguna**
3. **Restricciones de API:** Deja vacío o selecciona todas las APIs de Maps
4. Guarda
5. Espera 2-3 minutos
6. Prueba la app

⚠️ **IMPORTANTE:** Esto deja tu API Key sin protección. Cualquiera puede usarla.
Para producción, SIEMPRE usa restricciones.

## 🆘 ÚLTIMA OPCIÓN

Si nada funciona, crea un proyecto NUEVO en Google Cloud Console:

1. Crea nuevo proyecto: "FiberDesk Maps"
2. Habilita **Maps SDK for Android**
3. Crea nueva API Key
4. Déjala sin restricciones temporalmente
5. Úsala en tu app
6. Una vez que funcione, agrega restricciones

## 📞 CONTACTO GOOGLE CLOUD

Si tienes problemas, verifica:
- ✅ Cuenta de Google Cloud está activa
- ✅ Tienes créditos/facturación habilitada (Google Maps requiere cuenta de facturación)
- ✅ No has excedido las cuotas gratuitas

**Cuota gratuita de Maps SDK for Android:** 
- 28,000 cargas de mapa por mes GRATIS
