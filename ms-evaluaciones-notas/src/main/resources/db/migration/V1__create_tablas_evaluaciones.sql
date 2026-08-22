CREATE TABLE evaluacion (
    evaluacion_id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    -- Referencias cruzadas a ms-academico. NO son FK reales.
    asignacion_docente_id      BIGINT       NOT NULL,
    subperiodo_academico_id    BIGINT       NOT NULL,
    nombre                      VARCHAR(150) NOT NULL,
    fecha                       DATE         NOT NULL,
    ponderacion                 INT          NOT NULL
) ENGINE=InnoDB;

CREATE TABLE calificacion (
    calificacion_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    evaluacion_id      BIGINT      NOT NULL,
    -- Referencia cruzada a ms-estudiantes. NO es FK real.
    estudiante_id       BIGINT      NOT NULL,
    resultado           DECIMAL(2,1) NOT NULL,
    CONSTRAINT fk_calificacion_evaluacion
        FOREIGN KEY (evaluacion_id) REFERENCES evaluacion (evaluacion_id),
    CONSTRAINT chk_calificacion_resultado CHECK (resultado BETWEEN 1.0 AND 7.0),
    -- Un estudiante no puede tener dos calificaciones para la misma evaluacion.
    CONSTRAINT uq_calificacion_evaluacion_estudiante UNIQUE (evaluacion_id, estudiante_id)
) ENGINE=InnoDB;

CREATE TABLE solicitud_excepcion_calificacion (
    solicitud_excepcion_calificacion_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    calificacion_id                        BIGINT       NOT NULL,
    -- Referencias cruzadas a ms-identidad-acceso. NO son FK reales.
    solicitante_persona_rol_id              BIGINT       NOT NULL,
    aprobador_persona_rol_id                BIGINT       NULL,
    motivo                                   VARCHAR(500) NOT NULL,
    estado                                   VARCHAR(20)  NOT NULL,
    fecha_solicitud                          TIMESTAMP    NOT NULL,
    fecha_resolucion                         TIMESTAMP    NULL,
    CONSTRAINT fk_solicitud_calificacion
        FOREIGN KEY (calificacion_id) REFERENCES calificacion (calificacion_id),
    CONSTRAINT chk_solicitud_estado
        CHECK (estado IN ('PENDIENTE', 'APROBADA', 'RECHAZADA', 'EJECUTADA'))
) ENGINE=InnoDB;

CREATE INDEX idx_calificacion_evaluacion ON calificacion (evaluacion_id);
CREATE INDEX idx_calificacion_estudiante ON calificacion (estudiante_id);
CREATE INDEX idx_evaluacion_asignacion ON evaluacion (asignacion_docente_id);
CREATE INDEX idx_solicitud_calificacion ON solicitud_excepcion_calificacion (calificacion_id);