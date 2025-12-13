# Test - Verificar SUPER_ADMIN ve todas las empresas

## 1️⃣ LOGIN como SUPER_ADMIN
```bash
curl -X POST http://localhost:9090/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "superadmin",
    "password": "super123"
  }'
```

**Respuesta esperada:**
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

---

## 2️⃣ LISTAR TODAS LAS EMPRESAS (como SUPER_ADMIN)
```bash
curl -X GET http://localhost:9090/api/empresas \
  -H "Authorization: Bearer <TOKEN_DEL_PASO_1>" \
  -H "Content-Type: application/json"
```

**Respuesta esperada (debería listar TODAS):**
```json
[
  {
    "id": 1,
    "nombre": "EMPRESA SISTEMA",
    "ruc": "00000000000",
    "emailContacto": "sysadmin@controlyape.com",
    "activo": true
  },
  {
    "id": 2,
    "nombre": "EMPRESA A SAC",
    "ruc": "20123456789",
    "emailContacto": "contacto@empresaa.com",
    "activo": true
  },
  {
    "id": 3,
    "nombre": "EMPRESA B SAC",
    "ruc": "20987654321",
    "emailContacto": "contacto@empresab.com",
    "activo": true
  }
]
```

---

## 3️⃣ LISTAR COMO USUARIO NORMAL (userempresaa)
```bash
# Primero login
curl -X POST http://localhost:9090/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "userempresaa",
    "password": "pass123"
  }'

# Luego listar empresas
curl -X GET http://localhost:9090/api/empresas \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json"
```

**Respuesta esperada (solo su empresa):**
```json
[
  {
    "id": 2,
    "nombre": "EMPRESA A SAC",
    "ruc": "20123456789",
    "emailContacto": "contacto@empresaa.com",
    "activo": true
  }
]
```

---

## 🔍 Verificación de la Lógica

### En EmpresaController.listar():

```
Si el usuario es SUPER_ADMIN:
  ✅ Devuelve empresaService.listarActivas() 
     (TODAS las empresas con activo=true)
  
Si el usuario es normal:
  ✅ Devuelve List.of(su_empresa)
     (Solo su empresa asignada)
```

### Pasos para Debugar si no funciona:

1. **Verificar que DataInitializer creó las empresas:**
   ```bash
   # Ver logs en la consola al iniciar:
   # ✔ Empresa A creada con id: 2
   # ✔ Empresa B creada con id: 3
   ```

2. **Verificar el rol del usuario:**
   ```bash
   # En la respuesta de login, verificar:
   "rol": "SUPER_ADMIN"  # Debe ser exactamente así
   ```

3. **Verificar que el usuario está autenticado:**
   - El token debe venir en la respuesta del login
   - Pasar con header `Authorization: Bearer <token>`

4. **Ver logs del servidor:**
   ```
   Usuario: superadmin
   Rol: SUPER_ADMIN
   ¿Es SUPER_ADMIN? true ✅
   Retorna todas las empresas
   ```

---

## ✅ Si todo está bien:

- SUPER_ADMIN ve 3 empresas (SISTEMA, A, B)
- userempresaa ve solo EMPRESA A
- userempresab ve solo EMPRESA B
