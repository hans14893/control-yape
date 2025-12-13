# Cambios de Seguridad - Filtrado por Empresa

## Descripción
Se implementó validación para asegurar que cada usuario autenticado solo pueda ver y modificar registros de su propia empresa.

## Cambios Realizados

### 1. YapeMovimientoController
**Archivo**: `src/main/java/com/control/yape/controller/YapeMovimientoController.java`

- ✅ Inyección de `AuthUsuarioService` en el constructor
- ✅ `GET /api/yape-movimientos` - Filtra movimientos por empresa del usuario
- ✅ `GET /api/yape-cuentas/{cuentaId}/movimientos` - Valida que la cuenta pertenezca a su empresa
- ✅ `POST /api/yape-cuentas/{cuentaId}/movimientos` - Valida empresa del usuario
- ✅ `GET /api/empresas/{empresaId}/movimientos` - Solo accede a su propia empresa
- ✅ `GET /api/empresas/{empresaId}/dashboard/total-recibido` - Solo accede a su propia empresa

### 2. YapeCuentaController
**Archivo**: `src/main/java/com/control/yape/controller/YapeCuentaController.java`

- ✅ Inyección de `AuthUsuarioService` en el constructor
- ✅ `GET /api/yape-cuentas/empresa/{empresaId}` - Solo lista cuentas de su empresa
- ✅ `GET /api/yape-cuentas/{id}` - Valida que la cuenta le pertenezca
- ✅ `POST /api/yape-cuentas/empresa/{empresaId}` - Solo crea en su empresa
- ✅ `PUT /api/yape-cuentas/{id}` - Solo actualiza cuentas de su empresa
- ✅ `DELETE /api/yape-cuentas/{id}` - Solo desactiva cuentas de su empresa

### 3. EmpresaController
**Archivo**: `src/main/java/com/control/yape/controller/EmpresaController.java`

- ✅ Inyección de `AuthUsuarioService` en el constructor
- ✅ `GET /api/empresas` - Solo retorna la empresa del usuario autenticado
- ✅ `GET /api/empresas/{id}` - Solo accede a su propia empresa
- ✅ `PUT /api/empresas/{id}` - Solo actualiza su propia empresa

### 4. UsuarioController
**Archivo**: `src/main/java/com/control/yape/controller/UsuarioController.java`

- ✅ Inyección de `AuthUsuarioService` en el constructor
- ✅ `GET /api/usuarios` - Solo lista usuarios de su empresa
- ✅ `GET /api/usuarios/{id}` - Valida que el usuario pertenezca a su empresa
- ✅ `POST /api/usuarios` - Solo crea usuarios en su empresa
- ✅ `PUT /api/usuarios/{id}` - Solo edita usuarios de su empresa, no permite cambio de empresa

### 5. YapeMovimientoService
**Archivo**: `src/main/java/com/control/yape/service/YapeMovimientoService.java`

- ✅ Agregado método: `void validarCuentaPertenece(Long cuentaId, Long empresaId)`

### 6. YapeMovimientoServiceImpl
**Archivo**: `src/main/java/com/control/yape/service/impl/YapeMovimientoServiceImpl.java`

- ✅ Implementado método `validarCuentaPertenece()` para validar que una cuenta pertenece a una empresa

### 7. UsuarioRepository
**Archivo**: `src/main/java/com/control/yape/repository/UsuarioRepository.java`

- ✅ Agregado método: `List<Usuario> findByEmpresa_IdOrderByNombreCompleto(Long empresaId)`

## Cómo Funciona

1. **AuthUsuarioService** obtiene el usuario autenticado del contexto de Spring Security
2. Cada controlador valida que el usuario acceda solo a recursos de su empresa
3. Si intenta acceder a una empresa diferente, lanza `IllegalArgumentException`

## Ejemplo de Flujo

```
Usuario (empresa_id = 1) intenta acceder a:
GET /api/empresas/2/movimientos
           ↓
Controller obtiene usuario actual (empresa_id = 1)
           ↓
Compara: ¿2 == 1?
           ↓
NO → Lanza: "No tiene permisos para acceder a esta empresa"
```

## Pruebas Recomendadas

1. Loguear usuario de empresa 1
2. Intentar acceder a `/api/empresas/2/movimientos` → Debe fallar
3. Acceder a `/api/empresas/1/movimientos` → Debe funcionar
4. Crear usuario en empresa diferente → Debe fallar

## Notas Importantes

- ✅ No hay errores de compilación
- ✅ Todas las validaciones lanzan `IllegalArgumentException` con mensaje descriptivo
- ✅ El usuario no puede cambiar de empresa (en POST de usuarios)
- ✅ El endpoint `/api/empresas` ahora retorna solo su empresa, no todas
