package cl.sige.plataforma.ms_comunicaciones.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.sige.plataforma.ms_comunicaciones.domain.Notificacion;
import cl.sige.plataforma.ms_comunicaciones.exception.RecursoNoEncontradoException;
import cl.sige.plataforma.ms_comunicaciones.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacionService {
    
    private final NotificacionRepository notificacionRepository;

    @Transactional
    public Notificacion crear(Long destinatarioPersonaId, String tipoEvento, String entidadOrigenTipo, Long entidadOrigenId, String contenido) {
        Notificacion notificacion = notificacionRepository.save(
            new Notificacion(destinatarioPersonaId, tipoEvento, entidadOrigenTipo, entidadOrigenId, contenido));
        log.info("Notificacion creada: id={}, destinatarioPersonaId={}, tipoEvento={}",
            notificacion.getId(), destinatarioPersonaId, tipoEvento);
        return notificacion;
    }

    @Transactional
    public void marcarLeida(Long id) {
        Notificacion notificacion = notificacionRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Notificacion", id));
        notificacion.marcaLeida();
        log.info("Notificacion marcada como leida: id={}", id);
    }

    @Transactional(readOnly = true)
    public List<Notificacion> obtenerPorDestinatario(Long destinatarioPersonaId) {
        return notificacionRepository.findByDestinatarioPersonaId(destinatarioPersonaId);
    }   

}
