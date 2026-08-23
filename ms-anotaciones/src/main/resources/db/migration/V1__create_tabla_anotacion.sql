CREATE TABLE anotacion (
    anotacion_id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    -- Referencia cruzada a ms-estudiantes. NO es FK real.
    estudiante_id            BIGINT       NOT NULL,
    -- Referencia cruzada a ms-identidad-acceso. NO es FK real.
    autor_persona_rol_id     BIGINT       NOT NULL,
    categoria                VARCHAR(20)  NOT NULL,
    gravedad                 VARCHAR(20)  NULL,
    descripcion               VARCHAR(1000) NOT NULL,
    fecha_creacion            TIMESTAMP    NOT NULL,
    estado                    VARCHAR(20)  NOT NULL,
    CONSTRAINT chk_anotacion_categoria CHECK (categoria IN ('POSITIVA', 'NEGATIVA', 'ACADEMICA')),
    CONSTRAINT chk_anotacion_gravedad CHECK (gravedad IN ('LEVE', 'GRAVE', 'GRAVISIMA') OR gravedad IS NULL),
    CONSTRAINT chk_anotacion_estado CHECK (estado IN ('VIGENTE', 'ANULADA'))
) ENGINE=InnoDB;

CREATE INDEX idx_anotacion_estudiante ON anotacion (estudiante_id);
CREATE INDEX idx_anotacion_autor ON anotacion (autor_persona_rol_id);