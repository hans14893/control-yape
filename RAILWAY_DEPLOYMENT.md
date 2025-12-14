# 🚀 Desplegar Control-Yape en Railway

## Paso 1: Crear Cuenta en Railway

1. Ve a https://railway.app
2. Click en **"Start a New Project"**
3. Elige **"Deploy from GitHub"**
4. Autoriza GitHub (conecta tu cuenta)

---

## Paso 2: Seleccionar tu Repositorio

1. Busca tu repositorio de `control-yape`
2. Haz click en **"Deploy"**
3. Railway automáticamente detectará que es un **Maven + Java 21** proyecto

---

## Paso 3: Configurar MySQL Database

1. En el dashboard de Railway, ve a **"New Service"**
2. Busca **"MySQL"**
3. Haz click para crear una instancia de MySQL
4. Espera a que se configure (1-2 minutos)

---

## Paso 4: Configurar Variables de Entorno

### En el Dashboard de Railway:

1. Abre tu **servicio Java (control-yape)**
2. Ve a la pestaña **"Variables"**
3. Agrega las siguientes variables:

```bash
SPRING_PROFILES_ACTIVE=prod

# Base de datos (Railway genera estos valores automáticamente)
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/railway
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=<tu-password>

# JPA/Hibernate
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
SPRING_JPA_SHOW_SQL=false
SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.MySQLDialect
```

### Para obtener las credenciales de MySQL en Railway:

1. Abre el servicio **MySQL** en Railway
2. Ve a la pestaña **"Connect"**
3. Busca la sección **"Public Networking"** o **"Private URL"**
4. Copia los valores de:
   - Host
   - Port
   - Database
   - Username
   - Password

---

## Paso 5: Enlazar MySQL con Java

1. Ve a tu servicio **Java (control-yape)**
2. Abre **"Variables"** nuevamente
3. Busca **"MySQL"** en la sección de referencias
4. Haz click para **"Add a reference to MySQL"**

Railway automáticamente inyectará las variables correctas.

---

## Paso 6: Configurar el Puerto

Railway asigna automáticamente un puerto. Asegúrate que tu `application-prod.properties` tenga:

```properties
server.port=${PORT:9090}
```

---

## Paso 7: Verificar Configuración de Build

Railway automáticamente:
- ✅ Detecta `pom.xml` (Maven)
- ✅ Instala Java 21
- ✅ Compila con `mvn clean package`
- ✅ Ejecuta `java -jar target/control-yape-1.0.0-SNAPSHOT.jar`

**No necesitas hacer nada más.**

---

## Paso 8: Deploy

1. Ve a la pestaña **"Deployments"** en tu servicio
2. Haz click en **"Deploy"** o espera a que se dispare automáticamente
3. Monitorea los logs en **"Logs"**

**Espera a ver: "Tomcat started on port..."**

---

## Paso 9: Obtener URL Pública

1. Ve a tu servicio **Java (control-yape)**
2. En la parte superior, verás un botón con tu **URL pública**
3. Ejemplo: `https://control-yape-prod-1234.railway.app`

---

## ✅ Verificar que está funcionando

**Test 1: Login**
```bash
curl -X POST https://control-yape-prod-1234.railway.app/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "super",
    "password": "super123"
  }'
```

**Test 2: Listar Empresas**
```bash
curl -X GET https://control-yape-prod-1234.railway.app/api/empresas \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## 🔐 Seguridad en Producción

### Cambiar Credenciales del SUPERADMIN

1. Abre la base de datos desde Railway
2. Ejecuta:

```sql
-- Cambiar password del super admin
UPDATE usuario SET password = 'bcrypt_hash_de_tu_nueva_password' 
WHERE username = 'super';
```

**Para generar el hash BCrypt**, usa un generador online o:
```bash
# En tu terminal local
echo 'tu_password' | spring-crypto --encode
```

### Deshabilitar DataInitializer en Producción

Ya está configurado en tu código:
```java
@Profile("!prod")  // No se ejecuta en producción
public class DataInitializer
```

---

## 📊 Monitorear tu Aplicación

**En el Dashboard de Railway:**
- 📈 **Metrics**: Ver CPU, memoria, requests
- 📋 **Logs**: Ver logs en tiempo real
- 🔄 **Deployments**: Historial de despliegues
- ⚙️ **Settings**: Configurar dominio personalizado, variables, etc.

---

## 🚀 Actualizar tu Aplicación

**Cada vez que hagas cambios:**

```bash
# En tu terminal local
cd d:\CURSO IDAT\CICLO 4\...\control-yape
git add .
git commit -m "feat: Nueva funcionalidad"
git push origin main
```

**Railway automáticamente:**
1. Detecta el push
2. Compila el proyecto
3. Ejecuta los tests
4. Despliega automáticamente

---

## 🛠 Troubleshooting

### Error: "Failed to connect to database"

**Solución:**
1. Verifica que MySQL está running (verde ✅)
2. Verifica variables de entorno
3. Revisa logs: `SPRING_DATASOURCE_URL`

### Error: "Port already in use"

**Solución:**
Railway maneja automáticamente el puerto con `${PORT}`. Asegúrate que tu `application-prod.properties` tiene:
```properties
server.port=${PORT:9090}
```

### Aplicación no inicia

**Solución:**
1. Abre los **Logs** en Railway
2. Busca el error específico
3. Verifica las variables de entorno
4. Ejecuta localmente: `mvn clean package` para detectar problemas

---

## 📞 URLs Útiles

- **Railway Dashboard:** https://railway.app
- **Documentación Railway:** https://docs.railway.app
- **Mi Aplicación (ejemplo):** https://control-yape-prod-xxxx.railway.app
- **API Docs:** https://control-yape-prod-xxxx.railway.app/swagger-ui.html (si está configurado)

---

## ✅ Checklist Final

- [ ] Cuenta creada en Railway
- [ ] Repositorio conectado
- [ ] MySQL creado y conectado
- [ ] Variables de entorno configuradas
- [ ] Primer despliegue exitoso
- [ ] Login funciona con token
- [ ] Base de datos tiene datos iniciales
- [ ] URL pública obtenida
- [ ] Credenciales de admin cambiadas
- [ ] Logs monitoreados

---

**¡Listo! Tu aplicación está en producción en Railway** 🎉
