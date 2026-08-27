CREATE TABLE comunicado (
    comunicado_id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    -- Referencia cruzada a ms-identidad-acceso. NO es FK real.
    emisor_persona_rol_id     BIGINT       NOT NULL,
    tipo_alcance               VARCHAR(20)  NOT NULL,
    -- Referencias cruzadas a ms-academico. NO son FK reales. Nulas segun tipo_alcance.
    curso_id                    BIGINT       NULL,
    nivel_educativo_id          BIGINT       NULL,
    asunto                       VARCHAR(200)  NOT NULL,
    contenido                    VARCHAR(3000) NOT NULL,
    fecha_publicacion            TIMESTAMP    NOT NULL,
    CONSTRAINT chk_comunicado_alcance CHECK (tipo_alcance IN ('CURSO', 'NIVEL', 'ESTABLECIMIENTO'))
) ENGINE=InnoDB;

CREATE TABLE notificacion (
    notificacion_id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    -- Referencia cruzada a ms-identidad-acceso. NO es FK real.
    destinatario_persona_id     BIGINT       NOT NULL,
    tipo_evento                  VARCHAR(50)  NOT NULL,
    entidad_origen_tipo           VARCHAR(50)  NULL,
    entidad_origen_id              BIGINT       NULL,
    contenido                      VARCHAR(1000) NOT NULL,
    estado                         VARCHAR(20)  NOT NULL,
    fecha_creacion                 TIMESTAMP    NOT NULL,
    fecha_lectura                  TIMESTAMP    NULL,
    CONSTRAINT chk_notificacion_estado CHECK (estado IN ('ENVIADA', 'LEIDA'))
) ENGINE=InnoDB;

CREATE INDEX idx_comunicado_curso ON comunicado (curso_id);
CREATE INDEX idx_comunicado_nivel ON comunicado (nivel_educativo_id);
CREATE INDEX idx_notificacion_destinatario ON notificacion (destinatario_persona_id);