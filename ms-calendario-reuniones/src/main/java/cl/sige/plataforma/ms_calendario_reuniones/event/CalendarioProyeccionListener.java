package cl.sige.plataforma.ms_calendario_reuniones.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import cl.sige.plataforma.ms_calendario_reuniones.domain.enums.TipoCierreAcademico;
import cl.sige.plataforma.ms_calendario_reuniones.service.CalendarioProyeccionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CalendarioProyeccionListener {

    private final CalendarioProyeccionService proyeccionService;

    @KafkaListener(
        topics = "evaluaciones.eventos",
        groupId = "ms-calendario-reuniones",
        properties = {
            "value.deserializer=org.springframework.kafka.support.serializer.JsonDeserializer",
            "spring.json.trusted.packages=*",
            "spring.json.use.type.headers=false",
            "spring.json.value.default.type=cl.sige.plataforma.ms_calendario_reuniones.event.EvaluacionEventoRecibido",
            "auto.offset.reset=earliest"
        })
    public void escucharEvaluaciones(EvaluacionEventoRecibido evento) {
        // Otros tipos del topico (Calificacion, SolicitudExcepcion) no traen estos campos: se ignoran.
        if (evento == null || evento.evaluacionId() == null
                || evento.asignacionDocenteId() == null || evento.fecha() == null) {
            return;
        }
        proyeccionService.registrarEvaluacion(
            evento.evaluacionId(), evento.subperiodoAcademicoId(), evento.nombre(), evento.fecha());
    }

    @KafkaListener(
        topics = "academico.eventos",
        groupId = "ms-calendario-reuniones",
        properties = {
            "value.deserializer=org.springframework.kafka.support.serializer.JsonDeserializer",
            "spring.json.trusted.packages=*",
            "spring.json.use.type.headers=false",
            "spring.json.value.default.type=cl.sige.plataforma.ms_calendario_reuniones.event.AcademicoEventoRecibido",
            "auto.offset.reset=earliest"
        })
    public void escucharAcademico(AcademicoEventoRecibido evento) {
        // Solo los cierres traen fechaTermino (eventos viejos sin ella tambien se ignoran).
        if (evento == null || evento.fechaTermino() == null) {
            return;
        }
        if (evento.subperiodoAcademicoId() != null) {
            proyeccionService.registrarCierre(TipoCierreAcademico.SUBPERIODO,
                evento.subperiodoAcademicoId(), evento.nombre(), evento.fechaTermino());
        } else if (evento.nombreAnio() != null && evento.periodoAcademicoId() != null) {
            proyeccionService.registrarCierre(TipoCierreAcademico.PERIODO,
                evento.periodoAcademicoId(), evento.nombreAnio(), evento.fechaTermino());
        }
    }
}
