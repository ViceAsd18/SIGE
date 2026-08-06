CREATE TABLE estudiante (
    estudiante_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    -- persona_rol_id referencia a PERSONA_ROL en la base de datos de
    -- ms-identidad-acceso. NO es una FK real: cada microservicio tiene
    -- su propia base de datos, sin acceso cruzado. La integridad se
    -- garantiza por validacion Feign en tiempo de escritura, no por MySQL.
    persona_rol_id   BIGINT NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE informacion_sensible_estudiante (
    informacion_sensible_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    estudiante_id             BIGINT NOT NULL UNIQUE,
    informacion_salud         TEXT,
    informacion_nee           TEXT,
    observaciones             TEXT,
    CONSTRAINT fk_infosensible_estudiante
        FOREIGN KEY (estudiante_id) REFERENCES estudiante (estudiante_id)
) ENGINE=InnoDB;

CREATE TABLE apoderado (
    apoderado_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    -- Mismo caso que estudiante.persona_rol_id: referencia cruzada sin FK real.
    persona_rol_id   BIGINT NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE apoderado_estudiante (
    apoderado_estudiante_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    apoderado_id              BIGINT      NOT NULL,
    estudiante_id             BIGINT      NOT NULL,
    tipo_relacion             VARCHAR(20) NOT NULL,
    CONSTRAINT fk_apod_est_apoderado
        FOREIGN KEY (apoderado_id) REFERENCES apoderado (apoderado_id),
    CONSTRAINT fk_apod_est_estudiante
        FOREIGN KEY (estudiante_id) REFERENCES estudiante (estudiante_id),
    CONSTRAINT chk_tipo_relacion
        CHECK (tipo_relacion IN ('PRINCIPAL', 'SECUNDARIO')),
    CONSTRAINT uq_apoderado_estudiante
        UNIQUE (apoderado_id, estudiante_id)
) ENGINE=InnoDB;

CREATE INDEX idx_apod_est_apoderado ON apoderado_estudiante (apoderado_id);
CREATE INDEX idx_apod_est_estudiante ON apoderado_estudiante (estudiante_id);