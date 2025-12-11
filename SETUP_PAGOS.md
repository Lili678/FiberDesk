# 📱 Configuración Completa del Módulo de Pagos - FiberDesk

## ✅ Backend (FiberDeskAPI)

### 1. Instalar Dependencias Faltantes
```bash
cd FiberDeskAPI
npm install uuid express-validator
```

### 2. Verificar que el servidor esté corriendo
```bash
npm start
```

El servidor debe estar en: `http://localhost:3000`

### 3. Endpoints Disponibles
- `GET /api/pagos` - Obtener todos los pagos
- `GET /api/pagos/:id` - Obtener un pago por ID
- `GET /api/pagos/usuario/:usuarioId` - Obtener pagos de un usuario
- `POST /api/pagos` - Crear nuevo pago
- `PUT /api/pagos/:id` - Actualizar pago
- `DELETE /api/pagos/:id` - Eliminar pago

---

## 📱 Frontend (FiberDeskApp)

### 1. Sincronizar Gradle
En Android Studio:
1. Click en **File** > **Sync Project with Gradle Files**
2. Espera a que termine la sincronización

### 2. Configurar la URL del API
Edita: `app/src/main/java/com/example/fiberdesk_app/utils/constants/constants.kt`

```kotlin
const val BASE_URL = "http://10.0.2.2:3000/"  // Para emulador
// const val BASE_URL = "http://TU_IP:3000/"   // Para dispositivo físico
```

**Para dispositivo físico:**
1. Obtén tu IP local:
   - Windows: `ipconfig` (busca IPv4)
   - Mac/Linux: `ifconfig` o `ip addr`
2. Reemplaza en la URL: `http://192.168.X.X:3000/`

### 3. Ejecutar la App
1. Conecta un dispositivo o inicia el emulador
2. Click en **Run** (botón verde de play)
3. Selecciona tu dispositivo

---

## 🔧 Solución de Problemas Comunes

### Error: "Unresolved reference: SerializedName"
**Solución:**
1. File > Invalidate Caches / Restart
2. Sync Project with Gradle Files
3. Rebuild Project (Build > Rebuild Project)

### Error de Conexión al API
**Solución:**
1. Verifica que el backend esté corriendo
2. Verifica la URL en `constants.kt`
3. Para emulador usa: `10.0.2.2:3000`
4. Para dispositivo físico, usa tu IP local
5. Asegúrate de que dispositivo y PC estén en la misma red WiFi

### Error: "Network Security Configuration"
**Ya configurado en AndroidManifest.xml:**
```xml
android:usesCleartextTraffic="true"
```

---

## 📊 Estructura de Datos

### Modelo de Pago (Schema MongoDB)
```javascript
{
  id: String,
  usuarioId: ObjectId,
  monto: Number,
  abono: Number,
  metodoPago: String, // efectivo, transferencia, tarjeta, cheque
  fechaPago: Date,
  descripcion: String,
  estado: String, // pendiente, pagado, parcial
  createdAt: Date,
  updatedAt: Date
}
```

---

## 🎯 Funcionalidades Implementadas

### ✅ Backend
- ✅ CRUD completo de pagos
- ✅ Validaciones de datos
- ✅ Cálculo automático de estado según abono/monto
- ✅ Conexión a MongoDB Atlas
- ✅ Middleware de validación

### ✅ Frontend
- ✅ Lista de pagos con RecyclerView
- ✅ Crear nuevo pago
- ✅ Editar pago existente
- ✅ Eliminar pago
- ✅ Filtrar por usuario
- ✅ Estadísticas en tiempo real
- ✅ UI Material Design
- ✅ Manejo de estados (loading, error, success)

---

## 🚀 Próximos Pasos

1. **Crear modelo de Usuario** en el backend
2. **Implementar autenticación** (Login/Register)
3. **Agregar módulos de Inventario y Tickets**
4. **Implementar navegación** entre módulos
5. **Agregar búsqueda y filtros** avanzados
6. **Implementar reportes** y gráficos

---

## 📝 Notas Importantes

- El backend guarda automáticamente en MongoDB Atlas
- Los IDs se generan con UUID v4
- Las fechas están en formato ISO 8601
- El estado se calcula automáticamente:
  - `pendiente`: abono = 0
  - `parcial`: 0 < abono < monto
  - `pagado`: abono >= monto
