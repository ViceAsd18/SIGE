CREATE TABLE evaluacion_calendario (
    evaluacion_id            BIGINT       PRIMARY KEY,
    subperiodo_academico_id  BIGINT       NOT NULL,
    nombre                   VARCHAR(150) NOT NULL,
    fecha                    DATE         NOT NULL
) ENGINE=InnoDB;

CREATE TABLE cierre_academico_calendario (
    cierre_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo        VARCHAR(20)  NOT NULL,
    origen_id   BIGINT       NOT NULL,
    nombre      VARCHAR(150) NOT NULL,
    fecha       DATE         NOT NULL,
    CONSTRAINT chk_cierre_tipo CHECK (tipo IN ('SUBPERIODO', 'PERIODO')),
    CONSTRAINT uq_cierre_tipo_origen UNIQUE (tipo, origen_id)
) ENGINE=InnoDB;

CREATE INDEX idx_evalcal_fecha ON evaluacion_calendario (fecha);
CREATE INDEX idx_cierrecal_fecha ON cierre_academico_calendario (fecha);