# FiberDesk API - Documentación de Autenticación

## 🌐 Acceso Remoto (Sin Necesidad de Estar en la Misma Red)

### ⚡ Opción 1: ngrok (Temporal - Para Testing)

```bash
# 1. Instala ngrok
npm install -g ngrok

# 2. Ejecuta el script automático
npm run remote

# 3. Obtendrás una URL como: https://abc123.ngrok.io
# 4. Úsala en tu app Android
```

### 🚀 Opción 2: Railway (Permanente - Recomendado)

```bash
# 1. Prepara el proyecto
prepare-railway.bat

# 2. Sigue la guía rápida
```

**⏱️ 10 minutos** | **💰 Gratis** | **🔄 URL Permanente**

📖 **Guías:**
- [RAILWAY-QUICKSTART.md](RAILWAY-QUICKSTART.md) - Guía rápida (10 min)
- [DEPLOY-RAILWAY.md](DEPLOY-RAILWAY.md) - Guía detallada
- [README-REMOTE.md](README-REMOTE.md) - Todas las opciones

---

## Configuración del Backend

El backend está configurado con Node.js, Express, MongoDB y JWT para autenticación.

### Dependencias instaladas:
- express: Framework web
- mongoose: ODM para MongoDB
- bcryptjs: Encriptación de contraseñas
- jsonwebtoken: Autenticación JWT
- cors: Habilitar CORS
- dotenv: Variables de entorno

## Endpoints de Autenticación

### Base URL
```
http://localhost:3000/api
```

### 1. Registro de Usuario
**POST** `/auth/registro`

**Body (JSON):**
```json
{
  "correo": "usuario@ejemplo.com",
  "contraseña": "123456",
  "nombre": "Nombre Usuario" // Opcional
}
```

**Respuesta Exitosa (201):**
```json
{
  "success": true,
  "message": "Usuario registrado exitosamente",
  "data": {
    "_id": "user_id",
    "correo": "usuario@ejemplo.com",
    "nombre": "Nombre Usuario",
    "token": "jwt_token_aqui"
  }
}
```

**Errores:**
- 400: Correo ya registrado o datos faltantes
- 500: Error del servidor

---

### 2. Login de Usuario
**POST** `/auth/login`

**Body (JSON):**
```json
{
  "correo": "usuario@ejemplo.com",
  "contraseña": "123456"
}
```

**Respuesta Exitosa (200):**
```json
{
  "success": true,
  "message": "Login exitoso",
  "data": {
    "_id": "user_id",
    "correo": "usuario@ejemplo.com",
    "nombre": "Nombre Usuario",
    "token": "jwt_token_aqui"
  }
}
```

**Errores:**
- 401: Credenciales incorrectas o usuario desactivado
- 500: Error del servidor

---

### 3. Obtener Usuario Actual (Ruta Protegida)
**GET** `/auth/me`

**Headers:**
```
Authorization: Bearer {token}
```

**Respuesta Exitosa (200):**
```json
{
  "success": true,
  "data": {
    "_id": "user_id",
    "correo": "usuario@ejemplo.com",
    "nombre": "Nombre Usuario",
    "fechaRegistro": "2025-12-08T...",
    "activo": true
  }
}
```

**Errores:**
- 401: No autorizado (token inválido o no proporcionado)
- 500: Error del servidor

---

## Modelo de Usuario

```javascript
{
  correo: String (requerido, único, email válido),
  contraseña: String (requerido, min 6 caracteres, encriptado),
  nombre: String (opcional),
  fechaRegistro: Date (automático),
  activo: Boolean (default: true),
  timestamps: true (createdAt, updatedAt)
}
```

## Seguridad

- Las contraseñas se encriptan con bcrypt (10 salt rounds)
- Los tokens JWT expiran en 30 días
- La contraseña nunca se devuelve en las respuestas
- Rutas protegidas requieren token Bearer en el header Authorization

## Comandos para iniciar el servidor

```bash
# Modo desarrollo (con nodemon)
npm run dev

# Modo producción
npm start
```

## Variables de Entorno (.env)

```
MONGO_URI=tu_conexion_mongodb
PORT=3000
JWT_SECRET=tu_secret_key
```
