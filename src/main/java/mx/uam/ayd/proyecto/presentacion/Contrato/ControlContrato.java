package mx.uam.ayd.proyecto.presentacion.Contrato;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.negocio.ServicioContrato;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;

@Component
public class ControlContrato {

    private final ServicioContrato servicioContrato;
    private final VentanaContrato ventana;

    @Autowired
    public ControlContrato(ServicioContrato servicioContrato,
                           VentanaContrato ventana) {
        this.servicioContrato = servicioContrato;
        this.ventana = ventana;
    }

    @PostConstruct
    public void init() {
        ventana.setControlContrato(this);
    }

    /**
     * Inicia la historia de usuario.
     */
    public void iniciaContrato(Evento evento) {
        String clausulas = servicioContrato.obtenerClausulas(evento);
        ventana.muestraContrato(evento, clausulas);
    }

    /**
     * Guarda los cambios realizados en las cláusulas.
     */
    public void guardarClausulas(Evento evento, String clausulas) {
        servicioContrato.actualizarClausulas(evento, clausulas);
    }

    /**
     * Marca el contrato como firmado.
     */
    public void firmarContrato(Evento evento) {
        servicioContrato.firmarContrato(evento);
    }
}