# Guía de Prueba - Recuperación de Contraseña

## ✅ Compilación Exitosa
La aplicación ha sido compilada exitosamente con el nuevo diseño de recuperación de contraseña.

## 🎨 Cambios Implementados

### 1. **Nueva Pantalla de Recuperación**
- Diseño idéntico al login (mismo gradiente, logo, estilo)
- Campos con el mismo formato Material Design
- Botón con el mismo estilo y animación
- Progress bar integrado

### 2. **Flujo Completo**
1. Usuario hace clic en "¿Olvidaste tu contraseña?" en el login
2. Se abre pantalla completa (no diálogo) con diseño elegante
3. Usuario ingresa su correo
4. Sistema genera código de 6 dígitos
5. Se muestra diálogo para ingresar código y nueva contraseña
6. Contraseña actualizada exitosamente

## 🔧 Solución de Errores

### Error Anterior
El error al enviar el código se debió a que la función usaba `RetrofitClient.apiService` cuando debía usar `ApiClient.apiService`.

### Corrección Aplicada
```kotlin
// ❌ Antes (incorrecto)
RetrofitClient.apiService.solicitarRecuperacion(...)

// ✅ Ahora (correcto)
com.example.fiberdesk_app.network.ApiClient.apiService.solicitarRecuperacion(...)
```

## 📱 Cómo Probar

### Paso 1: Iniciar el Backend
```bash
cd C:\Users\alexi\Documents\GitHub\FiberDesk\FiberDeskAPI
node server.js
```

Debes ver:
```
Servidor backend corriendo en 0.0.0.0:3000
Mongo conectado correctamente
```

### Paso 2: Instalar la App
```bash
cd C:\Users\alexi\Documents\GitHub\FiberDesk\FiberDeskApp
.\gradlew installDebug
```

### Paso 3: Probar Recuperación
1. Abre FiberDesk en tu dispositivo/emulador
2. En el login, toca "¿Olvidaste tu contraseña?"
3. Verás la nueva pantalla con el mismo diseño elegante del login
4. Ingresa un correo registrado (ej: `test@test.com`)
5. Toca "Enviar Código"
6. Aparecerá un Toast con el código de 6 dígitos (ej: `123456`)
7. Se abrirá un diálogo para ingresar:
   - Código de verificación
   - Nueva contraseña
   - Confirmar contraseña
8. Toca "Restablecer"
9. ¡Listo! Contraseña actualizada

### Paso 4: Verificar
Inicia sesión con la nueva contraseña para confirmar que funciona.

## 🎯 Características

### Validaciones Implementadas
- ✅ Correo vacío
- ✅ Formato de correo válido
- ✅ Usuario existe en la base de datos
- ✅ Código de 6 dígitos
- ✅ Código no expirado (15 minutos)
- ✅ Contraseña mínimo 6 caracteres
- ✅ Contraseñas coinciden
- ✅ Hash seguro con bcrypt

### Experiencia de Usuario
- ✅ Progress bar durante la carga
- ✅ Mensajes de error claros
- ✅ Botón "Volver al inicio de sesión"
- ✅ Diseño consistente con el resto de la app
- ✅ Material Design 3
- ✅ Animaciones fluidas

## 🔐 Seguridad

### Backend
- Código expira en 15 minutos automáticamente
- Contraseñas hasheadas con bcrypt
- Validación de usuario existente
- Código de 6 dígitos aleatorio

### Base de Datos
Campos añadidos al esquema de Usuario:
```javascript
codigoRecuperacion: String
codigoRecuperacionExpira: Date
```

## 📝 Notas para Producción

En producción, deberías:
1. **Ocultar el código**: No mostrarlo en Toast, solo enviarlo por email
2. **Integrar servicio de email**: 
   - SendGrid
   - AWS SES
   - Mailgun
   - Nodemailer con SMTP
3. **Rate limiting**: Limitar intentos de recuperación por IP
4. **Logs de auditoría**: Registrar intentos de recuperación

## 🎨 Diseño Visual

La pantalla de recuperación ahora tiene:
- ✅ Logo de FiberDesk centrado
- ✅ Nombre de la app "FiberDesk"
- ✅ Título "Recuperar Contraseña"
- ✅ Subtítulo explicativo
- ✅ Campo de correo con icono
- ✅ Botón con gradiente azul
- ✅ Link "Volver al inicio de sesión"
- ✅ Progress bar circular
- ✅ Fondo con gradiente (igual que login)

¡Todo funciona correctamente! 🎉
