CREATE TABLE evento_institucional (
    evento_institucional_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo                       VARCHAR(30)  NOT NULL,
    fecha                      DATE         NOT NULL,
    descripcion                 VARCHAR(500) NOT NULL,
    CONSTRAINT chk_evento_tipo CHECK (tipo IN ('ACTIVIDAD_INSTITUCIONAL', 'DIA_NO_LECTIVO'))
) ENGINE=InnoDB;

CREATE TABLE reunion (
    reunion_id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo                         VARCHAR(30)  NOT NULL,
    fecha                        DATE         NOT NULL,
    hora_inicio                  TIME         NOT NULL,
    hora_termino                 TIME         NOT NULL,
    -- Referencia cruzada a ms-identidad-acceso. NO es FK real.
    convocante_persona_rol_id     BIGINT       NOT NULL,
    estado                        VARCHAR(20)  NOT NULL,
    lugar_modalidad                VARCHAR(200) NULL,
    CONSTRAINT chk_reunion_tipo CHECK (tipo IN
        ('APODERADO_DOCENTE', 'CURSO', 'INSTITUCIONAL', 'CONSEJO_PROFESORES', 'CONVIVENCIA_DISCIPLINARIA')),
    CONSTRAINT chk_reunion_estado CHECK (estado IN ('PROGRAMADA', 'REALIZADA', 'CANCELADA'))
) ENGINE=InnoDB;

CREATE TABLE reunion_participante (
    reunion_participante_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    reunion_id                 BIGINT NOT NULL,
    -- Referencia cruzada a ms-identidad-acceso. NO es FK real.
    persona_id                  BIGINT NOT NULL,
    CONSTRAINT fk_participante_reunion
        FOREIGN KEY (reunion_id) REFERENCES reunion (reunion_id),
    CONSTRAINT uq_reunion_participante UNIQUE (reunion_id, persona_id)
) ENGINE=InnoDB;

CREATE INDEX idx_reunion_convocante ON reunion (convocante_persona_rol_id);
CREATE INDEX idx_participante_reunion ON reunion_participante (reunion_id);