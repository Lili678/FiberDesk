# Configuración de Google Maps API Key

## El mapa no se muestra (pantalla gris)

Esto ocurre porque necesitas configurar una API Key válida de Google Cloud.

## Pasos para obtener tu API Key:

### 1. Ir a Google Cloud Console
- Abre: https://console.cloud.google.com/

### 2. Crear o seleccionar un proyecto
- Si no tienes proyecto, crea uno nuevo
- Dale un nombre como "FiberDesk"

### 3. Habilitar las APIs necesarias
Ve a "APIs & Services" → "Library" y habilita:
- **Maps SDK for Android** (OBLIGATORIO)
- **Geocoding API** (Para convertir direcciones en coordenadas)
- **Places API** (Opcional, para autocompletar direcciones)

### 4. Crear credenciales
1. Ve a "APIs & Services" → "Credentials"
2. Click en "+ CREATE CREDENTIALS"
3. Selecciona "API Key"
4. Se creará tu API Key (algo como: `AIzaSyBxXxXxXxXxXxXxXxXxXxXxXxXxXxXxXxX`)

### 5. (OPCIONAL) Restringir la API Key
Para mayor seguridad:
1. Click en tu API Key recién creada
2. En "Application restrictions" selecciona "Android apps"
3. Click "ADD AN ITEM"
4. Ingresa:
   - Package name: `com.example.fiberdesk_app`
   - SHA-1: Obtén tu SHA-1 con el comando:
     ```
     ./gradlew signingReport
     ```
     (Busca la línea que dice SHA1)

### 6. Configurar en AndroidManifest.xml

Abre el archivo:
```
FiberDeskApp/app/src/main/AndroidManifest.xml
```

Busca la línea que dice:
```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="TU_CLAVE_API_AQUI" />
```

Y reemplaza `TU_CLAVE_API_AQUI` con tu API Key real:
```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="AIzaSyBxXxXxXxXxXxXxXxXxXxXxXxXxXxXxXxX" />
```

### 7. Rebuild la app
1. En Android Studio: **Build → Clean Project**
2. Luego: **Build → Rebuild Project**
3. Instala nuevamente en tu dispositivo

## Verificar que funcionó

El mapa debería mostrar:
- ✅ Tiles del mapa (calles, edificios, etc.)
- ✅ Marcador rojo en el centro
- ✅ Controles de zoom funcionando
- ✅ Poder mover el mapa

## Solución de problemas

### El mapa sigue en gris
- Verifica que habilitaste **Maps SDK for Android** (no solo Maps JavaScript API)
- Espera 5-10 minutos después de crear la API Key (puede tardar en propagarse)
- Verifica que tienes conexión a internet
- Revisa el Logcat en Android Studio buscando errores de "Maps"

### Error de autenticación
- Verifica que copiaste la API Key completa (sin espacios)
- Si restringiste por SHA-1, verifica que sea el correcto
- Prueba sin restricciones primero

### Error de facturación
Google Maps requiere una cuenta de facturación (aunque tiene $200 USD gratis al mes):
1. Ve a "Billing" en Google Cloud Console
2. Configura una cuenta de facturación
3. Vincula tu proyecto a esa cuenta

## Alternativa temporal (para desarrollo)

Si solo quieres probar sin configurar Google Maps, puedes:

1. Usar solo el botón "📡 Usar mi ubicación actual" (GPS)
2. Ingresar coordenadas manualmente editando el código para hacer visibles los campos
3. Usar una dirección completa (se geocodifica automáticamente)

## Costos

Google Maps es GRATIS para uso moderado:
- $200 USD de crédito mensual gratis
- Hasta ~28,000 cargas de mapa al mes sin costo
- Después de eso: $7 USD por cada 1,000 cargas adicionales

Para una app pequeña/mediana, nunca pagarás nada.
