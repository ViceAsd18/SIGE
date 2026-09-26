# SIGE Escolar — Sistema Integral de Gestión Estudiantil

Plataforma de gestión escolar construida con arquitectura de microservicios, diseñada como proyecto de portafolio para demostrar un flujo completo de diseño de dominio, arquitectura distribuida, comunicación síncrona y asíncrona, y despliegue containerizado.

## Stack tecnológico

- **Java 21** + **Spring Boot 4.1**
- **Spring Cloud** (Config Server, Eureka, Gateway, OpenFeign)
- **MySQL 8.4** — una base de datos lógica por microservicio
- **Apache Kafka** — comunicación asíncrona basada en eventos
- **WebSocket (STOMP)** — mensajería en tiempo real
- **Docker Compose** — orquestación completa del ecosistema
- **Flyway** — versionado de esquema de base de datos
- **JWT** — autenticación entre Gateway y microservicios

## Arquitectura

El sistema está compuesto por 9 microservicios de dominio, más 3 componentes de infraestructura:

| Componente | Puerto | Responsabilidad |
|---|---|---|
| Config Server | 8888 | Configuración centralizada (Git como backend) |
| Eureka Server | 8761 | Registro y descubrimiento de servicios |
| Gateway | 8080 | Punto de entrada único, enrutamiento, validación JWT |
| ms-identidad-acceso | 8081 | Personas, roles, autenticación |
| ms-estudiantes | 8082 | Estudiantes, apoderados y sus relaciones |
| ms-academico | 8083 | Niveles, periodos, cursos, matrículas, asignaciones docentes |
| ms-evaluaciones-notas | 8084 | Evaluaciones, calificaciones, solicitudes de excepción |
| ms-anotaciones | 8085 | Anotaciones disciplinarias y académicas |
| ms-mensajeria | 8086 | Conversaciones y mensajería, con tiempo real vía WebSocket |
| ms-comunicaciones | 8087 | Comunicados institucionales y notificaciones |
| ms-calendario-reuniones | 8088 | Eventos institucionales y reuniones |
| ms-auditoria | 8089 | Registro de auditoría, alimentado por eventos Kafka |

Cada microservicio tiene su propia base de datos lógica (esquema aislado en un servidor MySQL compartido para desarrollo local), y ningún servicio accede directamente a los datos de otro — toda comunicación cruzada ocurre vía **OpenFeign** (síncrona) o **Kafka** (asíncrona).

### Documentos de diseño

El proceso de diseño completo está documentado en `docs/`:
- `SIGE-dominio-consolidado.md` — análisis del dominio de negocio
- `MODELO_LOGICO_BD.md` — modelo relacional y decisiones de transformación
- `ARQUITECTURA_MICROSERVICIOS.md` — arquitectura, eventos y decisiones técnicas

## Comunicación entre servicios

**Síncrona (OpenFeign):** validaciones cruzadas en tiempo de escritura — por ejemplo, `ms-academico` valida contra `ms-identidad-acceso` que un `personaRolId` corresponda a un Docente activo antes de crear una Asignación Docente.

**Asíncrona (Kafka):** los 9 microservicios de dominio publican eventos de negocio a sus propios tópicos, y todos publican adicionalmente al tópico común `auditoria.eventos`, consumido por `ms-auditoria` para generar el registro de auditoría de forma automática y desacoplada. `ms-comunicaciones` consume eventos de otros servicios (hoy: `AnotacionCreada`) para generar Notificaciones automáticas.

**Tiempo real (WebSocket):** `ms-mensajeria` consume su propio evento `MensajeEnviado` y lo reenvía vía STOMP a los clientes suscritos a `/topic/conversaciones/{id}`, permitiendo mensajería en vivo sin necesidad de polling.

## Cómo levantar el proyecto

Requiere Docker y Docker Compose.

```bash
git clone https://github.com/ViceAsd18/SIGE.git
cd SIGE
docker compose up -d
```

Esto levanta los 14 contenedores (MySQL, Kafka, Config Server, Eureka, Gateway y los 9 microservicios). En equipos con RAM limitada (recomendado 8GB+), es posible levantar solo un subconjunto según lo que se quiera probar:

```bash
docker compose up -d mysql config-server eureka-server kafka ms-identidad-acceso ms-estudiantes
```

Para confirmar el estado de los contenedores:
```bash
docker compose ps
```

Los que tienen healthcheck configurado deberían mostrar `healthy` una vez que terminan de inicializar. El dashboard de Eureka (`http://localhost:8761`) muestra además todos los servicios ya registrados y disponibles para recibir tráfico.

> **Nota de configuración:** el repositorio de configuración centralizada (`sige-config-repo`) es local y no forma parte de este repositorio Git por diseño (se sirve al Config Server vía `file://`). Para reproducir el entorno completo, es necesario crear ese repositorio Git local con los archivos `.yml` de cada servicio — ver `docs/ARQUITECTURA_MICROSERVICIOS.md` para el detalle de cada configuración.

## Desarrollo local (sin Docker)

Cada microservicio puede ejecutarse individualmente con Maven:

```bash
cd ms-identidad-acceso
./mvnw spring-boot:run
```

Requiere Config Server y Eureka corriendo previamente, y una base de datos MySQL local con el esquema correspondiente creado (ver migraciones Flyway en `src/main/resources/db/migration` de cada servicio).

## Estado del proyecto

- 9 microservicios de dominio con CRUD completo y validación cruzada vía Feign
- Infraestructura de microservicios (Config Server, Eureka, Gateway)
- Autenticación JWT
- Docker Compose completo
- Arquitectura orientada a eventos con Kafka (9 productores, 2 consumidores de negocio)
- Mensajería en tiempo real vía WebSocket

### Roadmap / limitaciones conocidas

- **Pruebas automatizadas:** no implementadas aún (JUnit 5 + Mockito planificados).
- **Autorización granular:** hoy cualquier JWT válido puede llamar a cualquier endpoint; falta restricción por rol a nivel de cada microservicio.
- **Frontend:** no implementado; el proyecto se probó de punta a punta vía REST Client / `.http`.
- **Notificaciones automáticas:** `ms-comunicaciones` hoy solo consume el evento `AnotacionCreada`; el resto de los eventos catalogados en la arquitectura (calificaciones, matrículas, reuniones) siguen el mismo patrón y quedan como extensión natural.
- **Transactional Outbox:** la publicación de eventos Kafka ocurre dentro de la misma transacción de negocio sin patrón Outbox formal; existe una ventana teórica de inconsistencia si Kafka no está disponible en el momento de publicar (documentado como limitación conocida, no bloqueante para el alcance de este proyecto).