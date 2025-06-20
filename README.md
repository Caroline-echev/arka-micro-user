# Microservicio de Gestión de Usuarios (`micro_user`)

Este microservicio proporciona funcionalidades para la **gestión de usuarios**, **autenticación** y **direcciones**, dentro de una arquitectura **reactiva** basada en Spring WebFlux.

---

## Características Principales

- **Gestión de usuarios**: Creación de usuarios con diferentes roles (`admin`, `logística`, `cliente`).
- **Autenticación**: Inicio de sesión con JWT y refresco de tokens.
- **Gestión de direcciones**: Asociación y actualización de direcciones por usuario.
- **Seguridad**: Protección de endpoints mediante JWT y roles.
- **Documentación**: API documentada con Swagger/OpenAPI.

---

## Tecnologías Utilizadas

- **Spring WebFlux** – Manejo reactivo de peticiones.
- **R2DBC** – Acceso reactivo a base de datos.
- **PostgreSQL** – Base de datos relacional.
- **JWT (JSON Web Tokens)** – Autenticación segura.
- **Flyway** – Migraciones de base de datos.
- **Swagger / OpenAPI** – Documentación automática.
- **Lombok** – Eliminación de código repetitivo.
- **MapStruct** – Mapeo entre modelos y DTOs.

---


##  Estructura del Proyecto

micro_user/
├── adapters/

│ ├── driving/ # Entrada: Interfaces externas (HTTP)

│ │ └── reactive/

│ │ ├── controller/ # Controladores REST

│ │ ├── dto/ # Objetos de transferencia de datos

│ │ └── mapper/ # Mapeo entre DTOs y modelos de dominio

│ ├── driven/ # Salida: Infraestructura externa

│ │ ├── r2dbc/ # Implementación de persistencia reactiva

│ │ │ ├── adapter/ # Implementación de los puertos SPI

│ │ │ ├── config/ # Configuraciones específicas de R2DBC

│ │ │ ├── entity/ # Entidades JPA para persistencia

│ │ │ ├── mapper/ # Mapeo entre entidades y modelos de dominio

│ │ │ ├── repository/ # Repositorios R2DBC

│ │ │ └── util/ # Utilidades específicas de persistencia

│ │ └── security/ # Implementación de seguridad

│ │ ├── adapter/ # Servicios relacionados a JWT y autenticación

│ │ └── util/ # Funciones utilitarias de seguridad

│

├── configuration/ # Configuración de la aplicación Spring Boot

│ ├── bean/ # Beans personalizados

│ ├── exception/ # Manejadores globales de errores

│ ├── flyway/ # Configuración de migraciones con Flyway

│ ├── security/ # Configuración de seguridad (JWT, filtros)

│ ├── swagger/ # Configuración de documentación Swagger

│ └── util/ # Utilidades generales de configuración

│
├── domain/ # Núcleo del dominio (independiente de frameworks)

│ ├── api/ # Interfaces de servicios (puertos de entrada)

│ ├── exception/ # Excepciones personalizadas del dominio

│ │ └── enums/ # Enumeraciones para tipos de error

│ ├── model/ # Modelos de dominio (entidades, value objects)

│ ├── spi/ # Interfaces de persistencia (puertos de salida)

│ └── usecase/ # Casos de uso (lógica de negocio)

│ └── util/ # Utilidades reutilizables en los casos de uso

│

└── application.properties # Archivo de configuración principal


---


## Configuración del Entorno

Este microservicio utiliza Spring Boot 3.4.5, programación reactiva con WebFlux, persistencia no bloqueante con R2DBC, y seguridad basada en JWT. A continuación se describe la configuración principal:

### JWT

Se usa para la autenticación de usuarios mediante tokens firmados.

```yaml
jwt:
  secret: ${JWT_SECRET}           # Clave secreta usada para firmar los tokens
  expiration-time: 3600           # Tiempo de expiración en segundos (1 hora)
Base de Datos y R2DBC
La configuración utiliza PostgreSQL con esquema dedicado (micro_user) y soporte reactivo con R2DBC.

yaml
adapters:
  r2dbc:
    host: localhost
    port: 5432
    database: arka
    schema: micro_user
    username: postgres
    password: postgres

spring:
  r2dbc:
    url: r2dbc:postgresql://${adapters.r2dbc.host}:${adapters.r2dbc.port}/${adapters.r2dbc.database}?schema=${adapters.r2dbc.schema}
    username: ${adapters.r2dbc.username}
    password: ${adapters.r2dbc.password}
Migraciones con Flyway
Flyway se encarga de aplicar automáticamente los scripts de migración almacenados en classpath:db/migration.

yaml
spring:
  flyway:
    enabled: true
    url: jdbc:postgresql://localhost:5432/arka
    user: postgres
    password: postgres
    schemas: micro_user
    baseline-on-migrate: true
    locations: classpath:db/migration
Logging y Puertos
yaml
logging:
  level:
    org.flywaydb: DEBUG
    org.springframework.security: DEBUG

server:
  port: 8080