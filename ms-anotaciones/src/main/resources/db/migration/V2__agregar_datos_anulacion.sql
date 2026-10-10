ALTER TABLE anotacion
    ADD COLUMN motivo_anulacion          VARCHAR(500) NULL,
    ADD COLUMN anulada_por_persona_rol_id BIGINT      NULL,
    ADD COLUMN fecha_anulacion           TIMESTAMP    NULL;