CREATE TABLE conversacion (
    conversacion_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_inicio       TIMESTAMP NOT NULL
) ENGINE=InnoDB;

CREATE TABLE mensaje (
    mensaje_id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    conversacion_id              BIGINT       NOT NULL,
    -- Referencia cruzada a ms-identidad-acceso (Persona). NO es FK real.
    autor_persona_id              BIGINT       NOT NULL,
    contenido                     VARCHAR(2000) NOT NULL,
    fecha_envio                   TIMESTAMP    NOT NULL,
    fecha_ultima_modificacion     TIMESTAMP    NULL,
    CONSTRAINT fk_mensaje_conversacion
        FOREIGN KEY (conversacion_id) REFERENCES conversacion (conversacion_id)
) ENGINE=InnoDB;

CREATE TABLE conversacion_participante (
    conversacion_participante_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    conversacion_id                 BIGINT    NOT NULL,
    -- Referencia cruzada a ms-identidad-acceso (Persona). NO es FK real.
    persona_id                       BIGINT    NOT NULL,
    fecha_incorporacion               TIMESTAMP NOT NULL,
    CONSTRAINT fk_participante_conversacion
        FOREIGN KEY (conversacion_id) REFERENCES conversacion (conversacion_id),
    -- Una persona no puede estar dos veces en la misma conversacion.
    CONSTRAINT uq_participante_conversacion_persona UNIQUE (conversacion_id, persona_id)
) ENGINE=InnoDB;

CREATE TABLE solicitud_acceso_conversacion (
    solicitud_acceso_conversacion_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    conversacion_id                     BIGINT       NOT NULL,
    -- Referencias cruzadas a ms-identidad-acceso. NO son FK reales.
    solicitante_persona_rol_id           BIGINT       NOT NULL,
    aprobador_persona_rol_id             BIGINT       NULL,
    motivo                                VARCHAR(500) NOT NULL,
    estado                                VARCHAR(20)  NOT NULL,
    fecha_solicitud                       TIMESTAMP    NOT NULL,
    fecha_resolucion                      TIMESTAMP    NULL,
    CONSTRAINT fk_solicitud_acceso_conversacion
        FOREIGN KEY (conversacion_id) REFERENCES conversacion (conversacion_id),
    CONSTRAINT chk_solicitud_acceso_estado
        CHECK (estado IN ('PENDIENTE', 'APROBADA', 'RECHAZADA', 'EJECUTADA'))
) ENGINE=InnoDB;

CREATE INDEX idx_mensaje_conversacion ON mensaje (conversacion_id);
CREATE INDEX idx_participante_conversacion ON conversacion_participante (conversacion_id);
CREATE INDEX idx_participante_persona ON conversacion_participante (persona_id);
CREATE INDEX idx_solicitud_acceso_conversacion ON solicitud_acceso_conversacion (conversacion_id);