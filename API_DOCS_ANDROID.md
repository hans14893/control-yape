# 📱 Documentación API Control-Yape para Integración Android

**URL BASE:** `http://localhost:9090` (desarrollo) | `http://tu-servidor:9090` (producción)

---

## 🔐 1. AUTENTICACIÓN (JWT)

### 1.1 Login - Obtener Token
**Endpoint:** `POST /api/auth/login`

**Request Body:**
```json
{
  "username": "super",
  "password": "super123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "usuarioId": 1,
  "username": "super",
  "nombreCompleto": "Administrador Sistema",
  "rol": "SUPERADMIN",
  "empresaId": 1,
  "empresaNombre": "EMPRESA SISTEMA"
}
```

**Headers para solicitudes posteriores:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### 1.2 Datos a Guardar Localmente (SharedPreferences/EncryptedSharedPreferences)
```
- token: String (JWT válido)
- usuarioId: Long
- username: String
- nombreCompleto: String
- rol: String (SUPERADMIN | USUARIO)
- empresaId: Long
- empresaNombre: String
```

---

## 🏢 2. EMPRESAS

### 2.1 Listar Empresas
**Endpoint:** `GET /api/empresas`
**Autenticación:** Requerida (Bearer Token)
**Perfil:** SUPERADMIN ve todas | USUARIO ve solo su empresa

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "nombre": "EMPRESA SISTEMA",
    "ruc": "20123456789",
    "telefono": "987654321",
    "email": "contacto@empresa.com",
    "activo": true
  },
  {
    "id": 2,
    "nombre": "CALIFORNIA",
    "ruc": "20987654321",
    "telefono": "987654322",
    "email": "california@yape.com",
    "activo": true
  }
]
```

### 2.2 Obtener Empresa por ID
**Endpoint:** `GET /api/empresas/{id}`
**Autenticación:** Requerida
**Parámetros:** `id` = ID de la empresa

**Response (200 OK):**
```json
{
  "id": 1,
  "nombre": "EMPRESA SISTEMA",
  "ruc": "20123456789",
  "telefono": "987654321",
  "email": "contacto@empresa.com",
  "activo": true
}
```

### 2.3 Crear Empresa (Solo SUPERADMIN)
**Endpoint:** `POST /api/empresas`
**Autenticación:** Requerida (SUPERADMIN)

**Request Body:**
```json
{
  "nombre": "NUEVA EMPRESA",
  "ruc": "20111111111",
  "telefono": "987654323",
  "email": "nueva@empresa.com"
}
```

**Response (201 Created):**
```json
{
  "id": 3,
  "nombre": "NUEVA EMPRESA",
  "ruc": "20111111111",
  "telefono": "987654323",
  "email": "nueva@empresa.com",
  "activo": true
}
```

---

## 👥 3. USUARIOS

### 3.1 Listar Usuarios (Autenticado)
**Endpoint:** `GET /api/usuarios`
**Autenticación:** Requerida
**Perfil:** SUPERADMIN ve todos | USUARIO ve solo su empresa

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "username": "super",
    "nombreCompleto": "Administrador Sistema",
    "rol": "SUPERADMIN",
    "empresaId": 1,
    "empresaNombre": "EMPRESA SISTEMA",
    "activo": true
  },
  {
    "id": 2,
    "username": "vendedor1",
    "nombreCompleto": "Juan Pérez",
    "rol": "USUARIO",
    "empresaId": 1,
    "empresaNombre": "EMPRESA SISTEMA",
    "activo": true
  }
]
```

### 3.2 Crear Usuario
**Endpoint:** `POST /api/usuarios`
**Autenticación:** Requerida

**Request Body:**
```json
{
  "username": "nuevousuario",
  "password": "pass123456",
  "nombreCompleto": "Carlos López",
  "rol": "USUARIO"
}
```

**Response (201 Created):**
```json
{
  "id": 3,
  "username": "nuevousuario",
  "nombreCompleto": "Carlos López",
  "rol": "USUARIO",
  "empresaId": 1,
  "empresaNombre": "EMPRESA SISTEMA",
  "activo": true
}
```

---

## 💰 4. CUENTAS YAPE

### 4.1 Listar Cuentas por Empresa
**Endpoint:** `GET /api/yape-cuentas/empresa/{empresaId}`
**Autenticación:** Requerida
**Parámetros:** `empresaId` = ID de la empresa

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "numero": "987654321",
    "nombreTitular": "Juan Pérez",
    "saldoActual": 5000.50,
    "empresaId": 1,
    "empresaNombre": "EMPRESA SISTEMA",
    "activo": true
  },
  {
    "id": 2,
    "numero": "987654322",
    "nombreTitular": "María García",
    "saldoActual": 2500.00,
    "empresaId": 1,
    "empresaNombre": "EMPRESA SISTEMA",
    "activo": true
  }
]
```

### 4.2 Crear Cuenta Yape
**Endpoint:** `POST /api/yape-cuentas`
**Autenticación:** Requerida

**Request Body:**
```json
{
  "numero": "987654325",
  "nombreTitular": "Pedro Sánchez",
  "saldoActual": 1000.00
}
```

**Response (201 Created):**
```json
{
  "id": 3,
  "numero": "987654325",
  "nombreTitular": "Pedro Sánchez",
  "saldoActual": 1000.00,
  "empresaId": 1,
  "empresaNombre": "EMPRESA SISTEMA",
  "activo": true
}
```

### 4.3 Actualizar Cuenta Yape
**Endpoint:** `PUT /api/yape-cuentas/{id}`
**Autenticación:** Requerida

**Request Body:**
```json
{
  "numero": "987654325",
  "nombreTitular": "Pedro Sánchez",
  "saldoActual": 1500.00
}
```

**Response (200 OK):**
```json
{
  "id": 3,
  "numero": "987654325",
  "nombreTitular": "Pedro Sánchez",
  "saldoActual": 1500.00,
  "empresaId": 1,
  "empresaNombre": "EMPRESA SISTEMA",
  "activo": true
}
```

---

## 📊 5. MOVIMIENTOS (TRANSACCIONES)

### 5.1 Listar Movimientos de una Cuenta
**Endpoint:** `GET /api/yape-cuentas/{cuentaId}/movimientos`
**Autenticación:** Requerida
**Parámetros:** `cuentaId` = ID de la cuenta Yape

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "monto": 50.00,
    "celular": "987654321",
    "concepto": "Venta producto",
    "tipo": "ENTRADA",
    "estado": "COMPLETADO",
    "fechaMovimiento": "2025-12-13T14:30:00",
    "cuentaId": 1,
    "cuentaNumero": "987654321"
  },
  {
    "id": 2,
    "monto": 100.00,
    "celular": "999999999",
    "concepto": "Transferencia",
    "tipo": "SALIDA",
    "estado": "COMPLETADO",
    "fechaMovimiento": "2025-12-13T15:45:00",
    "cuentaId": 1,
    "cuentaNumero": "987654321"
  }
]
```

### 5.2 Registrar Nuevo Movimiento
**Endpoint:** `POST /api/yape-cuentas/{cuentaId}/movimientos`
**Autenticación:** Requerida

**Request Body:**
```json
{
  "monto": 75.50,
  "celular": "987654323",
  "concepto": "Pago de factura",
  "tipo": "SALIDA"
}
```

**Response (201 Created):**
```json
{
  "id": 3,
  "monto": 75.50,
  "celular": "987654323",
  "concepto": "Pago de factura",
  "tipo": "SALIDA",
  "estado": "COMPLETADO",
  "fechaMovimiento": "2025-12-13T16:20:00",
  "cuentaId": 1,
  "cuentaNumero": "987654321"
}
```

### 5.3 Listar Movimientos por Empresa
**Endpoint:** `GET /api/empresas/{empresaId}/movimientos`
**Autenticación:** Requerida
**Query Params (opcionales):**
  - `fechaInicio`: 2025-12-01T00:00:00
  - `fechaFin`: 2025-12-31T23:59:59

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "monto": 50.00,
    "celular": "987654321",
    "concepto": "Venta producto",
    "tipo": "ENTRADA",
    "estado": "COMPLETADO",
    "fechaMovimiento": "2025-12-13T14:30:00",
    "cuentaId": 1,
    "cuentaNumero": "987654321"
  }
]
```

---

## 📈 6. DASHBOARD

### 6.1 Obtener Totales de la Empresa
**Endpoint:** `GET /api/dashboard/empresa/{empresaId}/totales`
**Autenticación:** Requerida

**Response (200 OK):**
```json
{
  "totalEntrada": 15000.00,
  "totalSalida": 5000.00,
  "saldoNeto": 10000.00,
  "cantidadMovimientos": 42,
  "cantidadCuentas": 3
}
```

---

## 🔔 7. NOTIFICACIONES

### 7.1 Enviar Notificación de Pago
**Endpoint:** `POST /api/yape-movimientos/notificacion`
**Autenticación:** Requerida

**Request Body:**
```json
{
  "numero": "987654321",
  "monto": 50.00,
  "concepto": "Pago de servicios"
}
```

**Response (200 OK):**
```json
{
  "mensaje": "Notificación enviada exitosamente",
  "numero": "987654321",
  "monto": 50.00
}
```

---

## 📱 8. ESTRUCTURA PARA BASE DE DATOS LOCAL (Room)

### 8.1 Entidad Usuario (LocalUser)
```kotlin
@Entity(tableName = "usuarios")
data class LocalUser(
    @PrimaryKey val id: Long,
    val username: String,
    val nombreCompleto: String,
    val rol: String,
    val empresaId: Long,
    val empresaNombre: String,
    val token: String,
    val fechaLogin: Long
)
```

### 8.2 Entidad Empresa (LocalEmpresa)
```kotlin
@Entity(tableName = "empresas")
data class LocalEmpresa(
    @PrimaryKey val id: Long,
    val nombre: String,
    val ruc: String,
    val telefono: String,
    val email: String,
    val activo: Boolean
)
```

### 8.3 Entidad Cuenta Yape (LocalYapeCuenta)
```kotlin
@Entity(tableName = "yape_cuentas")
data class LocalYapeCuenta(
    @PrimaryKey val id: Long,
    val numero: String,
    val nombreTitular: String,
    val saldoActual: Double,
    val empresaId: Long,
    val activo: Boolean,
    val fechaSincronizacion: Long
)
```

### 8.4 Entidad Movimiento (LocalMovimiento)
```kotlin
@Entity(tableName = "movimientos")
data class LocalMovimiento(
    @PrimaryKey val id: Long,
    val monto: Double,
    val celular: String,
    val concepto: String,
    val tipo: String, // ENTRADA o SALIDA
    val estado: String,
    val fechaMovimiento: Long,
    val cuentaId: Long,
    val cuentaNumero: String,
    val sincronizado: Boolean = false
)
```

---

## 🛠 9. FLUJO DE INTEGRACIÓN RECOMENDADO

### Paso 1: Login
```
POST /api/auth/login
Guardar: token, usuarioId, empresaId en SharedPreferences
```

### Paso 2: Cargar Datos Iniciales
```
GET /api/empresas/{empresaId}
GET /api/yape-cuentas/empresa/{empresaId}
GET /api/yape-movimientos (últimos 30 días)
Guardar en Room Database
```

### Paso 3: Registrar Movimiento
```
POST /api/yape-cuentas/{cuentaId}/movimientos
Guardarlo localmente con sincronizado=false
Sincronizar cuando haya conexión
```

### Paso 4: Sincronización
```
Detectar cambios en BD local (sincronizado=false)
Enviar al servidor
Actualizar sincronizado=true
Actualizar saldos locales
```

---

## ⚠️ 10. MANEJO DE ERRORES

### Error de Autenticación (401)
```json
{
  "timestamp": "2025-12-13T16:20:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Credenciales inválidas",
  "path": "/api/auth/login"
}
```

### Error de Autorización (403)
```json
{
  "timestamp": "2025-12-13T16:20:00",
  "status": 403,
  "error": "Forbidden",
  "message": "No tiene permisos para acceder a esta empresa",
  "path": "/api/empresas/5"
}
```

### Error de Validación (400)
```json
{
  "timestamp": "2025-12-13T16:20:00",
  "status": 400,
  "error": "Bad Request",
  "message": "El monto debe ser mayor a 0",
  "path": "/api/yape-cuentas/1/movimientos"
}
```

---

## 🔒 11. SEGURIDAD

- **Autenticación:** JWT (Bearer Token)
- **Token Duration:** Indefinido hasta logout (ajustar según necesidad)
- **Encriptación Local:** EncryptedSharedPreferences para guardar token
- **CORS:** Habilitado para desarrollo
- **HTTPS:** Implementar en producción

---

## 📞 12. CREDENCIALES DE PRUEBA

```
Username: super
Password: super123
Rol: SUPERADMIN
Empresa: EMPRESA SISTEMA (id: 1)
```

Otros usuarios se crean mediante: `POST /api/usuarios`

---

## 🌐 13. ENDPOINTS RESUMIDOS

| Método | Endpoint | Autenticación | Descripción |
|--------|----------|---------------|-------------|
| POST | `/api/auth/login` | ❌ | Obtener token |
| GET | `/api/empresas` | ✅ | Listar empresas |
| GET | `/api/empresas/{id}` | ✅ | Obtener empresa |
| POST | `/api/empresas` | ✅ SUPERADMIN | Crear empresa |
| GET | `/api/usuarios` | ✅ | Listar usuarios |
| POST | `/api/usuarios` | ✅ | Crear usuario |
| GET | `/api/yape-cuentas/empresa/{id}` | ✅ | Listar cuentas |
| POST | `/api/yape-cuentas` | ✅ | Crear cuenta |
| PUT | `/api/yape-cuentas/{id}` | ✅ | Actualizar cuenta |
| GET | `/api/yape-cuentas/{id}/movimientos` | ✅ | Listar movimientos |
| POST | `/api/yape-cuentas/{id}/movimientos` | ✅ | Registrar movimiento |
| GET | `/api/dashboard/empresa/{id}/totales` | ✅ | Ver totales |
| POST | `/api/yape-movimientos/notificacion` | ✅ | Enviar notificación |

---

**Última actualización:** 13 de Diciembre de 2025  
**Versión API:** 1.0.0  
**Estado:** ✅ Producción Ready
