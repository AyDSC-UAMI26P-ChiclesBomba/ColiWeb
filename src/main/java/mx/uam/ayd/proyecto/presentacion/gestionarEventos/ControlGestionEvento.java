package mx.uam.ayd.proyecto.presentacion.gestionarEventos;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.negocio.ServicioCliente;
import mx.uam.ayd.proyecto.negocio.ServicioEvento;
import mx.uam.ayd.proyecto.negocio.modelo.Cliente;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;

/**
 * Módulo de control para la HU-5
 * @author JLCB
 */
@Component
public class ControlGestionEvento {
    
    private final ServicioEvento servicioEvento;
    private final ServicioCliente servicioCliente;
    private final VentanaGestionEvento ventana;

    @Autowired
    public ControlGestionEvento(ServicioEvento servicioEvento, ServicioCliente servicioCliente, VentanaGestionEvento ventanaGestion){
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
        ventana.setControlGestionEvento(this);
    }

    public void iniciaCreacionFecha(LocalDate fecha){
        List<Cliente> clientes = servicioCliente.obtenerClientes();
        ventana.muestraCreacionFecha(fecha, clientes);
    }
    public void iniciaModificacionEvento(Evento evento){

    }
}
