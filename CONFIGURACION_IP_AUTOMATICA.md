# Configuración Automática del Servidor - FiberDesk

## 🎉 Nuevas Funcionalidades

Se ha implementado un sistema completo de **detección automática de IP** que permite que la aplicación se conecte al servidor sin importar a qué red estés conectado.

## ✨ Características Implementadas

### 1. **Detección Automática de Servidor**
   - La app detecta automáticamente la IP del servidor en la red local
   - Escanea la red WiFi para encontrar el servidor al iniciar la app
   - No necesitas configurar manualmente la IP cada vez que cambies de red

### 2. **Pantalla de Configuración**
   - Nueva pantalla de configuración del servidor accesible desde el login
   - Botón de configuración (⚙️) en la esquina superior derecha del login
   - Permite activar/desactivar la detección automática
   - Opción para configurar manualmente la IP si lo prefieres

### 3. **Funciones de Prueba**
   - Botón "Detectar Servidor" para buscar manualmente el servidor en la red
   - Botón "Probar Conexión" para verificar que la conexión funcione
   - Mensajes informativos sobre el estado de la conexión

### 4. **Persistencia de Configuración**
   - La app guarda la configuración en SharedPreferences
   - Recuerda la última IP que funcionó correctamente
   - No necesitas configurar cada vez que abras la app

## 📱 Cómo Usar

### Opción 1: Detección Automática (Recomendada)

1. **Abrir la app** en tu dispositivo móvil
2. En la pantalla de login, **tocar el ícono de configuración (⚙️)** en la esquina superior derecha
3. **Mantener activado** el switch "Detectar servidor automáticamente"
4. **Tocar "Detectar Servidor"**
5. La app escaneará la red y encontrará automáticamente el servidor
6. **Tocar "Probar Conexión"** para verificar
7. **Tocar "Guardar Configuración"**
8. ¡Listo! Ahora puedes usar la app

### Opción 2: Configuración Manual

1. **Abrir la app** en tu dispositivo móvil
2. En la pantalla de login, **tocar el ícono de configuración (⚙️)**
3. **Desactivar** el switch "Detectar servidor automáticamente"
4. **Ingresar manualmente** la IP de tu computadora
5. **Tocar "Probar Conexión"** para verificar
6. **Tocar "Guardar Configuración"**

## 🔧 Cómo Obtener la IP de tu Computadora

### Windows (PowerShell):
```powershell
ipconfig | Select-String "IPv4"
```

### Mac/Linux (Terminal):
```bash
ifconfig | grep "inet "
```

### Windows (GUI):
1. Abrir Configuración → Red e Internet → WiFi
2. Click en la red conectada
3. Buscar "Dirección IPv4"

## 📋 Requisitos

- Tanto el dispositivo móvil como la computadora deben estar conectados a la **misma red WiFi**
- El servidor debe estar ejecutándose en el puerto **3000** (configurable)
- El dispositivo móvil debe tener **permisos de WiFi** habilitados

## 🚀 Ventajas del Nuevo Sistema

✅ **Sin cables USB necesarios** - Solo necesitas WiFi  
✅ **Funciona en cualquier red** - Casa, oficina, universidad, etc.  
✅ **Configuración automática** - La app encuentra el servidor por ti  
✅ **Fácil de usar** - Interfaz simple e intuitiva  
✅ **Persistente** - Guarda la configuración automáticamente  
✅ **Pruebas integradas** - Verifica la conexión antes de usar  

## 🐛 Solución de Problemas

### "No se pudo detectar el servidor"

**Causas comunes:**
- El servidor no está ejecutándose
- Los dispositivos están en redes WiFi diferentes
- El firewall está bloqueando la conexión

**Soluciones:**
1. Verificar que el servidor esté corriendo (`node server.js` en FiberDeskAPI)
2. Confirmar que ambos dispositivos están en la misma red WiFi
3. Verificar la IP de tu computadora y configurarla manualmente
4. Desactivar temporalmente el firewall de Windows para pruebas

### "Error de conexión"

**Soluciones:**
1. Usar la pantalla de configuración para detectar el servidor nuevamente
2. Verificar que el puerto 3000 esté libre en tu computadora
3. Probar con configuración manual usando la IP correcta
4. Reiniciar el servidor backend

### El servidor cambia de IP frecuentemente

**Soluciones:**
- Mantener activada la detección automática
- Asignar una IP estática en tu router para tu computadora
- Usar la función "Detectar Servidor" cuando la IP cambie

## 📝 Notas Técnicas

### Archivos Creados:
- `NetworkPreferences.kt` - Gestión de configuración
- `ServerDetector.kt` - Detección automática de servidor
- `ServerConfigActivity.kt` - Pantalla de configuración
- `FiberDeskApplication.kt` - Inicialización de la app
- `activity_server_config.xml` - Layout de configuración

### Archivos Modificados:
- `NetworkConfig.kt` - Soporte para IP dinámica
- `AndroidManifest.xml` - Permisos y nueva Activity
- `LoginActivity.kt` - Botón de configuración
- `activity_login.xml` - Botón de configuración en UI

## 🎯 Próximos Pasos

Para compilar y ejecutar la app:

```bash
cd FiberDeskApp
./gradlew clean build
./gradlew installDebug
```

O desde Android Studio:
1. Abrir el proyecto FiberDeskApp
2. Build → Rebuild Project
3. Run → Run 'app'

## 💡 Consejos de Uso

- **Primera vez**: Usar "Detectar Servidor" para encontrar automáticamente la IP
- **Cambio de red**: Volver a detectar el servidor desde la configuración
- **Problemas**: Usar "Probar Conexión" para diagnosticar
- **Estabilidad**: Mantener el servidor corriendo en tu computadora

---

¡La aplicación ahora es totalmente flexible y funciona en cualquier red WiFi! 🎉
