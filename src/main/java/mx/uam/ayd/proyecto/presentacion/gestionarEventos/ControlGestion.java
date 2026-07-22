package mx.uam.ayd.proyecto.presentacion.gestionarEventos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.negocio.ServicioCliente;
import mx.uam.ayd.proyecto.negocio.ServicioEvento;

/**
 * Módulo de control para la HU-5
 * @author JLCB
 */
@Component
public class ControlGestion {
    
    private final ServicioEvento servicioEvento;
    private final ServicioCliente servicioCliente;
    private final VentanaGestion ventana;

    @Autowired
    public ControlGestion(ServicioEvento servicioEvento, ServicioCliente servicioCliente, VentanaGestion ventanaGestion){
        this.servicioEvento = servicioEvento;
        this.servicioCliente = servicioCliente;
        this.ventana = ventanaGestion;
    }

    /**
	 * Método que se ejecuta después de la construcción del bean
	 * y realiza la conexión bidireccional entre el control y la ventana
	 */
    @PostConstruct
    public void init() {
        ventana.setControlGestion(this);
    }

    public void inicia(){
        ventana.inicia();
    }
}
