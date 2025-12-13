# Credenciales de Acceso Iniciales

## 🔐 Usuario SUPER_ADMIN (Administrador del Sistema)

**Username:** `superadmin`  
**Password:** `super123`  
**Rol:** `SUPER_ADMIN`

### Permisos del SUPER_ADMIN:
- ✅ Ve todas las empresas del sistema
- ✅ Puede crear empresas
- ✅ Puede actualizar cualquier empresa
- ✅ Puede desactivar empresas
- ✅ Acceso total al sistema

---

## 👤 Usuarios de Ejemplo (por Empresa)

### Empresa A
**Username:** `userempresaa`  
**Password:** `pass123`  
**Rol:** `GERENTE`  
**Empresa:** `EMPRESA A SAC` (RUC: 20123456789)

### Empresa B
**Username:** `userempresab`  
**Password:** `pass123`  
**Rol:** `GERENTE`  
**Empresa:** `EMPRESA B SAC` (RUC: 20987654321)

---

## 📊 Comportamiento por Rol

| Rol | Empresas | Cuentas | Movimientos |
|-----|----------|---------|-------------|
| **SUPER_ADMIN** | Ve todas ✅ | Ver todas ⚠️ | Ver todos ⚠️ |
| **GERENTE** | Solo su empresa | Solo su empresa | Solo su empresa |
| **CAJERO** | Solo su empresa | Solo su empresa | Solo su empresa |

> **Nota:** El SUPER_ADMIN puede ver todas las empresas pero las cuentas/movimientos siguen filtrados por empresa. Para tener acceso completo de admin, considera agregar lógica adicional.

---

## 🔄 Flujo de Login

1. **POST** `/api/auth/login`
   ```json
   {
     "username": "superadmin",
     "password": "super123"
   }
   ```

2. Respuesta: Token JWT + datos del usuario
   ```json
   {
     "token": "eyJhbGciOiJIUzI1NiIs...",
     "usuarioId": 1,
     "username": "superadmin",
     "nombreCompleto": "Super Administrador del Sistema",
     "rol": "SUPER_ADMIN",
     "empresaId": 1,
     "empresaNombre": "EMPRESA SISTEMA"
   }
   ```

3. Usar token en header para acceso autenticado:
   ```
   Authorization: Bearer <token>
   ```

---

## 📝 Datos Iniciales Creados Automáticamente

En el arranque de la aplicación, `DataInitializer` crea:

1. **EMPRESA SISTEMA** - Para alojar al superadmin
2. **SUPER_ADMIN** - superadmin / super123
3. **EMPRESA A SAC** - RUC 20123456789
4. **EMPRESA B SAC** - RUC 20987654321
5. **Usuario Empresa A** - userempresaa / pass123
6. **Usuario Empresa B** - userempresab / pass123

> Los datos se crean solo si no existen (`ifPresentOrElse`)

---

## 🔒 Seguridad Implementada

- ✅ Filtrado de empresas por usuario (excepto SUPER_ADMIN)
- ✅ Filtrado de cuentas por empresa del usuario
- ✅ Filtrado de movimientos por empresa del usuario
- ✅ Validación en cada endpoint
- ✅ Contraseñas encriptadas con BCrypt
- ✅ JWT para autenticación
- ✅ CORS configurado para localhost:5173

---

## 🚀 Próximos Pasos

1. Agregar más usuarios a cada empresa
2. Crear cuentas Yape en cada empresa
3. Registrar movimientos de ejemplo
4. Probar flujo completo de login y acceso
