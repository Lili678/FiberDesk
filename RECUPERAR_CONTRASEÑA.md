# Funcionalidad de Recuperación de Contraseña

## ✅ Características Implementadas

### Frontend (Android)
- **Botón "¿Olvidaste tu contraseña?"** en la pantalla de login
- **Diálogo de solicitud** para ingresar correo electrónico
- **Diálogo de verificación** para ingresar código y nueva contraseña
- **Validaciones completas** de campos y formato
- **Interfaz Material Design** con TextInputLayouts

### Backend (API)
- **Endpoint POST /api/auth/recuperar-password** - Genera código de 6 dígitos
- **Endpoint POST /api/auth/restablecer-password** - Valida código y actualiza contraseña
- **Campos en modelo Usuario:**
  - `codigoRecuperacion` - Almacena el código generado
  - `codigoRecuperacionExpira` - Fecha de expiración (15 minutos)

## 🔄 Flujo de Recuperación

### 1. Solicitar Código
```
Usuario → Clic en "¿Olvidaste tu contraseña?" 
       → Ingresa correo
       → API genera código de 6 dígitos
       → Código se muestra en pantalla (desarrollo)
```

### 2. Restablecer Contraseña
```
Usuario → Ingresa código recibido
       → Ingresa nueva contraseña (mínimo 6 caracteres)
       → Confirma contraseña
       → API valida código y actualiza contraseña
```

## 📋 Validaciones Implementadas

### Frontend
- ✅ Correo no vacío
- ✅ Formato de correo válido
- ✅ Código de 6 dígitos
- ✅ Contraseña mínimo 6 caracteres
- ✅ Contraseñas coinciden

### Backend
- ✅ Usuario existe en BD
- ✅ Código válido y no expirado (15 minutos)
- ✅ Campos requeridos presentes
- ✅ Hash automático de nueva contraseña

## 🔐 Seguridad

1. **Código temporal**: Expira en 15 minutos
2. **Hash de contraseña**: bcrypt con salt de 10 rounds
3. **Limpieza de código**: Se elimina después del uso exitoso
4. **Validación de expiración**: Verifica timestamp en BD

## 🧪 Modo Desarrollo

**IMPORTANTE**: En desarrollo, el código se muestra en un Toast después de solicitarlo.

```kotlin
Toast.makeText(
    context, 
    "Código de verificación: $codigo\n(Guárdalo para el siguiente paso)", 
    Toast.LENGTH_LONG
).show()
```

### Para Producción
Reemplazar la respuesta del endpoint `recuperar-password` para:
1. Enviar código por correo electrónico
2. NO incluir el código en la respuesta JSON
3. Integrar servicio de email (SendGrid, AWS SES, etc.)

## 📱 Uso en la App

### Desde Login:
1. Toca "¿Olvidaste tu contraseña?"
2. Ingresa tu correo electrónico
3. Anota el código de 6 dígitos que aparece
4. Ingresa el código en la siguiente pantalla
5. Crea tu nueva contraseña (mínimo 6 caracteres)
6. Confirma la contraseña
7. ¡Listo! Inicia sesión con tu nueva contraseña

## 🔧 Archivos Modificados/Creados

### Frontend
```
✅ RecoverPasswordActivity.kt (actualizado)
✅ dialog_recuperar_contrasena.xml (nuevo)
✅ dialog_verificar_codigo.xml (nuevo)
✅ ApiService.kt (agregados endpoints)
```

### Backend
```
✅ controllers/auth.js (agregadas funciones)
   - exports.solicitarRecuperacion
   - exports.restablecerContrasena
✅ models/usuario_schema.js (agregados campos)
   - codigoRecuperacion
   - codigoRecuperacionExpira
✅ middleware/auth.js (agregadas rutas)
   - POST /recuperar-password
   - POST /restablecer-password
```

## 🎯 Endpoints API

### Solicitar Recuperación
```
POST /api/auth/recuperar-password
Body: { "correo": "usuario@ejemplo.com" }
Response: { 
  "success": true, 
  "mensaje": "Código de recuperación generado",
  "codigo": "123456" // Solo en desarrollo
}
```

### Restablecer Contraseña
```
POST /api/auth/restablecer-password
Body: { 
  "correo": "usuario@ejemplo.com",
  "codigo": "123456",
  "nuevaContrasena": "nuevaPassword123"
}
Response: { 
  "success": true, 
  "message": "Contraseña restablecida exitosamente"
}
```

## ⚠️ Notas Importantes

1. **El código expira en 15 minutos** después de generarse
2. **Solo un código válido** por usuario a la vez (se sobrescribe)
3. **El código se elimina** automáticamente después de usarlo
4. **En producción**, remover el campo `codigo` de la respuesta de recuperación
5. **Configurar servicio de email** antes de usar en producción

## ✨ Estado del Proyecto

- ✅ Frontend compilado exitosamente
- ✅ Backend funcionando correctamente
- ✅ Endpoints probados y operacionales
- ✅ Base de datos actualizada con nuevos campos
- ⚠️ Pendiente: Integración de servicio de email para producción
