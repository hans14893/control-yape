# Control Yape - Backend API

Aplicación Spring Boot para gestión de transacciones y cuentas Yape.

## Requisitos

- **Java:** 21 LTS mínimo
- **Base de datos:** MySQL 5.7+ 
- **Maven:** 3.6+

## Variables de Entorno Requeridas (Producción)

```bash
# Base de datos (REQUERIDO en producción)
export DB_URL=jdbc:mysql://HOST:PORT/DATABASE
export DB_USER=usuario
export DB_PASSWORD=contraseña_segura

# Opcional
export SERVER_PORT=9090
export JPA_DDL_AUTO=validate
export SPRING_PROFILES_ACTIVE=prod
```

## Build y Despliegue

### Compilar
```bash
./mvnw clean package -DskipTests
```

### Ejecutar en Producción
```bash
export SPRING_PROFILES_ACTIVE=prod
export DB_URL=jdbc:mysql://db-host:3306/control_yape
export DB_USER=prod_user
export DB_PASSWORD=$(cat /run/secrets/db_password)  # Usar secrets en Docker
java -jar target/control-yape-1.0.0-SNAPSHOT.jar
```

### Ejecutar en Desarrollo
```bash
export SPRING_PROFILES_ACTIVE=dev
./mvnw spring-boot:run
```

## Perfiles de Configuración

| Perfil | Descripción | DDL Hibernate |
|--------|-------------|---------------|
| `dev` | Desarrollo local | create-drop |
| `prod` | Producción | validate |
| (default) | Valores base configurables | update |

## Endpoints Principales

- `POST /api/auth/login` - Autenticación
- `GET /api/empresas` - Listar empresas
- `GET /api/yape-cuentas/empresa/{id}` - Cuentas por empresa
- `GET /api/yape-movimientos/empresa/{id}` - Movimientos por empresa

## Seguridad

- ✅ Contraseñas encriptadas con BCrypt
- ✅ JWT para autenticación
- ✅ CORS configurado
- ✅ SQL Injection protected (prepared statements)
- ✅ Session cookies secure

## Notas de Despliegue

1. **Nunca** usar `ddl-auto=create` o `create-drop` en producción
2. **Siempre** usar variables de entorno para credenciales
3. **Aplicar** migraciones de BD antes de actualizar
4. **Revisar** logs de aplicación regularmente
5. **Usar** reverse proxy (Nginx/Apache) en frente

---

*Última actualización: 2025-12-13*
