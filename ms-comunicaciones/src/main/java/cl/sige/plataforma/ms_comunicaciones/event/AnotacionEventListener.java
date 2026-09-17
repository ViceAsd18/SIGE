package cl.sige.plataforma.ms_comunicaciones.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import cl.sige.plataforma.ms_comunicaciones.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component 
@RequiredArgsConstructor
@Slf4j 
public class AnotacionEventListener {
    
    private final NotificacionService notificacionService;

    @KafkaListener(topics = "anotaciones.eventos", groupId = "ms-comunicaciones")
    public void escucharAnotacionCreada(AnotacionCreadaEvent evento) {
        log.info("Evento recibido: AnotacionCreada, anotacionId={}", evento.anotacionId());

        String contenido = "Se registró una nueva anotacion (" + evento.categoria() + ")";

        notificacionService.crear(
            evento.estudianteId(),
            "ANOTACION_CREADA",
            "Anotacion",
            evento.anotacionId(),
            contenido);
    }
}
