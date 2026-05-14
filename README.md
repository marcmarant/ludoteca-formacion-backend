# Ludoteca Backend

Backend en Spring Boot para la app de gestion de una ludoteca.

## Funcionalidades añadidas y cambios respecto al tutorial

### Endpoint separado para POST Y PUT
Respecto al tutorial he decidido separar los endpoints asi como los casos de uso que involuvcran la creación o actualización de un recurso, ya que muchas veces tienen lógica diferente si bien en ambos casos se acaba ejecutando una operación save en base de datos.

### Autenticacion y autorizacion
- Sericio de Login con JWT en `/auth`.
- Seguridad stateless con Spring Security.
- Rutas publicas:
  - `GET /**`
  - `POST /authors/search`
  - `POST /loans/search`
  - `/auth`,
- Rutas protegidas:
  - `POST`, `PUT`, `DELETE` requieren token.
- Rutas de administracion:
  - `/users` requiere rol `ADMIN`.

### Servicio users
Servicio de Users accesible en `/users` para gestionar la autenticacion y autorizacion he creado un nuevo servicio de usuarios con su propia entidad, repositorio, servicio y controlador. Este servicio se encarga de gestionar los usuarios del sistema, que tienen un rol (actualmente empleado o administrador). Protegiendo las rutas de edición de recursos user solo para usuarios con el rl de administrador.

### Gestor de excepciones global
`GlobalExceptionHandler` maneja todas las excepciones de la aplicacion y devuelve respuestas HTTP correspondientes:
- `400 BAD REQUEST` para validaciones fallidas.
- `401 UNAUTHORIZED` para accesos sin token o token invalido.
- `404 NOT FOUND` para recursos no encontrados.
- `409 CONFLICT` para conflictos de borrado por referencias.
- `500 INTERNAL SERVER ERROR` para errores inesperados.
  Para las excepciones he creado clases personalizadas para casos concretos, y para otras más genericas he utilizado excepciones ya existentes.

## Estructura general

- `src/main/java/com/ccsw/tutorial/auth`: login, JWT y filtro de autenticacion
- `src/main/java/com/ccsw/tutorial/author`: API y logica de autores
- `src/main/java/com/ccsw/tutorial/category`: API y logica de categorias
- `src/main/java/com/ccsw/tutorial/game`: API y logica de juegos
- `src/main/java/com/ccsw/tutorial/client`: API y logica de clientes
- `src/main/java/com/ccsw/tutorial/loan`: API y logica de prestamos
- `src/main/java/com/ccsw/tutorial/user`: API y logica de usuarios
- `src/main/java/com/ccsw/tutorial/common`: excepciones, criterios y utilidades compartidas
- `src/main/resources/application.properties`: configuracion general
- `src/main/resources/data.sql`: datos iniciales

## Documentacion y herramientas

Swagger UI:
- `http://localhost:8080/swagger-ui/index.html`

OpenAPI:
- `http://localhost:8080/v3/api-docs`

## Ejecucion de tests

Todos los tests:
```powershell
.\mvnw test
```

Solo una clase de test:
```powershell
.\mvnw -Dtest=GameIT test
```

## Notas

- El backend usa base de datos H2 en memoria y carga datos de ejemplo al arrancar.
- Para peticiones protegidas, enviar cabecera:
  - `Authorization: Bearer <token>`

## Usuarios de prueba para probar la app (data.sql)

- `admin / admin` -> rol `ROLE_ADMIN`
- `employee1 / employee1` -> rol `ROLE_EMPLOYEE`
