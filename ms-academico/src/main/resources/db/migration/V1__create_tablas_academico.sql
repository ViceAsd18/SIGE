-- Orden de creación respeta las dependencias de FK: primero las entidades
-- sin dependencias, luego las que las referencian.

CREATE TABLE nivel_educativo (
    nivel_educativo_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre                VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE periodo_academico (
    periodo_academico_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_anio             VARCHAR(20) NOT NULL UNIQUE,
    fecha_inicio            DATE        NOT NULL,
    fecha_termino           DATE        NOT NULL,
    estado                  VARCHAR(20) NOT NULL,
    CONSTRAINT chk_periodo_estado CHECK (estado IN ('ABIERTO', 'CERRADO'))
) ENGINE=InnoDB;

CREATE TABLE asignatura (
    asignatura_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre           VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE subperiodo_academico (
    subperiodo_academico_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    periodo_academico_id       BIGINT      NOT NULL,
    nombre                      VARCHAR(50) NOT NULL,
    fecha_inicio                DATE        NOT NULL,
    fecha_termino               DATE        NOT NULL,
    estado                      VARCHAR(20) NOT NULL,
    CONSTRAINT fk_subperiodo_periodo
        FOREIGN KEY (periodo_academico_id) REFERENCES periodo_academico (periodo_academico_id),
    CONSTRAINT chk_subperiodo_estado CHECK (estado IN ('ABIERTO', 'CERRADO')),
    -- "Primer semestre" debe poder repetirse entre distintos periodos (2026, 2027, ...),
    -- pero no duplicarse dentro del MISMO periodo.
    CONSTRAINT uq_subperiodo_nombre_por_periodo UNIQUE (periodo_academico_id, nombre)
) ENGINE=InnoDB;

CREATE TABLE curso (
    curso_id                       BIGINT AUTO_INCREMENT PRIMARY KEY,
    nivel_educativo_id             BIGINT      NOT NULL,
    periodo_academico_id           BIGINT      NOT NULL,
    paralelo                       VARCHAR(5)  NOT NULL,
    -- Referencia cruzada a PERSONA_ROL en ms-identidad-acceso.
    -- NO es FK real: cada microservicio tiene su propia base de datos.
    -- La integridad se garantiza por validacion Feign en tiempo de escritura.
    profesor_jefe_persona_rol_id   BIGINT      NOT NULL,
    CONSTRAINT fk_curso_nivel
        FOREIGN KEY (nivel_educativo_id) REFERENCES nivel_educativo (nivel_educativo_id),
    CONSTRAINT fk_curso_periodo
        FOREIGN KEY (periodo_academico_id) REFERENCES periodo_academico (periodo_academico_id),
    -- No pueden existir dos cursos "1 Medio A" en el mismo periodo.
    CONSTRAINT uq_curso_nivel_periodo_paralelo UNIQUE (nivel_educativo_id, periodo_academico_id, paralelo)
) ENGINE=InnoDB;

CREATE TABLE asignacion_docente (
    asignacion_docente_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    -- Referencia cruzada a PERSONA_ROL en ms-identidad-acceso. NO es FK real.
    docente_persona_rol_id     BIGINT      NOT NULL,
    asignatura_id               BIGINT      NOT NULL,
    curso_id                    BIGINT      NOT NULL,
    fecha_inicio_vigencia       DATE        NOT NULL,
    fecha_termino_vigencia      DATE        NULL,
    estado                      VARCHAR(20) NOT NULL,
    CONSTRAINT fk_asigdoc_asignatura
        FOREIGN KEY (asignatura_id) REFERENCES asignatura (asignatura_id),
    CONSTRAINT fk_asigdoc_curso
        FOREIGN KEY (curso_id) REFERENCES curso (curso_id),
    CONSTRAINT chk_asigdoc_estado CHECK (estado IN ('VIGENTE', 'NO_VIGENTE'))
) ENGINE=InnoDB;

CREATE TABLE matricula (
    matricula_id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    -- Referencia cruzada a ESTUDIANTE en ms-estudiantes. NO es FK real
    -- (distinto de las demas referencias cruzadas de este archivo, que
    -- apuntan a ms-identidad-acceso).
    estudiante_id            BIGINT      NOT NULL,
    curso_id                 BIGINT      NOT NULL,
    estado                   VARCHAR(20) NOT NULL,
    motivo                   VARCHAR(255) NULL,
    fecha_inicio_vigencia    DATE        NOT NULL,
    fecha_termino_vigencia   DATE        NULL,
    CONSTRAINT fk_matricula_curso
        FOREIGN KEY (curso_id) REFERENCES curso (curso_id),
    CONSTRAINT chk_matricula_estado CHECK (estado IN ('ACTIVA', 'RETIRADA', 'FINALIZADA'))
) ENGINE=InnoDB;

-- Indices sobre columnas de FK real y sobre referencias cruzadas que
-- vamos a consultar con frecuencia (ej. "todas las matriculas de tal estudiante").
CREATE INDEX idx_subperiodo_periodo ON subperiodo_academico (periodo_academico_id);
CREATE INDEX idx_curso_nivel ON curso (nivel_educativo_id);
CREATE INDEX idx_curso_periodo ON curso (periodo_academico_id);
CREATE INDEX idx_asigdoc_asignatura ON asignacion_docente (asignatura_id);
CREATE INDEX idx_asigdoc_curso ON asignacion_docente (curso_id);
CREATE INDEX idx_asigdoc_docente_persona_rol ON asignacion_docente (docente_persona_rol_id);
CREATE INDEX idx_matricula_curso ON matricula (curso_id);
CREATE INDEX idx_matricula_estudiante ON matricula (estudiante_id);