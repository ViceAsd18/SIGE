# SIGE — Documento Consolidado del Dominio (Fase 1)

**Estado:** Documento vivo. Cada fase aprobada actualiza este documento. Las decisiones aquí registradas no se reabren salvo nueva evidencia que lo justifique. **Fase 1 (Análisis del Dominio) completa — todos los bloqueantes resueltos.**
**Alcance de este documento:** análisis de dominio. No contiene modelo de clases, esquema de base de datos ni decisiones de arquitectura técnica.

---

## 1. Propósito y visión general

El SIGE es la plataforma destinada a ser la **fuente oficial única** de información académica y administrativa de un establecimiento educacional, reemplazando registros manuales/físicos actualmente dispersos. Sus objetivos de negocio son: centralizar la información, garantizar trazabilidad, reducir errores y duplicidad, y mejorar la comunicación entre los actores del establecimiento.

**Alcance inicial confirmado:**
- Un único establecimiento educacional (sin arquitectura multi-tenant en esta versión).
- El sistema parte desde "estudiante ya matriculado" — no incluye proceso de admisión/postulación.
- No se gestionan decisiones automáticas de promoción/repitencia.
- No se gestionan procesos disciplinarios completos (suspensiones, condicionalidades), solo el registro de anotaciones.
- No se gestiona disponibilidad horaria para agendamiento (pendiente de confirmar en Fase 1.8).

---

## 2. Identidad y roles del sistema

**Decisión confirmada (resolución del bloqueante #5):** una misma persona física puede desempeñar más de un rol simultáneamente en el establecimiento (ej. un docente que es apoderado de un estudiante del mismo colegio, o un funcionario con responsabilidades adicionales). Por lo tanto, el dominio adopta un modelo de **identidad única (Persona) con uno o más Roles asociados**, en vez de tratar cada actor como una entidad completamente independiente.

```
Persona (identidad común)
  datos personales, credenciales, trazabilidad
   ├── Rol: Estudiante
   ├── Rol: Apoderado        → relación con Estudiante
   ├── Rol: Docente          → asignaciones docentes
   ├── Rol: Profesor Jefe    → responsabilidad sobre Curso (no es un rol separado del rol Docente; es una responsabilidad adicional sobre él — ver 3.6)
   ├── Rol: Inspector        → alcance transversal de convivencia
   ├── Rol: Administrativo   → alcance transversal administrativo
   └── Rol: Directivo        → alcance transversal institucional
```

**Separación de responsabilidades entre Persona y Rol:**
- **Persona** contiene la identidad común: datos personales de identificación/contacto, credenciales de acceso, y es el punto de referencia para la trazabilidad de acciones (quién hizo qué, independientemente del rol bajo el cual actuó).
- **Rol** contiene las responsabilidades y relaciones propias de cada función dentro del dominio, ya definidas en las fases anteriores (ej. la relación N:M Apoderado↔Estudiante pertenece al rol Apoderado, no a la Persona en general).

Una misma Persona puede tener uno o más Roles activos, simultáneos o a lo largo del tiempo.

| Rol | Responsabilidad / relación propia |
|---|---|
| Estudiante | Sujeto principal del dominio. Puede consultar su información y responder mensajes, no iniciarlos. |
| Apoderado | Relación N:M con Estudiante. Existen tipos: principal y secundario (diferencia de permisos pendiente). |
| Docente | Imparte asignaturas en cursos mediante Asignación Docente. |
| Profesor Jefe | Responsabilidad adicional sobre un Curso, independiente de la asignación de asignaturas (no es un rol formalmente distinto de Docente — ver 3.6). |
| Inspector | Responsable de convivencia escolar, seguimiento conductual y anotaciones disciplinarias, con alcance transversal. |
| Administrativo | Responsable de procesos administrativos como matrícula (sin subdivisión formal aún). |
| Directivo | Acceso transversal, supervisión, reportes consolidados, gestión institucional. |

**Regla transversal confirmada:** la privacidad de la información del estudiante es crítica. Ningún rol accede a información fuera de su ámbito autorizado. Las relaciones estudiante–apoderado requieren control de permisos. Toda acción relevante debe mantener trazabilidad.

**Implicancia aceptada explícitamente:** se acepta la mayor complejidad de este modelo (frente a la alternativa de actores independientes) porque representa una necesidad real del dominio, y porque introducir esta identidad unificada más adelante, sobre un sistema ya construido con actores independientes, sería un cambio estructural mucho más costoso que resolverlo ahora en el análisis.

**Consecuencia sobre la capa de autorización:** dado que una Persona puede tener múltiples roles, la autorización no puede depender solo del rol en abstracto — debe considerar el **rol activo bajo el cual la persona está actuando, más el contexto de relación existente** (ver sección 5.1). Esto también deja planteada, como posible extensión futura y no como alcance confirmado, la pregunta de si el sistema debería advertir o restringir situaciones de conflicto de interés (ej. un docente evaluando o registrando anotaciones sobre un estudiante del cual es apoderado) — **no se asume como parte del alcance inicial**, se deja registrada para consideración posterior.

---

## 3. Entidades y conceptos por módulo

### 3.1 Estudiante
Representa la **identidad** de la persona dentro del sistema. Su existencia es independiente de si está matriculado actualmente. Conserva historial permanentemente, incluso tras retirarse del establecimiento.

**Contiene:** datos de identificación y contacto propios del estudiante.
**No contiene:** historial académico (ver 3.9), ni datos sensibles de salud/NEE directamente (ver 3.2).

### 3.2 Información sensible del estudiante
Concepto separado de la información personal general, con permisos de acceso propios y más restrictivos.

```
Estudiante
 ├── InformaciónPersonal   (datos de identificación y contacto)
 └── InformaciónSensible   (salud, NEE, observaciones médicas)
```

*Motivo de la separación:* evitar que datos de salud de un menor queden expuestos junto con datos básicos por defecto; requieren control de acceso independiente.

### 3.3 Apoderado
Relación N:M con Estudiante. Existen tipos de relación: **principal** y **secundario**. **Resuelto para v1 (bloqueante de autorización):** ambos tipos tienen el mismo nivel de acceso a la información del estudiante asociado (académica, conductual e Información Sensible) — la diferencia entre principal y secundario queda diferida a una fase futura de permisos administrativos específicos, no afecta el acceso a información en esta versión.

### 3.4 Matrícula
Entidad núcleo del dominio. Representa la vinculación de un Estudiante a un Curso dentro de un Periodo Académico.

**Regla definitiva (corregida tras revisión arquitectónica):**
> Un estudiante puede tener múltiples registros históricos de matrícula dentro del mismo periodo académico, pero solo una matrícula activa a la vez. Un cambio de curso implica finalizar la matrícula vigente y crear una nueva; nunca se sobrescribe.

**Corrección aplicada (revisión final):** el `Estado` de Matrícula se mantiene como enum simple (Activa, Retirada, Finalizada), **sin mezclar el motivo del cambio dentro del valor del estado**. El motivo (ej. cambio de curso, cierre académico normal, retiro del estudiante) se registra por separado, mediante el mecanismo transversal de auditoría (sección 5.2) o un atributo `motivo` propio de la matrícula — la elección exacta entre ambos mecanismos queda como detalle de modelo formal, no de dominio.

Además, Matrícula incorpora **fechas de vigencia explícitas** (`fecha inicio vigencia`, `fecha término vigencia`), por consistencia con Asignación Docente y para soportar consultas históricas del tipo "¿en qué curso estaba el estudiante en una fecha determinada?".

```
Matrícula 1:
  Periodo: 2026 — Curso: 1° Medio A
  Estado: Finalizada
  Vigencia: 01-03-2026 a 20-05-2026
  Motivo (vía auditoría): cambio de curso

Matrícula 2:
  Periodo: 2026 — Curso: 1° Medio B
  Estado: Activa
  Vigencia: 21-05-2026 — (sin fecha término, vigente)
```

**Estados confirmados:** Activa, Retirada, Finalizada. (Transiciones válidas entre estados: **pendiente**, ver sección 6). **Pendiente adicional (revisión final):** definir si Retirada es un estado terminal independiente de Finalizada, o si el retiro es simplemente un motivo particular de Finalizada (ver sección 6, pendiente #11).

### 3.5 Nivel Educativo
Entidad propia de dominio (ej. Educación Básica, Educación Media). Relación jerárquica con Curso.

```
NivelEducativo
   └── Curso
```

### 3.6 Curso
Instancia académica concreta: Nivel Educativo + paralelo + Periodo Académico. Se **recrea cada periodo académico** (no "avanza" de nivel); el historial del estudiante se reconstruye vía sus Matrículas. Tiene un Profesor Jefe principal (único), cuya jefatura es independiente de que dicte o no una asignatura en ese curso. Sin límite de capacidad en esta versión.

### 3.7 Asignatura
Concepto curricular estable, independiente del periodo académico (ej. "Matemática").

### 3.8 Asignación Docente
Entidad intermedia que vincula Docente + Asignatura + Curso + Periodo, **con vigencia temporal** (corrección aplicada tras revisión), lo que permite reemplazos durante el año sin perder historial.

```
AsignaciónDocente:
  Docente, Asignatura, Curso, Periodo
  Fecha inicio vigencia, Fecha término vigencia, Estado
```

Sin co-docencia en esta versión (un docente principal por asignación).

### 3.9 Historial académico (proyección, no entidad)
**No es un atributo ni estructura interna de Estudiante.** Es una vista/proyección derivada de: Matrículas, Cursos, Asignaciones Académicas, Evaluaciones, Calificaciones y Anotaciones.

### 3.10 Evaluación
Instrumento académico creado por un docente (prueba, trabajo, etc.), asociado a una Asignación Docente. Tiene ponderación y fecha, y pertenece a un Subperiodo Académico.

### 3.11 Calificación
Resultado obtenido por un Estudiante en una Evaluación específica. Escala: **1.0 a 7.0**, nota mínima de aprobación **4.0** (parametrizable). El promedio de asignatura se calcula según ponderaciones definidas por evaluación. El promedio general del estudiante no es responsabilidad de este módulo (corresponde a Reportes).

**Ciclo de vida:** el docente responsable de la Asignación puede crear evaluaciones y registrar/modificar calificaciones mientras el Subperiodo esté **abierto**. Tras el cierre, la modificación requiere una **Solicitud de Excepción de Calificación** (ver 3.11.1), aprobada por el rol Directivo.

### 3.11.1 Solicitud de Excepción de Calificación
**Bloqueante resuelto.** Entidad propia del dominio, con flujo de aprobación formal — decisión adoptada por consistencia con el principio de que el SIGE es la fuente oficial única de información académica (Fase 1.1): la modificación de una calificación cerrada no debe depender de canales externos al sistema (correo, conversación verbal).

```
SolicitudExcepciónCalificación:
  Calificación afectada (referencia puntual)
  Solicitante: Persona + Rol Docente
  Aprobador: Persona + Rol Directivo
  Motivo
  Estado
  Fecha solicitud / Fecha resolución
```

**Alcance puntual (decisión confirmada):** la excepción habilita la modificación de **una Calificación específica**, no una reapertura del Subperiodo completo — minimiza la ventana de modificación y evita que otras calificaciones del mismo Subperiodo queden editables sin necesidad.

**Regla de negocio confirmada:**
> La aprobación de una Solicitud de Excepción no modifica automáticamente la Calificación. Solo autoriza al Docente responsable a realizar la modificación puntual, la cual queda registrada mediante el mecanismo transversal de auditoría (sección 5.2).

**Relación con mecanismos transversales:**
- **RegistroAuditoria** registra la modificación efectiva de la Calificación una vez ejecutada — es un mecanismo distinto y complementario a la Solicitud (la Solicitud gestiona la aprobación previa; la Auditoría registra el hecho consumado).
- **Notificación** se dispara al menos en dos momentos: cuando se crea la Solicitud (para alertar al Directivo) y cuando se resuelve (para informar al Docente solicitante).

### 3.12 Anotación
Registro histórico e inmutable (no se elimina físicamente) asociado a un Estudiante, con autor, fecha, categoría, descripción y estado.

**Categorías confirmadas:** positiva, negativa, académica. Las negativas tienen **gravedad**: leve, grave, gravísima.

**EstadoAnotación:** Vigente, Anulada. No hay eliminación física; toda corrección se realiza mediante modificación auditada o anulación lógica.

**Hoja de vida:** no es una entidad independiente; es una vista construida a partir del historial de Anotaciones del estudiante.

### 3.13 Mensajería — Conversación y Mensaje
Modelo tipo chat (no correo independiente):

```
Conversación: participantes, fecha de inicio
Mensaje: conversación (ref), autor, fecha, contenido
```

**Reglas de inicio de contacto confirmadas:**

| Desde | Hacia | Permitido |
|---|---|---|
| Docente | Apoderado | Sí |
| Apoderado | Docente | Sí |
| Estudiante | — | Solo responder, no iniciar |
| Apoderado | Apoderado | No |

Sin conversaciones grupales en esta versión (evolución futura). Mensajes editables con historial de auditoría; no hay eliminación física.

### 3.14 Comunicado
Concepto separado de la mensajería directa: comunicación institucional unidireccional hacia un grupo (curso, nivel, establecimiento). Emisores autorizados y alcance: **pendiente**.

### 3.15 Notificación
Concepto **transversal** del dominio (no exclusivo de mensajería). Se genera a partir de eventos ocurridos en otros módulos (ej. nueva anotación, nueva evaluación, reunión agendada) y se dirige a los actores correspondientes.

### 3.16 Acceso Excepcional a Conversaciones (Auditoría de mensajería)
**Bloqueante resuelto.** Ningún rol tiene acceso libre a conversaciones privadas fuera de aquellas donde participa directamente — esto aplica incluso a Directivo e Inspector, consistente con el principio ya cerrado en la capa de autorización (sección 5.1): alcance institucional no implica acceso automático a información privada.

**Roles que pueden solicitar acceso excepcional, según su ámbito:**
- **Inspector** — situaciones de convivencia escolar, investigaciones internas o seguimiento de casos.
- **Directivo** — reclamos formales, supervisión institucional, requerimientos administrativos o legales.

**Regla de aprobación:**
- Si **Inspector** solicita → aprueba **Directivo**.
- Si **Directivo** solicita → aprueba **otro integrante del equipo directivo con la misma facultad**, si existe.
- **Excepción aceptada:** si el establecimiento tiene un único integrante directivo con esta responsabilidad, el propio Directivo puede autorizar su propia solicitud, pero con controles reforzados obligatorios: motivo obligatorio, registro previo de la solicitud, y auditoría completa del acceso realizado. **No se considera acceso libre ni automático** — la diferencia con la Alternativa A original es que siempre queda una solicitud formal y motivada antes del acceso, no una facultad discrecional sin registro previo.

**Flujo confirmado (control previo obligatorio — la auditoría posterior no reemplaza la autorización previa):**
```
SolicitudAccesoConversación
   ↓
Aprobación / autorización excepcional
   ↓
Acceso puntual a la conversación
   ↓
RegistroAuditoria del acceso
```

**Entidad formal:**
```
SolicitudAccesoConversación:
  Conversación afectada (referencia puntual)
  Solicitante: Persona + Rol (Inspector o Directivo)
  Aprobador: Persona + Rol (cuando corresponda)
  Motivo
  Estado: Pendiente → Aprobada → Ejecutada / Pendiente → Rechazada
  Fecha solicitud / Fecha resolución
```

**Regla de negocio confirmada:**
> La autorización es puntual sobre una conversación específica. No habilita acceso general a todas las conversaciones de una Persona ni del establecimiento.

Mismo patrón estructural que `SolicitudExcepciónCalificación` (sección 3.11.1) — el dominio consolida así un patrón reutilizable de "excepción con aprobación formal" para accesos/modificaciones sensibles fuera del flujo normal.

### 3.17 Periodo Académico
Entidad de primera clase (corrección aplicada). Contiene: nombre/año, fecha de inicio, fecha de término, estado (abierto/cerrado).

```
PeriodoAcadémico
   Estado: abierto/cerrado
   └── SubPeriodoAcadémico   (ej. Primer semestre, Segundo semestre)
          Estado: abierto/cerrado
```

Las notas se organizan por subperiodo. **Corrección aplicada (revisión final):** Subperiodo Académico también tiene su propio estado abierto/cerrado, ya que el cierre académico que afecta a Evaluación/Calificación (sección 3.11) probablemente ocurre a este nivel, no solo a nivel de Periodo completo. **Queda pendiente** la decisión final sobre si el cierre ocurre exclusivamente a nivel de Subperiodo, si además existe un cierre a nivel de Periodo completo (ej. cierre anual, posterior al cierre de ambos semestres), o si ambos niveles coexisten con reglas propias (ver sección 6, pendiente #6).

### 3.18 Calendario (proyección)
**Bloqueante resuelto — Fase 1.8 cerrada.** Calendario **no es una entidad propia**; es una vista proyectada a partir de eventos ya existentes en otras entidades del dominio. No almacena información duplicada.

```
Vista Calendario, proyecta:
  Evaluación            → referencia (fecha)
  Reunión                → referencia (fecha/hora)
  Cierre de periodo     → referencia (Subperiodo/Periodo Académico)
  Actividad institucional → EventoInstitucional
  Día no lectivo          → EventoInstitucional
```

### 3.19 Evento Institucional
Entidad liviana creada porque Actividad institucional y Día no lectivo no tienen entidad origen en ningún otro módulo — sin esta entidad, no podrían formar parte del calendario oficial sin romper el principio de que Calendario no almacena datos propios.

```
EventoInstitucional:
  Tipo: Actividad institucional | Día no lectivo
  Fecha
  Descripción
```

### 3.20 Reunión
Entidad propia (no deriva de otra existente). Catálogo cerrado de tipos para v1:

- Reunión Apoderado-Docente
- Reunión de Curso
- Reunión Institucional
- Consejo de Profesores
- Reunión de Convivencia/Disciplinaria

```
Reunión:
  Tipo
  Fecha, Hora inicio, Hora término
  Convocante: Persona + Rol
  Audiencia (participantes esperados)
  Estado: Programada → Realizada / Programada → Cancelada
  Lugar / modalidad (si aplica)
```

**Reglas de convocatoria confirmadas (derivadas de la capa de autorización, sección 5.1 — no se crean reglas independientes):**

| Rol | Puede convocar |
|---|---|
| Docente | Reunión individual con apoderado de estudiantes de su contexto académico (vía Asignación Docente vigente) |
| Profesor Jefe | Reunión de Curso, con estudiantes/apoderados del curso asignado |
| Inspector | Reunión de Convivencia/Disciplinaria |
| Directivo | Reunión Institucional y Consejo de Profesores |

**Convocatoria:** se comunica mediante Comunicado/Notificación, no mediante Mensajería (ya confirmado en el cierre inicial de la fase).

**Decisiones de alcance confirmadas para v1:**
- **Sin registro de asistencia real.** La entidad Reunión registra únicamente convocatoria, participantes esperados, fecha/hora y estado — no confirmación ni asistencia efectiva (evita abrir un subdominio adicional de presencia/justificaciones). Queda como evolución futura.
- **Reunión no genera Anotación automáticamente.** Se mantiene la separación ya establecida en Fase 1.6: Reunión es instancia de comunicación/gestión; Anotación es registro académico/conductual formal. Si corresponde registrar una anotación tras una reunión, se hace mediante el flujo normal ya existente, como acción manual separada.
- **Sin gestión de disponibilidad horaria.** La reunión se crea con fecha/hora ya definida por quien tiene permiso para convocarla. Evolución futura posible.

**Visibilidad de Reunión:** se deriva de la capa de autorización ya definida (sección 5.1) según el tipo de audiencia — no requiere una regla de permisos nueva.

---

## 4. Relaciones confirmadas entre entidades

- Apoderado **N:M** Estudiante (con tipo de relación: principal/secundario).
- Estudiante **1:N** Matrícula (histórico; solo una activa a la vez).
- Matrícula **N:1** Curso, Matrícula **N:1** Periodo Académico.
- Curso **N:1** Nivel Educativo, Curso **N:1** Periodo Académico, Curso **N:1** Docente (profesor jefe).
- Asignación Docente **N:1** Docente, **N:1** Asignatura, **N:1** Curso, **N:1** Periodo Académico (con vigencia temporal propia).
- Evaluación **N:1** Asignación Docente, **N:1** Subperiodo Académico.
- Calificación **N:1** Estudiante, **N:1** Evaluación.
- Anotación **N:1** Estudiante, **N:1** Actor (autor).
- Conversación **N:M** Actor (participantes); Mensaje **N:1** Conversación.
- Subperiodo Académico **N:1** Periodo Académico.
- Solicitud de Excepción de Calificación **N:1** Calificación, **N:1** Persona (solicitante), **N:1** Persona (aprobador).
- Solicitud de Acceso a Conversación **N:1** Conversación, **N:1** Persona (solicitante), **N:1** Persona (aprobador, cuando corresponda).
- Reunión **N:1** Persona (convocante), **N:M** Persona (audiencia/participantes esperados).
- Calendario (vista) proyecta: Evaluación, Reunión, Subperiodo/Periodo Académico (cierre), EventoInstitucional — sin relación de almacenamiento propia (no es entidad).

---

## 5. Conceptos transversales del dominio

### 5.1 Capa de autorización (derivada del contexto, no relaciones duplicadas)
**Bloqueante resuelto (a nivel de dominio — no es aún matriz técnica de permisos).**

**Principio general confirmado:**
> El acceso se determina mediante **Rol activo + relación de contexto + acción permitida**. No existe acceso por rol aislado, salvo los roles con alcance institucional explícito (Inspector, Administrativo, Directivo). Las relaciones académicas habilitan acceso transitivo (ej. Docente → Asignación Docente vigente → Curso → Matrículas activas → Estudiantes). Información Sensible es una categoría separada y **no se hereda automáticamente** desde ningún otro tipo de acceso (académico, administrativo o de rol ampliado).

**Autorización confirmada por rol:**

| Rol | Acceso académico/conductual | Información Sensible |
|---|---|---|
| Docente | Solo sobre las asignaturas donde tiene Asignación Docente vigente: crear Evaluaciones, registrar/modificar Calificaciones, consultar lo necesario de sus estudiantes en esa asignatura, crear Anotaciones dentro de su ámbito. **Sin acceso automático al resto de asignaturas del mismo estudiante.** | No, por defecto |
| Profesor Jefe | Vista académica general de los estudiantes de su curso, todas las anotaciones del curso, puede crear anotaciones, consulta la relación Apoderado↔Estudiante del curso. | **No** — la jefatura no otorga acceso automático a datos médicos/NEE; requiere autorización específica |
| Apoderado | Información académica básica, notas y anotaciones permitidas, de sus estudiantes asociados (relación vigente). **v1: mismo acceso para principal y secundario** — diferencia queda para permisos administrativos futuros. | Sí, únicamente respecto de sus propios estudiantes asociados (mismo criterio v1: sin distinción principal/secundario) |
| Inspector | Gestiona y consulta Anotaciones (transversal), consulta InformaciónPersonal necesaria. **Sin acceso a Calificaciones, Evaluaciones ni gestión académica.** | Sí, cuando sea relevante para su función de convivencia |
| Administrativo | Gestiona Matrículas e InformaciónPersonal necesaria para procesos administrativos. **Sin acceso por defecto** a Calificaciones ni Anotaciones. | No, por defecto — casos especiales quedan para subroles administrativos futuros |
| Directivo | Consulta académica y administrativa consolidada, anotaciones, reportes institucionales. **Escritura/aprobación:** puede aprobar procesos excepcionales que requieran autorización institucional (ej. solicitudes de modificación de notas post-cierre) — la escritura directa sobre el dato sigue perteneciendo al responsable del proceso (docente/administrativo). | Sí, para supervisión institucional |
| Estudiante | Su propia información académica/conductual. | Sí, la propia |

**Nota sobre "UTP/administrador académico" (pendiente #15):** con esta resolución, la aprobación de excepciones de notas post-cierre queda mapeada al rol **Directivo**, no a un actor nuevo. Se deja registrado así; se ajustará si en una fase posterior se confirma que UTP es un sub-rol distinto de Directivo.

**Nota sobre conflicto de interés (no resuelta, no bloqueante):** con roles múltiples por Persona, cabe la pregunta de si el sistema debe detectar/restringir situaciones donde el Rol activo entra en conflicto con otro Rol de la misma Persona (ej. Docente evaluando a un estudiante del cual también es Apoderado). No se asume como parte del alcance inicial; queda registrada como extensión futura posible.

Notas, Anotaciones, Mensajería y Reunión deben consumir esta capa de autorización, no redefinir permisos módulo a módulo. **Pendiente restante:** el detalle exacto de permisos por subrol (especialmente administrativo) queda para refinamiento futuro.

### 5.2 Auditoría / trazabilidad
Concepto común transversal, reemplaza las reglas de auditoría redactadas de forma independiente en cada módulo. **Actualizado tras la decisión de identidad única (sección 2):** el registro se ata a la Persona (identidad real que ejecutó la acción), y opcionalmente al Rol bajo el cual actuó, dado que una misma Persona puede tener más de un rol.

```
RegistroAuditoría:
  Persona, Rol activo (opcional), Fecha, Acción, Entidad afectada,
  Valor anterior, Valor nuevo, Motivo (cuando aplique)
```

Aplica a: Matrícula, Calificación, Asignación Docente, Anotación, Mensaje, y cualquier entidad con modificación relevante.

### 5.3 Notificaciones
Concepto transversal generado por eventos de otros módulos (no exclusivo de mensajería). Pendiente de definir el catálogo completo de eventos que disparan notificación.

### 5.4 Estados y ciclos de vida — sección formal (expandida en revisión final)
Patrón recurrente en el dominio: varias entidades tienen ciclo de vida con estados propios. Se consolida aquí, de forma explícita, el estado de cada una y sus transiciones (confirmadas o pendientes), para evitar que queden dispersas y potencialmente incompletas por módulo.

| Entidad | Estados confirmados | Transiciones | Terminal(es) |
|---|---|---|---|
| Matrícula | Activa, Retirada, Finalizada | **Pendiente** — no definido qué transiciones son válidas (ver #5, sección 6) | **Pendiente** — no está claro si Retirada y Finalizada son ambos terminales independientes, o si Retirada es un motivo particular de Finalizada (ver #11, sección 6) |
| Periodo Académico | Abierto, Cerrado | Pendiente | Cerrado (asumido, no confirmado explícitamente) |
| Subperiodo Académico | Abierto, Cerrado *(agregado en esta revisión)* | Pendiente — depende de si el cierre real ocurre a este nivel, al nivel de Periodo, o en ambos (ver #6, sección 6) | Cerrado (asumido, no confirmado explícitamente) |
| Anotación | Vigente, Anulada | Confirmado: Vigente → Anulada (anulación lógica, no eliminación) | Anulada — **a confirmar explícitamente como terminal** (no debería volver a Vigente) |
| Asignación Docente | Vigente / no vigente, derivado de fechas de vigencia | Confirmado conceptualmente (fin de vigencia por reemplazo) | No vigente, por fecha de término |
| Evaluación / Calificación | Implícito (editable/no editable según estado del Subperiodo) | Depende del cierre académico; excepción post-cierre vía **Solicitud de Excepción de Calificación** (ver 3.11.1) — **resuelto**, entidad propia con flujo de aprobación | — |
| Solicitud de Excepción de Calificación *(agregada en esta revisión)* | Pendiente, Aprobada, Rechazada, Ejecutada | Confirmado: Pendiente → Aprobada → Ejecutada; Pendiente → Rechazada | Rechazada, Ejecutada (ambos terminales) |
| Solicitud de Acceso a Conversación *(agregada en esta revisión)* | Pendiente, Aprobada, Rechazada, Ejecutada | Confirmado: Pendiente → Aprobada → Ejecutada; Pendiente → Rechazada | Rechazada, Ejecutada (ambos terminales) |
| Reunión *(agregada — cierre Fase 1.8)* | Programada, Realizada, Cancelada | Confirmado: Programada → Realizada; Programada → Cancelada | Realizada, Cancelada (ambos terminales) |

**Nota metodológica:** de aquí en adelante, toda entidad nueva con ciclo de vida debe declarar explícitamente sus estados y transiciones en esta tabla al momento de definirse, en vez de mencionarse solo de forma narrativa dentro de su sección — esto evita repetir el tipo de vacío detectado en esta revisión (ej. Subperiodo sin estado, pese a que el cierre académico ya dependía de él).

### 5.5 Periodos académicos
Ya detallado en 3.17. Es la unidad temporal que enmarca Matrícula, Curso, Asignación Docente y Evaluación. Con subperiodos (semestres) relevantes para el módulo de notas.

---

## 6. Decisiones pendientes, por prioridad

### Bloqueantes para el modelo de dominio formal
1. ~~Definir la capa de autorización~~ — ** RESUELTO.** Principio general confirmado (Rol activo + relación de contexto + acción permitida) y autorización confirmada por rol, incluyendo InformaciónSensible como categoría separada no heredable (ver sección 5.1, tabla completa).
2. ~~Definir el mecanismo exacto de autorización excepcional para auditoría de mensajería~~ — ** RESUELTO.** Se adopta entidad `SolicitudAccesoConversación` con flujo de aprobación formal, alcance puntual sobre una conversación específica, y regla explícita de aprobación cruzada entre Inspector/Directivo (con excepción controlada si el establecimiento tiene un único integrante directivo con esta facultad) (ver sección 3.16).
3. ~~Definir el modelo de segregación y permisos específicos de Información Sensible~~ — ** RESUELTO** como parte del punto 1 (sección 5.1): acceso confirmado por rol; queda como refinamiento futuro no bloqueante el detalle exacto de subroles administrativos.
4. ~~Cerrar Fase 1.8 (Calendario/Reuniones)~~ — ** RESUELTO.** Calendario como proyección (no entidad), `EventoInstitucional` creada para Actividad institucional/Día no lectivo, `Reunión` formalizada con catálogo cerrado de 5 tipos, reglas de convocatoria derivadas de la capa de autorización, sin registro de asistencia real ni generación automática de Anotación, sin gestión de disponibilidad horaria (ver secciones 3.18–3.20).
5. ~~Definir el concepto de Actor~~ — ** RESUELTO.** Se adopta identidad única (Persona) con uno o más Roles asociados (ver sección 2).
6. ~~Resolver la naturaleza de "Solicitud de excepción de nota"~~ — ** RESUELTO.** Se adopta como entidad propia del dominio (`SolicitudExcepciónCalificación`), con flujo de aprobación formal (Pendiente → Aprobada/Rechazada → Ejecutada), alcance puntual sobre una Calificación específica (no reapertura del Subperiodo completo), y relación con Auditoría y Notificación como mecanismos transversales complementarios (ver sección 3.11.1).

### Importantes, no bloqueantes
7. Transiciones de estado válidas para Matrícula (¿Retirada puede volver a Activa o es terminal?).
8. Granularidad del cierre académico: si ocurre exclusivamente por Subperiodo, también a nivel de Periodo completo, o ambos coexisten con reglas propias.
9. ~~Diferencia de permisos entre apoderado principal y secundario~~ — ** RESUELTO para v1** (ver sección 3.3): mismo acceso para ambos; la distinción queda para una fase futura de permisos administrativos específicos.
10. Quién puede emitir Comunicados y a qué alcance (curso, nivel, establecimiento).
11. ~~Acceso de Personal Administrativo a Notas y Anotaciones~~ — ** RESUELTO** (ver sección 5.1): sin acceso por defecto a Calificaciones ni Anotaciones.
12. Catálogo completo de eventos que disparan Notificación.
13. Posible subdivisión futura de Personal Administrativo en sub-roles (relevante para el refinamiento de InformaciónSensible mencionado en el punto 1).
14. Definir si "Establecimiento" debe existir como entidad formal (con nombre, configuración propia), o si el acceso transversal de Inspector/Administrativo/Directivo se expresa simplemente como "sin restricción de contexto", sin relación hacia una entidad.
15. ~~Mapear "UTP/administrador académico"~~ — ** RESUELTO (provisoriamente):** mapeado al rol Directivo, como aprobador de excepciones de notas (ver sección 5.1). Se ajustará si en el futuro se confirma que UTP es un sub-rol distinto.
16. Definir si Notificación es una entidad persistente con estado propio (ej. enviada/leída), o un mecanismo efímero sin necesidad de persistencia.
17. Resolver si Retirada y Finalizada son estados terminales independientes de Matrícula, o si el retiro del estudiante es un caso particular (motivo) de Finalizada.
18. Explorar la relación conceptual entre Auditoría (sección 5.2) y Notificación (sección 5.3): si conviene un concepto común de "evento de dominio" del cual ambas se deriven.
19. **(Nuevo)** Definir si el sistema debe detectar/restringir conflictos de interés entre roles de una misma Persona (ej. Docente que es Apoderado del mismo estudiante) — mencionado como extensión futura en sección 5.1, no confirmado como alcance actual.

### Menores
20. Existencia de categoría "neutra" de anotación (descartada por ahora, revisar a futuro).
21. Capacidad máxima de curso (fuera de alcance inicial, evaluar más adelante).
22. Co-docencia (fuera de alcance inicial, evolución futura).
23. Conversaciones grupales en mensajería (fuera de alcance inicial, evolución futura).
24. Reglas de promoción/repitencia (explícitamente fuera de alcance).
25. ~~Gestión de disponibilidad horaria para agendar reuniones~~ — ** RESUELTO:** fuera de alcance v1 (ver sección 3.20), queda como evolución futura.
26. Definir si Conversación tiene algún estado o ciclo de vida propio (ej. archivada/cerrada) o permanece abierta indefinidamente.
27. Definir si "Comunicado" debe modelarse como entidad propia con estado, o como un caso especial de Notificación con audiencia amplia — depende de resolver primero el pendiente #10.

---

## 7. Fases completadas y aprobadas

| Fase | Contenido | Estado |
|---|---|---|
| 1.1 | Contexto general del dominio | Aprobada |
| 1.2 | Actores del sistema | Aprobada |
| 1.3 | Estudiantes y matrículas | Aprobada (con corrección posterior) |
| 1.4 | Cursos y asignaturas | Aprobada (con corrección posterior) |
| 1.5 | Notas y evaluaciones | Aprobada |
| 1.6 | Anotaciones y comportamiento | Aprobada (con corrección posterior) |
| 1.7 | Mensajería | Aprobada |
| — | Revisión arquitectónica de dominio | Aplicada, correcciones incorporadas |
| 1.8 | Calendario y reuniones | Aprobada |
| — | Resolución de bloqueantes (autorización, Información Sensible, Actor/Persona, Solicitud de Excepción de Calificación, Solicitud de Acceso a Conversación, cierre de Fase 1.8) | Aplicada, correcciones incorporadas |

---

*Fin del documento consolidado. **Fase 1 (Análisis del Dominio) completa: los 6 bloqueantes identificados están resueltos** — capa de autorización, segregación de Información Sensible, concepto Actor/Persona+Roles, Solicitud de Excepción de Calificación, Solicitud de Acceso a Conversación, y cierre de Fase 1.8 (Calendario/Reuniones). Quedan pendientes menores e importantes no bloqueantes que pueden resolverse durante el modelo de dominio formal o quedar como evolución futura, según corresponda en cada caso. Próximo paso: iniciar el modelo de dominio formal (entidades, atributos definitivos, agregados) a partir de este documento como fuente única de verdad.*
