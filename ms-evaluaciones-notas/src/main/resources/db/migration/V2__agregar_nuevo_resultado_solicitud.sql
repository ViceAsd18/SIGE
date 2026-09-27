ALTER TABLE solicitud_excepcion_calificacion
    ADD COLUMN nuevo_resultado DECIMAL(2,1) NULL;

-- Backfill de filas existentes con el resultado actual, para no dejar NOT NULL sobre datos viejos sin valor
UPDATE solicitud_excepcion_calificacion sec
    JOIN calificacion c ON c.calificacion_id = sec.calificacion_id
    SET sec.nuevo_resultado = c.resultado
    WHERE sec.nuevo_resultado IS NULL;

ALTER TABLE solicitud_excepcion_calificacion
    MODIFY COLUMN nuevo_resultado DECIMAL(2,1) NOT NULL;

ALTER TABLE solicitud_excepcion_calificacion
    ADD CONSTRAINT chk_solicitud_resultado CHECK (nuevo_resultado BETWEEN 1.0 AND 7.0);