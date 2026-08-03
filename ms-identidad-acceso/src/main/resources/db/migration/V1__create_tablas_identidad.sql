CREATE TABLE persona (
    persona_id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    rut_documento_identidad   VARCHAR(20)  NOT NULL UNIQUE,
    nombres                   VARCHAR(100) NOT NULL,
    apellidos                 VARCHAR(100) NOT NULL,
    fecha_nacimiento          DATE         NOT NULL,
    email                     VARCHAR(150) NOT NULL UNIQUE,
    telefono                  VARCHAR(20),
    usuario                   VARCHAR(50)  NOT NULL UNIQUE,
    password_hash             VARCHAR(255) NOT NULL,
    fecha_creacion             TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE rol (
    rol_id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol   VARCHAR(30) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE persona_rol (
    persona_rol_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    persona_id       BIGINT      NOT NULL,
    rol_id           BIGINT      NOT NULL,
    fecha_inicio     DATE        NOT NULL,
    fecha_termino    DATE        NULL,
    estado           VARCHAR(20) NOT NULL,
    CONSTRAINT fk_persona_rol_persona
        FOREIGN KEY (persona_id) REFERENCES persona (persona_id),
    CONSTRAINT fk_persona_rol_rol
        FOREIGN KEY (rol_id) REFERENCES rol (rol_id),
    CONSTRAINT chk_persona_rol_estado
        CHECK (estado IN ('ACTIVO', 'INACTIVO'))
) ENGINE=InnoDB;

CREATE INDEX idx_persona_rol_persona ON persona_rol (persona_id);
CREATE INDEX idx_persona_rol_rol ON persona_rol (rol_id);