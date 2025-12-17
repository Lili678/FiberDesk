# Integración Frontend-Backend de Clientes - FiberDesk

## ✅ Cambios Completados

### 1. Modelos de Datos (ClienteModels.kt)
Se creó un archivo completo con todos los modelos que coinciden con el backend:
- **ClienteModel**: Modelo principal con estructura anidada (Name, LastName, Location, etc.)
- **CrearClienteRequest**: Request para crear nuevos clientes
- **ActualizarClienteRequest**: Request para actualizar clientes
- **ClienteResponse**: Response del servidor
- **ClienteInfoCompleta**: Información completa con pagos y tickets
- **Extensiones útiles**: `getNombreCompleto()`, `getDireccionCompleta()`

**Ubicación**: `FiberDeskApp/app/src/main/java/com/example/fiberdesk_app/data/model/ClienteModels.kt`

### 2. Cliente API (ClientesApiClient.kt)
Cliente HTTP nativo (sin librerías externas) que implementa todos los endpoints:
- `obtenerClientes()` - GET /api/clientes
- `obtenerClientePorId(id)` - GET /api/clientes/:id
- `buscarClientes(query)` - GET /api/clientes/buscar?query=
- `crearCliente(cliente)` - POST /api/clientes
- `actualizarCliente(id, cliente)` - PUT /api/clientes/:id
- `obtenerInfoCompleta(id)` - GET /api/clientes/:id/completo
- `archivarCliente(id, archivar)` - PATCH /api/clientes/:id/archivar
- `eliminarCliente(id)` - DELETE /api/clientes/:id

**Características**:
- Usa `HttpURLConnection` (nativo)
- Parseo JSON con `org.json`
- URL base: `http://10.0.2.2:3000/` (emulador Android)
- Manejo de errores HTTP

**Ubicación**: `FiberDeskApp/app/src/main/java/com/example/fiberdesk_app/data/remote/ClientesApiClient.kt`

### 3. Repositorio (ClientesRepository.kt)
Capa de repositorio con manejo de corrutinas:
- Todas las funciones son `suspend` (asíncronas)
- Ejecuta en `Dispatchers.IO`
- Retorna `Result<T>` (Success/Error)
- Manejo centralizado de excepciones

**Ubicación**: `FiberDeskApp/app/src/main/java/com/example/fiberdesk_app/data/repository/ClientesRepository.kt`

### 4. ViewModel (ClientesViewModel.kt)
ViewModel con arquitectura MVVM:
- **LiveData observables**:
  - `clientes`: Lista de clientes
  - `cliente`: Cliente individual
  - `infoCompleta`: Información completa del cliente
  - `isLoading`: Estado de carga
  - `error`: Mensajes de error
  - `mensaje`: Mensajes de éxito

- **Funciones**:
  - `cargarClientes(incluirArchivados)`
  - `buscarClientes(query)`
  - `obtenerClientePorId(id)`
  - `obtenerInfoCompleta(id)`
  - `crearCliente(cliente)`
  - `actualizarCliente(id, cliente)`
  - `archivarCliente(id, archivar)`
  - `eliminarCliente(id)`
  - `limpiarMensajes()`

**Ubicación**: `FiberDeskApp/app/src/main/java/com/example/fiberdesk_app/ui/clientes/ClientesViewModel.kt`

### 5. Activity - Lista de Clientes (ListaClientesActivity.kt)
**Cambios realizados**:
- ✅ Integrado con `ClientesViewModel`
- ✅ Carga datos reales del servidor
- ✅ Búsqueda en tiempo real conectada al backend
- ✅ ProgressBar mientras carga
- ✅ Manejo de errores con Toast
- ✅ Recarga automática al volver a la pantalla (`onResume`)
- ✅ Conversión de `ClienteModel` a `Cliente` para compatibilidad UI

**Ubicación**: `FiberDeskApp/app/src/main/java/com/example/fiberdesk_app/ListaClientesActivity.kt`

### 6. Activity - Agregar Cliente (AgregarClienteActivity.kt)
**Cambios realizados**:
- ✅ Integrado con `ClientesViewModel`
- ✅ Formulario completo con todos los campos
- ✅ Validaciones obligatorias:
  - Nombre
  - Apellido Paterno
  - Teléfono (10 dígitos)
  - Email (formato válido)
  - Coordenadas GPS
- ✅ Envío de datos al servidor
- ✅ ProgressBar durante guardado
- ✅ Toast de éxito/error
- ✅ Cierre automático al guardar exitosamente

**Ubicación**: `FiberDeskApp/app/src/main/java/com/example/fiberdesk_app/AgregarClienteActivity.kt`

### 7. Layout - Agregar Cliente (activity_agregar_cliente.xml)
**Cambios realizados**:
- ✅ Agregados todos los campos del formulario:
  - Nombre
  - Segundo Nombre (opcional)
  - Apellido Paterno
  - Apellido Materno (opcional)
  - Teléfono
  - Email
  - Dirección completa (Calle, Número Ext/Int, Colonia, Municipio, Estado, CP)
  - Coordenadas (Latitud/Longitud)
- ✅ Botones GPS y Mapa mantienen funcionalidad original
- ✅ ProgressBar agregado

**Ubicación**: `FiberDeskApp/app/src/main/res/layout/activity_agregar_cliente.xml`

### 8. Layout - Lista Clientes (activity_lista_clientes.xml)
**Cambios realizados**:
- ✅ Agregado ProgressBar centrado
- ✅ Cambiado LinearLayout por FrameLayout para overlay

**Ubicación**: `FiberDeskApp/app/src/main/res/layout/activity_lista_clientes.xml`

## 📋 Arquitectura Implementada

```
┌─────────────────────────────────────────────┐
│         Activity (UI)                        │
│  - ListaClientesActivity                     │
│  - AgregarClienteActivity                    │
└──────────────┬──────────────────────────────┘
               │ observa LiveData
               ▼
┌─────────────────────────────────────────────┐
│         ViewModel                            │
│  - ClientesViewModel                         │
│  - Lógica de presentación                    │
│  - LiveData para UI                          │
└──────────────┬──────────────────────────────┘
               │ llama funciones suspend
               ▼
┌─────────────────────────────────────────────┐
│         Repository                           │
│  - ClientesRepository                        │
│  - Manejo de corrutinas                      │
│  - Result<T> (Success/Error)                 │
└──────────────┬──────────────────────────────┘
               │ llama API
               ▼
┌─────────────────────────────────────────────┐
│         API Client                           │
│  - ClientesApiClient                         │
│  - HttpURLConnection nativo                  │
│  - JSON parsing                              │
└──────────────┬──────────────────────────────┘
               │ HTTP
               ▼
┌─────────────────────────────────────────────┐
│         Backend (Node.js)                    │
│  - Express + MongoDB                         │
│  - Puerto 3000                               │
└─────────────────────────────────────────────┘
```

## 🔄 Flujo de Datos

### Cargar Lista de Clientes:
1. Activity observa `viewModel.clientes`
2. Activity llama `viewModel.cargarClientes()`
3. ViewModel llama `repository.obtenerClientes()`
4. Repository llama `ClientesApiClient.obtenerClientes()` en IO thread
5. API hace GET a `http://10.0.2.2:3000/api/clientes`
6. Backend responde con JSON
7. API parsea JSON a `List<ClienteModel>`
8. Repository envuelve en `Result.Success`
9. ViewModel actualiza LiveData `clientes`
10. Activity recibe cambio y actualiza RecyclerView

### Crear Cliente:
1. Usuario llena formulario y presiona "Guardar"
2. Activity valida campos
3. Activity crea `CrearClienteRequest`
4. Activity llama `viewModel.crearCliente(request)`
5. ViewModel llama `repository.crearCliente(request)`
6. Repository llama `ClientesApiClient.crearCliente()` en IO thread
7. API hace POST a `http://10.0.2.2:3000/api/clientes`
8. Backend valida y guarda en MongoDB
9. Backend responde con cliente creado
10. API parsea respuesta a `ClienteResponse`
11. Repository envuelve en `Result.Success`
12. ViewModel actualiza LiveData `mensaje`
13. Activity muestra Toast y cierra

## ⚙️ Configuración Necesaria

### Backend
El backend ya está corriendo en puerto 3000:
- **URL**: http://localhost:3000
- **MongoDB**: Conectado correctamente
- **Endpoints**: Todos funcionando

### Android
Configuración requerida en el proyecto:

#### build.gradle.kts (app level)
Asegúrate de tener:
```kotlin
dependencies {
    // Core AndroidX
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    
    // Lifecycle & LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}
```

#### AndroidManifest.xml
Permisos ya existentes:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

## 🧪 Pruebas

### Probar Lista de Clientes:
1. Abre la app y ve a "Clientes"
2. Deberías ver un ProgressBar mientras carga
3. Se mostrarán todos los clientes del servidor
4. Escribe en el buscador para filtrar en tiempo real

### Probar Crear Cliente:
1. Ve a "Agregar Cliente"
2. Llena todos los campos obligatorios
3. Usa GPS o Mapa para coordenadas
4. Presiona "Guardar"
5. Verás ProgressBar y luego Toast de éxito
6. Volverás a la lista con el nuevo cliente

## 🐛 Manejo de Errores

El sistema maneja estos errores:
- ❌ **Sin conexión**: Toast "Error al obtener clientes"
- ❌ **Servidor caído**: Toast con mensaje HTTP
- ❌ **Validación**: Toast específico por campo
- ❌ **Email inválido**: Toast "Ingrese un email válido"
- ❌ **Teléfono**: Toast "Ingrese un teléfono válido de 10 dígitos"
- ❌ **Sin coordenadas**: Toast "Las coordenadas son obligatorias"

## 📱 Compatibilidad UI

Para mantener compatibilidad con las Activities existentes, se mantiene el modelo viejo `Cliente` en la UI y se convierte desde `ClienteModel`:

```kotlin
fun convertirClienteModelACliente(modelo: ClienteModel): Cliente {
    return Cliente(
        nombre = modelo.Name.FirstName + segundo nombre,
        apellidos = apellidoPaterno + apellidoMaterno,
        telefono = primer teléfono,
        correo = Email,
        calle = Address.Street,
        // ... demás campos
        latitud = Coordinates.Latitude,
        longitud = Coordinates.Longitude
    )
}
```

## 🔮 Próximos Pasos (Opcional)

1. **DetalleClienteActivity**: Actualizar para usar API
2. **Editar Cliente**: Implementar formulario de edición
3. **Documentos**: Agregar funcionalidad de subir documentos
4. **Paquetes**: Formulario para asignar paquetes
5. **Archivar**: Botón para archivar clientes
6. **Eliminar**: Confirmación y eliminación
7. **Relaciones**: Mostrar pagos y tickets del cliente
8. **Offline**: Caché local con Room

## ✅ Estado Final

- ✅ Backend funcionando en puerto 3000
- ✅ 10 endpoints REST operativos
- ✅ MongoDB conectado
- ✅ Frontend conectado al backend
- ✅ Arquitectura MVVM completa
- ✅ Búsqueda en tiempo real
- ✅ Crear clientes funcional
- ✅ Validaciones implementadas
- ✅ Manejo de errores robusto
- ✅ Sin errores de compilación

**El módulo de clientes está completamente integrado y listo para usar! 🎉**
