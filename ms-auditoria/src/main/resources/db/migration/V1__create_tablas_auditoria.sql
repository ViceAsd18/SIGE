CREATE TABLE registro_auditoria (
    registro_auditoria_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    -- Referencia cruzada a ms-identidad-acceso. NO es FK real.
    persona_id                   BIGINT       NOT NULL,
    -- Referencia cruzada a ms-identidad-acceso. NO es FK real. Nullable.
    rol_activo_persona_rol_id     BIGINT       NULL,
    fecha                         TIMESTAMP    NOT NULL,
    accion                        VARCHAR(50)  NOT NULL,
    entidad_afectada_tipo          VARCHAR(50)  NOT NULL,
    entidad_afectada_id             BIGINT       NOT NULL,
    valor_anterior                  VARCHAR(2000) NULL,
    valor_nuevo                     VARCHAR(2000) NULL,
    motivo                          VARCHAR(500)  NULL
) ENGINE=InnoDB;

CREATE INDEX idx_auditoria_persona ON registro_auditoria (persona_id);
CREATE INDEX idx_auditoria_entidad ON registro_auditoria (entidad_afectada_tipo, entidad_afectada_id);
CREATE INDEX idx_auditoria_fecha ON registro_auditoria (fecha);