# Modelo Lógico de Base de Datos
## Plataforma SIGE Escolar

**Fuente:** este documento transforma el Modelo Conceptual del Dominio SIGE (Fase 1, documento `SIGE-dominio-consolidado.md`, cerrada con los 6 bloqueantes resueltos) en un modelo relacional lógico.
**Alcance:** entidades, atributos, claves primarias/foráneas, relaciones y verificación de formas normales. No incluye tipos de dato específicos de motor, índices, particionamiento, ni decisiones de ORM — eso corresponde al Modelo Físico.

---

## Nota previa: Decisiones de transformación del modelo conceptual al lógico

El modelo conceptual describe el dominio en términos de negocio; el paso a un modelo lógico obliga a resolver varias situaciones que el conceptual dejó expresadas de forma narrativa. Cada decisión de esta sección se documenta explícitamente para no corregir el dominio "en silencio".

### 1. Relaciones N:M convertidas en entidades asociativas

| Relación conceptual | Entidad asociativa resultante | Motivo |
|---|---|---|
| Persona ↔ Rol (una Persona puede tener uno o más Roles simultáneos — Fase 1, sección 2) | `PERSONA_ROL` | Es, en esencia, una relación N:M: una Persona puede tener varios Roles y un Rol es compartido por muchas Personas. Además, `PERSONA_ROL` es el punto de referencia que ya exigían Auditoría y la capa de autorización ("rol activo"). |
| Apoderado ↔ Estudiante (N:M, confirmada desde Fase 1.3) | `APODERADO_ESTUDIANTE` | Relación N:M explícita en el dominio, con atributo propio `tipo_relacion` (principal/secundario). |
| Docente ↔ Asignatura ↔ Curso (con vigencia temporal) | `ASIGNACION_DOCENTE` | Ya era una entidad intermedia en el modelo conceptual (3.8); se mantiene como asociativa en el modelo lógico. |
| Estudiante ↔ Curso, a través del tiempo | `MATRICULA` | Aunque el análisis conceptual la trata como "entidad núcleo del dominio", estructuralmente resuelve una relación N:M entre Estudiante y Curso a lo largo de distintos periodos (un estudiante con múltiples matrículas históricas; un curso con múltiples estudiantes). Se clasifica como asociativa por esta razón estructural — **esto no reduce su relevancia de negocio**, que se mantiene documentada en su descripción. |
| Conversación ↔ Persona (participantes) | `CONVERSACION_PARTICIPANTE` | Relación N:M explícita en el modelo conceptual (3.13). |
| Reunión ↔ Persona (audiencia esperada) | `REUNION_PARTICIPANTE` | Relación N:M explícita (3.20: "Audiencia (participantes esperados)"). |

### 2. Atributos derivados que NO deben almacenarse

Consistente con la corrección aplicada en la revisión arquitectónica de la Fase 1 (evitar fuentes de verdad duplicadas):

- **Historial académico del Estudiante** — vista/proyección derivada de Matrícula, Curso, Asignación Docente, Evaluación, Calificación y Anotación. No existe tabla `HISTORIAL_ACADEMICO`.
- **Hoja de vida del Estudiante** — vista construida a partir de `ANOTACION`. No existe tabla propia.
- **Calendario** — vista que proyecta `EVALUACION`, `REUNION`, cierre de `PERIODO_ACADEMICO`/`SUBPERIODO_ACADEMICO` y `EVENTO_INSTITUCIONAL`. Confirmado en el cierre de Fase 1.8 como proyección, no entidad — **no existe tabla `CALENDARIO`**.
- **Promedio general del estudiante** — corresponde al módulo de Reportes (fuera del alcance de este modelo), no se almacena en este esquema.

### 3. Decisiones de normalización (eliminación de redundancia transitiva)

- `ASIGNACION_DOCENTE` **no** almacena `periodo_academico_id` como columna propia, aunque el modelo conceptual lo menciona como parte de "Docente + Asignatura + Curso + Periodo". El periodo académico ya es derivable transitivamente vía `ASIGNACION_DOCENTE.curso_id → CURSO.periodo_academico_id`. Mantenerlo duplicado violaría 3FN (dependencia transitiva).
- Por el mismo criterio, `MATRICULA` **no** almacena `periodo_academico_id` directo — se obtiene vía `MATRICULA.curso_id → CURSO.periodo_academico_id`.

### 4. Resolución de un pendiente de dominio, a nivel de modelo lógico

El análisis conceptual (Fase 1, sección 3.4) dejó explícitamente abierto si el motivo de cambio de estado de una Matrícula vive en un atributo propio o exclusivamente en el registro de auditoría, indicando que era "detalle de modelo formal, no de dominio". Para este modelo lógico se resuelve: `MATRICULA.motivo` como atributo propio (texto breve, para consulta directa), complementado por `REGISTRO_AUDITORIA` para el historial completo de cambios. Se deja constancia de que esta es una decisión de este nivel de modelado, no una reinterpretación de una regla de dominio ya cerrada.

### 5. Referencias polimórficas (Notificación y Registro de Auditoría)

`NOTIFICACION` y `REGISTRO_AUDITORIA` son mecanismos transversales que deben poder referenciar **cualquier entidad del dominio** (una Anotación, una Evaluación, una Reunión, una Solicitud, etc.). El modelo relacional clásico no permite una única clave foránea apuntando a tablas distintas. Se resuelve mediante un patrón de **referencia genérica**: dos columnas, `entidad_afectada_tipo` (discriminador textual) y `entidad_afectada_id` (identificador), **sin integridad referencial declarativa a nivel lógico**. Esa integridad deberá garantizarse a nivel de aplicación o mediante mecanismos específicos del motor en el Modelo Físico. Se documenta como una limitación conocida y aceptada de este nivel de modelo, no como un descuido.

### 6. Alcance institucional de Comunicado, sin entidad Establecimiento

La Fase 1 dejó pendiente (#14) si "Establecimiento" debe existir como entidad formal. Como no fue resuelto, el alcance "institucional completo" de un `COMUNICADO` se representa dejando nulas las columnas `curso_id` y `nivel_educativo_id` (en vez de referenciar una entidad inexistente). **Decisión provisoria**, sujeta a ajuste si "Establecimiento" se formaliza en una fase posterior.

### 7. Subtipos de Rol: solo Estudiante y Apoderado tienen tabla propia

De los seis roles definidos en el dominio, solo `ESTUDIANTE` y `APODERADO` se modelan como especialización propia de `PERSONA_ROL` (con tabla dedicada), porque: (a) son referenciados extensivamente por otras entidades como clave de negocio estable (Matrícula, Calificación, Anotación, ApoderadoEstudiante), y/o (b) tienen atributos propios (`INFORMACION_SENSIBLE_ESTUDIANTE`). Los roles Docente, Inspector, Administrativo y Directivo se referencian **directamente vía `persona_rol_id`**, ya que no agregan atributos estructurales propios en este nivel — sus responsabilidades se expresan a través de las entidades que los relacionan (ej. `ASIGNACION_DOCENTE`).

### 8. Notificación como entidad persistente

El dominio dejó como pendiente no bloqueante (#16) si Notificación debe ser persistente o efímera. Para que el modelo lógico sea consistente (una tabla requiere persistencia), se opta por modelarla **como entidad persistente con estado** (`Enviada` / `Leída`). El catálogo completo de eventos que la disparan (pendiente #12) no bloquea la estructura de esta tabla y queda para diseño de API/eventos.

### 9. Mensaje referencia a Persona, no a Persona+Rol

A diferencia de `ANOTACION` y `REGISTRO_AUDITORIA` (que sí requieren conocer el rol activo del autor), `MENSAJE.autor` se modela como referencia directa a `PERSONA`. El rol relevante para Mensajería se valida como regla de negocio al **iniciar la Conversación** (capa de autorización, no en cada mensaje individual) — se documenta esta asimetría intencional.

---

## Índice de entidades

### Entidades principales
1. PERSONA
2. ROL
3. ESTUDIANTE
4. INFORMACION_SENSIBLE_ESTUDIANTE
5. APODERADO
6. NIVEL_EDUCATIVO
7. PERIODO_ACADEMICO
8. SUBPERIODO_ACADEMICO
9. CURSO
10. ASIGNATURA
11. EVALUACION
12. CALIFICACION
13. ANOTACION
14. CONVERSACION
15. MENSAJE
16. COMUNICADO
17. NOTIFICACION
18. REGISTRO_AUDITORIA
19. EVENTO_INSTITUCIONAL
20. REUNION

### Entidades asociativas
21. PERSONA_ROL
22. MATRICULA
23. ASIGNACION_DOCENTE
24. APODERADO_ESTUDIANTE
25. CONVERSACION_PARTICIPANTE
26. REUNION_PARTICIPANTE
27. SOLICITUD_EXCEPCION_CALIFICACION
28. SOLICITUD_ACCESO_CONVERSACION

---

## 1. PERSONA
Identidad única de cualquier individuo que interactúa con el sistema, independiente de los roles que desempeñe (Fase 1, sección 2).

| Atributo | Propósito |
|---|---|
| persona_id (PK) | Identificador único de la identidad |
| rut_documento_identidad | Identificación oficial de la persona |
| nombres | Nombres de la persona |
| apellidos | Apellidos de la persona |
| fecha_nacimiento | Fecha de nacimiento |
| email | Contacto principal |
| telefono | Contacto secundario |
| usuario | Credencial de acceso |
| password_hash | Credencial de acceso (representación lógica; detalle de hashing es de Modelo Físico) |

### Claves foráneas
Ninguna.

### Relaciones
Persona (1) — (N) PersonaRol
Persona (1) — (N) RegistroAuditoria (como actor)
Persona (1) — (N) Mensaje (como autor)
Persona (N) — (M) Conversación (vía ConversacionParticipante)
Persona (N) — (M) Reunión (vía ReunionParticipante)

---

## 2. ROL
Catálogo cerrado de tipos de rol que una Persona puede desempeñar en el establecimiento.

| Atributo | Propósito |
|---|---|
| rol_id (PK) | Identificador del tipo de rol |
| nombre_rol | Estudiante, Apoderado, Docente, Inspector, Administrativo o Directivo |

### Claves foráneas
Ninguna.

### Relaciones
Rol (1) — (N) PersonaRol

---

## 3. ESTUDIANTE
Especialización de PersonaRol para el rol Estudiante. Representa la identidad académica del estudiante, independiente de si tiene una matrícula activa (Fase 1, sección 3.1).

| Atributo | Propósito |
|---|---|
| estudiante_id (PK) | Identificador de la especialización Estudiante |
| persona_rol_id (FK, único) | Referencia a la asignación de rol Estudiante en PersonaRol |

### Claves foráneas
- persona_rol_id → PERSONA_ROL

### Relaciones
Estudiante (1) — (1) InformacionSensibleEstudiante
Estudiante (1) — (N) Matrícula
Estudiante (1) — (N) Calificación
Estudiante (1) — (N) Anotación
Estudiante (N) — (M) Apoderado (vía ApoderadoEstudiante)
Estudiante (1) — (N) Notificación (como destinatario, vía Persona)

---

## 4. INFORMACION_SENSIBLE_ESTUDIANTE
Datos de salud, necesidades educativas especiales (NEE) y observaciones médicas del estudiante, segregados con permisos propios (Fase 1, sección 3.2 y capa de autorización 5.1).

| Atributo | Propósito |
|---|---|
| informacion_sensible_id (PK) | Identificador del registro |
| estudiante_id (FK, único) | Estudiante al que pertenece la información |
| informacion_salud | Antecedentes de salud relevantes |
| informacion_nee | Necesidades educativas especiales |
| observaciones | Observaciones adicionales relevantes |

### Claves foráneas
- estudiante_id → ESTUDIANTE

### Relaciones
InformacionSensibleEstudiante (1) — (1) Estudiante

---

## 5. APODERADO
Especialización de PersonaRol para el rol Apoderado.

| Atributo | Propósito |
|---|---|
| apoderado_id (PK) | Identificador de la especialización Apoderado |
| persona_rol_id (FK, único) | Referencia a la asignación de rol Apoderado en PersonaRol |

### Claves foráneas
- persona_rol_id → PERSONA_ROL

### Relaciones
Apoderado (N) — (M) Estudiante (vía ApoderadoEstudiante)

---

## 6. NIVEL_EDUCATIVO
Clasificación curricular estable (ej. Educación Básica, Educación Media). Fase 1, sección 3.5.

| Atributo | Propósito |
|---|---|
| nivel_educativo_id (PK) | Identificador del nivel |
| nombre | Nombre del nivel educativo |

### Claves foráneas
Ninguna.

### Relaciones
NivelEducativo (1) — (N) Curso

---

## 7. PERIODO_ACADEMICO
Unidad temporal principal del dominio (año escolar). Fase 1, sección 3.17.

| Atributo | Propósito |
|---|---|
| periodo_academico_id (PK) | Identificador del periodo |
| nombre_anio | Año/nombre del periodo (ej. 2026) |
| fecha_inicio | Inicio del periodo |
| fecha_termino | Término del periodo |
| estado | Abierto / Cerrado |

### Claves foráneas
Ninguna.

### Relaciones
PeriodoAcademico (1) — (N) SubperiodoAcademico
PeriodoAcademico (1) — (N) Curso

---

## 8. SUBPERIODO_ACADEMICO
Subdivisión del Periodo Académico (ej. semestre), unidad sobre la que se organizan las evaluaciones y el cierre de notas. Fase 1, sección 3.17 corregida.

| Atributo | Propósito |
|---|---|
| subperiodo_academico_id (PK) | Identificador del subperiodo |
| periodo_academico_id (FK) | Periodo académico al que pertenece |
| nombre | Ej. "Primer semestre" |
| fecha_inicio | Inicio del subperiodo |
| fecha_termino | Término del subperiodo |
| estado | Abierto / Cerrado |

### Claves foráneas
- periodo_academico_id → PERIODO_ACADEMICO

### Relaciones
SubperiodoAcademico (N) — (1) PeriodoAcademico
SubperiodoAcademico (1) — (N) Evaluación

---

## 9. CURSO
Instancia académica concreta: Nivel + paralelo + Periodo. Se recrea cada periodo académico (Fase 1, sección 3.6).

| Atributo | Propósito |
|---|---|
| curso_id (PK) | Identificador del curso |
| nivel_educativo_id (FK) | Nivel educativo del curso |
| periodo_academico_id (FK) | Periodo académico al que pertenece esta instancia de curso |
| paralelo | Letra o identificador del paralelo (ej. "A") |
| profesor_jefe_persona_rol_id (FK) | Docente responsable como profesor jefe (referencia a PersonaRol con rol Docente) |

### Claves foráneas
- nivel_educativo_id → NIVEL_EDUCATIVO
- periodo_academico_id → PERIODO_ACADEMICO
- profesor_jefe_persona_rol_id → PERSONA_ROL

### Relaciones
Curso (N) — (1) NivelEducativo
Curso (N) — (1) PeriodoAcademico
Curso (1) — (N) Matrícula
Curso (1) — (N) AsignacionDocente

---

## 10. ASIGNATURA
Concepto curricular estable, independiente del periodo académico. Fase 1, sección 3.7.

| Atributo | Propósito |
|---|---|
| asignatura_id (PK) | Identificador de la asignatura |
| nombre | Nombre de la asignatura (ej. "Matemática") |

### Claves foráneas
Ninguna.

### Relaciones
Asignatura (1) — (N) AsignacionDocente

---

## 11. EVALUACION
Instrumento académico creado por un docente dentro de una Asignación Docente vigente. Fase 1, sección 3.10.

| Atributo | Propósito |
|---|---|
| evaluacion_id (PK) | Identificador de la evaluación |
| asignacion_docente_id (FK) | Asignación docente bajo la cual se crea |
| subperiodo_academico_id (FK) | Subperiodo al que pertenece |
| nombre | Nombre del instrumento (ej. "Prueba Unidad 1") |
| fecha | Fecha de aplicación |
| ponderacion | Peso relativo dentro del promedio de asignatura |

### Claves foráneas
- asignacion_docente_id → ASIGNACION_DOCENTE
- subperiodo_academico_id → SUBPERIODO_ACADEMICO

### Relaciones
Evaluación (N) — (1) AsignacionDocente
Evaluación (N) — (1) SubperiodoAcademico
Evaluación (1) — (N) Calificación

---

## 12. CALIFICACION
Resultado obtenido por un Estudiante en una Evaluación específica. Escala 1.0–7.0, aprobación 4.0 (Fase 1, sección 3.11).

| Atributo | Propósito |
|---|---|
| calificacion_id (PK) | Identificador de la calificación |
| evaluacion_id (FK) | Evaluación asociada |
| estudiante_id (FK) | Estudiante evaluado |
| resultado | Nota obtenida |

### Claves foráneas
- evaluacion_id → EVALUACION
- estudiante_id → ESTUDIANTE

### Relaciones
Calificación (N) — (1) Evaluación
Calificación (N) — (1) Estudiante
Calificación (1) — (N) SolicitudExcepcionCalificacion

---

## 13. ANOTACION
Registro histórico e inmutable asociado a un Estudiante. Categorías: positiva, negativa, académica; gravedad para negativas. Fase 1, sección 3.12.

| Atributo | Propósito |
|---|---|
| anotacion_id (PK) | Identificador de la anotación |
| estudiante_id (FK) | Estudiante al que refiere |
| autor_persona_rol_id (FK) | Rol activo bajo el cual la Persona registró la anotación |
| categoria | Positiva / Negativa / Académica |
| gravedad | Leve / Grave / Gravísima (aplica solo a categoría Negativa) |
| descripcion | Detalle del hecho registrado |
| fecha_creacion | Fecha de creación |
| estado | Vigente / Anulada |

### Claves foráneas
- estudiante_id → ESTUDIANTE
- autor_persona_rol_id → PERSONA_ROL

### Relaciones
Anotación (N) — (1) Estudiante
Anotación (N) — (1) PersonaRol (autor)

---

## 14. CONVERSACION
Contenedor de una conversación tipo chat entre participantes fijos. Fase 1, sección 3.13.

| Atributo | Propósito |
|---|---|
| conversacion_id (PK) | Identificador de la conversación |
| fecha_inicio | Fecha en que se inició la conversación |

### Claves foráneas
Ninguna directa (participantes vía tabla asociativa).

### Relaciones
Conversación (1) — (N) Mensaje
Conversación (N) — (M) Persona (vía ConversacionParticipante)
Conversación (1) — (N) SolicitudAccesoConversacion

---

## 15. MENSAJE
Envío individual dentro de una Conversación. Editable con historial de auditoría; sin eliminación física. Fase 1, sección 3.13.

| Atributo | Propósito |
|---|---|
| mensaje_id (PK) | Identificador del mensaje |
| conversacion_id (FK) | Conversación a la que pertenece |
| autor_persona_id (FK) | Persona autora del mensaje |
| fecha_envio | Fecha/hora de envío |
| contenido | Texto del mensaje |
| fecha_ultima_modificacion | Fecha de la última edición, si existe (nulo si no ha sido editado) |

### Claves foráneas
- conversacion_id → CONVERSACION
- autor_persona_id → PERSONA

### Relaciones
Mensaje (N) — (1) Conversación
Mensaje (N) — (1) Persona (autor)

---

## 16. COMUNICADO
Comunicación institucional unidireccional hacia un grupo (curso, nivel o establecimiento). Fase 1, sección 3.14 — emisores y alcance quedaron como pendiente no bloqueante (#10); estructura mínima formalizada aquí.

| Atributo | Propósito |
|---|---|
| comunicado_id (PK) | Identificador del comunicado |
| emisor_persona_rol_id (FK) | Persona + Rol que emite el comunicado |
| tipo_alcance | Curso / Nivel / Establecimiento |
| curso_id (FK, nulo) | Curso destinatario, si tipo_alcance = Curso |
| nivel_educativo_id (FK, nulo) | Nivel destinatario, si tipo_alcance = Nivel |
| asunto | Título del comunicado |
| contenido | Cuerpo del comunicado |
| fecha_publicacion | Fecha de emisión |

### Claves foráneas
- emisor_persona_rol_id → PERSONA_ROL
- curso_id → CURSO (nulo si no aplica)
- nivel_educativo_id → NIVEL_EDUCATIVO (nulo si no aplica)

### Relaciones
Comunicado (N) — (1) PersonaRol (emisor)
Comunicado (N) — (0..1) Curso
Comunicado (N) — (0..1) NivelEducativo

---

## 17. NOTIFICACION
Concepto transversal generado por eventos de otros módulos, dirigido a los actores correspondientes. Fase 1, sección 3.15.

| Atributo | Propósito |
|---|---|
| notificacion_id (PK) | Identificador de la notificación |
| destinatario_persona_id (FK) | Persona que recibe la notificación |
| tipo_evento | Tipo de evento que originó la notificación (catálogo completo pendiente de definición) |
| entidad_origen_tipo | Discriminador del tipo de entidad que originó la notificación (referencia genérica, ver Decisión de transformación #5) |
| entidad_origen_id | Identificador de la entidad de origen (referencia genérica) |
| contenido | Texto de la notificación |
| estado | Enviada / Leída |
| fecha_creacion | Fecha de generación |
| fecha_lectura | Fecha en que fue leída (nulo si no ha sido leída) |

### Claves foráneas
- destinatario_persona_id → PERSONA
- (entidad_origen_tipo, entidad_origen_id): referencia genérica, sin FK declarativa — ver Decisión de transformación #5

### Relaciones
Notificación (N) — (1) Persona (destinatario)

---

## 18. REGISTRO_AUDITORIA
Mecanismo transversal de trazabilidad. Registra Persona y Rol activo bajo el cual se ejecutó la acción. Fase 1, sección 5.2.

| Atributo | Propósito |
|---|---|
| registro_auditoria_id (PK) | Identificador del registro |
| persona_id (FK) | Persona que ejecutó la acción |
| rol_activo_persona_rol_id (FK, nulo) | Rol bajo el cual actuó, si corresponde |
| fecha | Fecha/hora de la acción |
| accion | Tipo de acción ejecutada (creación, modificación, anulación, acceso, etc.) |
| entidad_afectada_tipo | Discriminador del tipo de entidad afectada (referencia genérica, ver Decisión de transformación #5) |
| entidad_afectada_id | Identificador de la entidad afectada (referencia genérica) |
| valor_anterior | Valor previo a la acción, cuando aplica |
| valor_nuevo | Valor posterior a la acción, cuando aplica |
| motivo | Motivo de la acción, cuando aplica |

### Claves foráneas
- persona_id → PERSONA
- rol_activo_persona_rol_id → PERSONA_ROL (nulo)
- (entidad_afectada_tipo, entidad_afectada_id): referencia genérica, sin FK declarativa — ver Decisión de transformación #5

### Relaciones
RegistroAuditoria (N) — (1) Persona
RegistroAuditoria (N) — (0..1) PersonaRol

---

## 19. EVENTO_INSTITUCIONAL
Actividad institucional o día no lectivo, proyectado en el Calendario. Fase 1, sección 3.19.

| Atributo | Propósito |
|---|---|
| evento_institucional_id (PK) | Identificador del evento |
| tipo | Actividad institucional / Día no lectivo |
| fecha | Fecha del evento |
| descripcion | Descripción del evento |

### Claves foráneas
Ninguna.

### Relaciones
Ninguna relación directa a nivel de tabla — se consume desde la vista Calendario (proyección, no entidad).

---

## 20. REUNION
Entidad propia con catálogo cerrado de 5 tipos. Fase 1, sección 3.20.

| Atributo | Propósito |
|---|---|
| reunion_id (PK) | Identificador de la reunión |
| tipo | Apoderado-Docente / Curso / Institucional / Consejo de Profesores / Convivencia-Disciplinaria |
| fecha | Fecha de la reunión |
| hora_inicio | Hora de inicio |
| hora_termino | Hora de término |
| convocante_persona_rol_id (FK) | Persona + Rol que convoca |
| estado | Programada / Realizada / Cancelada |
| lugar_modalidad | Lugar físico o modalidad (presencial/virtual), si aplica |

### Claves foráneas
- convocante_persona_rol_id → PERSONA_ROL

### Relaciones
Reunión (N) — (1) PersonaRol (convocante)
Reunión (N) — (M) Persona (vía ReunionParticipante)

---

## 21. PERSONA_ROL *(asociativa)*
Resuelve la relación N:M entre Persona y Rol. Representa una asignación de rol concreta a una Persona, con vigencia propia — es el punto de referencia para autorización y auditoría (Fase 1, sección 2 y 5.1).

| Atributo | Propósito |
|---|---|
| persona_rol_id (PK) | Identificador de la asignación de rol |
| persona_id (FK) | Persona titular del rol |
| rol_id (FK) | Tipo de rol asignado |
| fecha_inicio | Fecha desde la cual el rol está activo |
| fecha_termino | Fecha de término del rol, si aplica |
| estado | Activo / Inactivo |

### Claves foráneas
- persona_id → PERSONA
- rol_id → ROL

### Relaciones
PersonaRol (N) — (1) Persona
PersonaRol (N) — (1) Rol
PersonaRol (1) — (0..1) Estudiante (si rol = Estudiante)
PersonaRol (1) — (0..1) Apoderado (si rol = Apoderado)
PersonaRol (1) — (N) AsignacionDocente (si rol = Docente)
PersonaRol (1) — (N) Curso (como profesor jefe, si rol = Docente)
PersonaRol (1) — (N) RegistroAuditoria (como rol activo)

---

## 22. MATRICULA *(asociativa)*
Vincula Estudiante + Curso dentro de un Periodo Académico (implícito vía Curso). Múltiples registros históricos por estudiante; solo una activa a la vez (Fase 1, sección 3.4).

| Atributo | Propósito |
|---|---|
| matricula_id (PK) | Identificador de la matrícula |
| estudiante_id (FK) | Estudiante matriculado |
| curso_id (FK) | Curso asociado (el periodo se obtiene vía Curso — ver Decisión de transformación #3) |
| estado | Activa / Retirada / Finalizada |
| motivo | Motivo del estado actual (ver Decisión de transformación #4) |
| fecha_inicio_vigencia | Fecha desde la cual la matrícula está vigente |
| fecha_termino_vigencia | Fecha de término de vigencia, si aplica |

### Claves foráneas
- estudiante_id → ESTUDIANTE
- curso_id → CURSO

### Relaciones
Matrícula (N) — (1) Estudiante
Matrícula (N) — (1) Curso

---

## 23. ASIGNACION_DOCENTE *(asociativa)*
Vincula Docente + Asignatura + Curso, con vigencia temporal propia (Fase 1, sección 3.8).

| Atributo | Propósito |
|---|---|
| asignacion_docente_id (PK) | Identificador de la asignación |
| docente_persona_rol_id (FK) | Docente responsable (referencia a PersonaRol con rol Docente) |
| asignatura_id (FK) | Asignatura dictada |
| curso_id (FK) | Curso en el que se dicta (el periodo se obtiene vía Curso — ver Decisión de transformación #3) |
| fecha_inicio_vigencia | Inicio de la vigencia de esta asignación |
| fecha_termino_vigencia | Término de vigencia, si aplica (permite reemplazos) |
| estado | Vigente / No vigente |

### Claves foráneas
- docente_persona_rol_id → PERSONA_ROL
- asignatura_id → ASIGNATURA
- curso_id → CURSO

### Relaciones
AsignacionDocente (N) — (1) PersonaRol (docente)
AsignacionDocente (N) — (1) Asignatura
AsignacionDocente (N) — (1) Curso
AsignacionDocente (1) — (N) Evaluación

---

## 24. APODERADO_ESTUDIANTE *(asociativa)*
Resuelve la relación N:M entre Apoderado y Estudiante, con tipo de relación (Fase 1, sección 3.3).

| Atributo | Propósito |
|---|---|
| apoderado_estudiante_id (PK) | Identificador de la relación |
| apoderado_id (FK) | Apoderado |
| estudiante_id (FK) | Estudiante asociado |
| tipo_relacion | Principal / Secundario |

### Claves foráneas
- apoderado_id → APODERADO
- estudiante_id → ESTUDIANTE

### Relaciones
ApoderadoEstudiante (N) — (1) Apoderado
ApoderadoEstudiante (N) — (1) Estudiante

---

## 25. CONVERSACION_PARTICIPANTE *(asociativa)*
Resuelve la relación N:M entre Conversación y Persona (Fase 1, sección 3.13).

| Atributo | Propósito |
|---|---|
| conversacion_participante_id (PK) | Identificador del registro de participación |
| conversacion_id (FK) | Conversación |
| persona_id (FK) | Persona participante |
| fecha_incorporacion | Fecha en que se incorporó a la conversación |

### Claves foráneas
- conversacion_id → CONVERSACION
- persona_id → PERSONA

### Relaciones
ConversacionParticipante (N) — (1) Conversación
ConversacionParticipante (N) — (1) Persona

---

## 26. REUNION_PARTICIPANTE *(asociativa)*
Resuelve la relación N:M entre Reunión y Persona (audiencia esperada, sin registro de asistencia real en v1 — Fase 1, sección 3.20).

| Atributo | Propósito |
|---|---|
| reunion_participante_id (PK) | Identificador del registro |
| reunion_id (FK) | Reunión |
| persona_id (FK) | Persona convocada |

### Claves foráneas
- reunion_id → REUNION
- persona_id → PERSONA

### Relaciones
ReunionParticipante (N) — (1) Reunión
ReunionParticipante (N) — (1) Persona

---

## 27. SOLICITUD_EXCEPCION_CALIFICACION *(asociativa)*
Flujo formal de excepción para modificar una Calificación tras el cierre del Subperiodo (Fase 1, sección 3.11.1).

| Atributo | Propósito |
|---|---|
| solicitud_excepcion_calificacion_id (PK) | Identificador de la solicitud |
| calificacion_id (FK) | Calificación afectada (alcance puntual) |
| solicitante_persona_rol_id (FK) | Docente que solicita |
| aprobador_persona_rol_id (FK) | Directivo que aprueba/rechaza |
| motivo | Motivo de la solicitud |
| estado | Pendiente / Aprobada / Rechazada / Ejecutada |
| fecha_solicitud | Fecha de creación de la solicitud |
| fecha_resolucion | Fecha de aprobación/rechazo |

### Claves foráneas
- calificacion_id → CALIFICACION
- solicitante_persona_rol_id → PERSONA_ROL
- aprobador_persona_rol_id → PERSONA_ROL

### Relaciones
SolicitudExcepcionCalificacion (N) — (1) Calificación
SolicitudExcepcionCalificacion (N) — (1) PersonaRol (solicitante)
SolicitudExcepcionCalificacion (N) — (1) PersonaRol (aprobador)

---

## 28. SOLICITUD_ACCESO_CONVERSACION *(asociativa)*
Flujo formal de autorización excepcional para acceder a una Conversación privada (Fase 1, sección 3.16).

| Atributo | Propósito |
|---|---|
| solicitud_acceso_conversacion_id (PK) | Identificador de la solicitud |
| conversacion_id (FK) | Conversación afectada (alcance puntual) |
| solicitante_persona_rol_id (FK) | Inspector o Directivo que solicita |
| aprobador_persona_rol_id (FK) | Directivo que aprueba (puede coincidir con el solicitante solo en la excepción de directivo único — ver Fase 1, sección 3.16) |
| motivo | Motivo de la solicitud |
| estado | Pendiente / Aprobada / Rechazada / Ejecutada |
| fecha_solicitud | Fecha de creación de la solicitud |
| fecha_resolucion | Fecha de aprobación/rechazo |

### Claves foráneas
- conversacion_id → CONVERSACION
- solicitante_persona_rol_id → PERSONA_ROL
- aprobador_persona_rol_id → PERSONA_ROL

### Relaciones
SolicitudAccesoConversacion (N) — (1) Conversación
SolicitudAccesoConversacion (N) — (1) PersonaRol (solicitante)
SolicitudAccesoConversacion (N) — (1) PersonaRol (aprobador)

---

## Consideraciones importantes

Decisiones de dominio que este modelo lógico preserva explícitamente, sin reinterpretarlas:

- **Persona + Rol activo, no "Actor" genérico.** Toda referencia a "quién realiza una acción" en el dominio se resuelve mediante `PERSONA` (identidad) y, cuando el rol importa, `PERSONA_ROL` (rol activo) — nunca mediante una entidad "Actor" abstracta.
- **Autorización basada en contexto**, no en el rol aislado. El modelo lógico no incluye una tabla de "permisos" — la autorización se deriva, en tiempo de ejecución, de las relaciones ya modeladas (`ASIGNACION_DOCENTE`, `CURSO.profesor_jefe_persona_rol_id`, `APODERADO_ESTUDIANTE`), consistente con la decisión de la Fase 1 de no duplicar relaciones de contexto en una tabla de permisos aparte.
- **RegistroAuditoria asociado a Persona y Rol activo**, nunca a un "Actor" ni solo al Rol sin identidad.
- **SolicitudExcepcionCalificacion** como entidad formal con ciclo de vida propio (Pendiente → Aprobada/Rechazada → Ejecutada), no como una simple reapertura de Subperiodo.
- **SolicitudAccesoConversacion** como entidad formal con aprobación, preservando que ningún rol tiene acceso libre a conversaciones privadas ajenas.
- **Calendario como proyección**, no como entidad — no existe tabla `CALENDARIO` en este modelo.
- **Reunión como entidad propia**, con catálogo cerrado de 5 tipos y sin registro de asistencia real en esta versión.
- **EventoInstitucional como entidad propia**, necesaria para que Actividad Institucional y Día No Lectivo puedan proyectarse en el Calendario sin romper el principio de que Calendario no almacena datos propios.

---

# Verificación de Formas Normales

## Primera Forma Normal (1FN)
**Requisito:** atributos atómicos, sin grupos repetitivos ni valores multivaluados en una misma fila.

**Cumplimiento:** el caso más relevante del dominio es que una Persona puede tener múltiples roles. Si `PERSONA` almacenara los roles como una lista o columna repetida (ej. `roles = "Docente, Apoderado"`), se violaría 1FN. Esto se resuelve mediante la entidad asociativa `PERSONA_ROL`, donde cada rol de una Persona es una **fila independiente**, no un valor compuesto dentro de una columna.

Otro ejemplo: `ANOTACION.gravedad` almacena un único valor atómico (Leve/Grave/Gravísima) por fila — no se permite que una misma Anotación tenga múltiples valores de gravedad simultáneos.

## Segunda Forma Normal (2FN)
**Requisito:** cumplir 1FN, y que todo atributo no-clave dependa de la **clave primaria completa** (relevante en tablas con clave compuesta; no aplica si la PK es de un solo atributo).

**Cumplimiento:** todas las entidades de este modelo usan **claves primarias sustitutas de un solo atributo** (`*_id`), incluidas las asociativas (ej. `apoderado_estudiante_id`, no una clave compuesta `(apoderado_id, estudiante_id)`). Esta decisión de diseño evita por construcción el riesgo de dependencias parciales: en `APODERADO_ESTUDIANTE`, el atributo `tipo_relacion` depende del par completo (Apoderado, Estudiante), no de uno solo — al usar una PK sustituta, esa dependencia queda expresada correctamente sin ambigüedad, y el par `(apoderado_id, estudiante_id)` se mantiene como restricción de unicidad a nivel de Modelo Físico, no como clave primaria.

## Tercera Forma Normal (3FN)
**Requisito:** cumplir 2FN, y que ningún atributo no-clave dependa transitivamente de otro atributo no-clave (solo debe depender de la clave primaria).

**Cumplimiento — ejemplo principal (ya documentado en Decisiones de transformación #3):** `ASIGNACION_DOCENTE` no almacena `periodo_academico_id`. Si lo hiciera, ese atributo dependería de `curso_id` (un atributo no-clave de la propia tabla), y `curso_id` a su vez ya determina el periodo vía `CURSO.periodo_academico_id` — una dependencia transitiva clásica (`asignacion_docente_id → curso_id → periodo_academico_id`). Se elimina la columna redundante y el periodo se obtiene por join a través de `CURSO`. El mismo razonamiento se aplicó a `MATRICULA`.

**Otro ejemplo:** `CURSO` almacena `nivel_educativo_id`, pero **no** almacena el nombre del nivel educativo (ej. "Educación Media") directamente en la tabla `CURSO`. Ese nombre depende de `nivel_educativo_id`, no de `curso_id` — se obtiene vía join a `NIVEL_EDUCATIVO`, evitando una dependencia transitiva y la posibilidad de que el nombre del nivel quede inconsistente entre distintas filas de `CURSO`.

---

# Revisión crítica del Modelo Lógico

## Mejoras posibles
- **Auditoría y Notificación con referencia genérica (Decisión de transformación #5)** funcionan a nivel lógico, pero en el Modelo Físico convendrá evaluar mecanismos que refuercen la integridad (ej. triggers de validación, o una tabla de metadatos de entidades válidas) para reducir el riesgo de registros huérfanos.
- **Catálogo de eventos de Notificación** (pendiente #12 del dominio) puede evolucionar sin alterar la estructura de la tabla `NOTIFICACION` — solo se amplía el conjunto de valores válidos de `tipo_evento`.
- **Subdivisión futura de Personal Administrativo en sub-roles** (pendiente #13 del dominio) es absorbible sin cambios estructurales: bastaría con agregar nuevas filas al catálogo `ROL` y ajustar la capa de autorización en la aplicación, sin tocar `PERSONA_ROL` ni ninguna otra tabla.
- **Formalización futura de "Establecimiento"** (pendiente #14) podría convertir las columnas nulas de `COMUNICADO` (alcance institucional) en una referencia real, sin romper compatibilidad si se diseña como columna adicional opcional.

## Riesgos de diseño
- **Referencias polimórficas sin integridad declarativa** (`NOTIFICACION` y `REGISTRO_AUDITORIA`) son el punto de mayor riesgo estructural del modelo: nada a nivel de base de datos impide que `entidad_afectada_id` apunte a un registro inexistente. Este riesgo se acepta conscientemente en el modelo lógico y debe resolverse explícitamente en el Modelo Físico o en la capa de aplicación.
- **Estados y transiciones aún no completamente cerrados en el dominio** (ej. si Retirada es terminal independiente de Finalizada en `MATRICULA` — pendiente #17) no bloquean la estructura de columnas, pero sí quedan pendientes las reglas de transición válida, que deberán implementarse como lógica de aplicación, no como restricción de base de datos en este nivel.
- **`ASIGNACION_DOCENTE.docente_persona_rol_id` y similares no garantizan, a nivel de base de datos, que el `PERSONA_ROL` referenciado tenga efectivamente `rol = Docente`** (o Directivo, Inspector, según el caso). Esta validación de "tipo de rol correcto" es un candidato claro para restricción a nivel de Modelo Físico (constraint o validación de aplicación).

## Escalabilidad
- El modelo está diseñado para **un único establecimiento** (consistente con el alcance confirmado en Fase 1.1). Si en el futuro se requiere soporte multi-establecimiento, la mayoría de las entidades (Curso, Periodo Académico, Persona) necesitarían una columna de referencia a `Establecimiento` — el diseño actual, al no tener esa columna, requeriría una migración estructural, no solo una extensión.
- El patrón de "Solicitud con flujo de aprobación" (`SOLICITUD_EXCEPCION_CALIFICACION`, `SOLICITUD_ACCESO_CONVERSACION`) es reutilizable: si aparecen nuevas necesidades de excepción controlada en el futuro (ej. corrección de asistencia, si se incorpora), el mismo patrón estructural puede replicarse sin rediseñar el enfoque general.
- La separación `PERSONA` / `PERSONA_ROL` permite agregar nuevos tipos de rol sin alterar la tabla `PERSONA`, lo que favorece la evolución del catálogo de actores del establecimiento sin migraciones mayores.

## Consistencia del modelo
- Las 28 entidades identificadas cubren la totalidad de los conceptos cerrados en la Fase 1 del análisis de dominio; no se introdujeron entidades de negocio nuevas más allá de las estrictamente necesarias para resolver relaciones N:M (`PERSONA_ROL`, `APODERADO_ESTUDIANTE`, `CONVERSACION_PARTICIPANTE`, `REUNION_PARTICIPANTE`), consistente con la instrucción de no ampliar el alcance del dominio en esta etapa.
- El patrón "excepción con flujo de aprobación formal" se aplica de manera consistente en `SOLICITUD_EXCEPCION_CALIFICACION` y `SOLICITUD_ACCESO_CONVERSACION`, reflejando que ambas decisiones de dominio (cerradas en momentos distintos del análisis) convergieron en la misma solución estructural — es una señal de coherencia del dominio, no una coincidencia forzada por este modelo.
- Persisten pendientes no bloqueantes del dominio (transiciones de Matrícula, granularidad exacta del cierre académico, catálogo de eventos de Notificación) que **no impiden** este modelo lógico, pero que deberán resolverse antes o durante el Modelo Físico para definir restricciones y lógica de validación con precisión.

---

# Preparación para futuras etapas

Este modelo lógico deja preparado lo siguiente:

- **Modelo Físico:** las 28 entidades, sus atributos y relaciones están listas para recibir tipos de dato específicos de motor, índices, restricciones de integridad (incluyendo las validaciones de "tipo de rol correcto" señaladas como riesgo), y decisión sobre el mecanismo de referencia genérica de `NOTIFICACION`/`REGISTRO_AUDITORIA`.
- **Diseño de API:** cada entidad principal y asociativa constituye un candidato natural de recurso o sub-recurso; las relaciones N:1/N:M ya definidas anticipan los endpoints de navegación necesarios (ej. estudiante → matrículas, curso → asignaciones docentes).
- **Seguridad/autorización:** la capa de autorización del dominio (Fase 1, sección 5.1) ya identifica exactamente qué relaciones de contexto deben consultarse (`ASIGNACION_DOCENTE`, `CURSO.profesor_jefe_persona_rol_id`, `APODERADO_ESTUDIANTE`) para resolver permisos en tiempo de ejecución — este modelo lógico expone esas relaciones de forma directa y consultable.
- **Implementación:** el documento sirve como referencia única para la construcción de scripts de migración, entidades de persistencia (ORM) y pruebas de integridad de datos, una vez completado el Modelo Físico.

**Pendiente explícito antes de avanzar al Modelo Físico:** resolver, o al menos acotar, los riesgos de diseño señalados arriba (integridad de referencias polimórficas, validación de tipo de rol en claves foráneas hacia `PERSONA_ROL`), ya que ahí sí se traducen en decisiones concretas de restricciones de motor.
