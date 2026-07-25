package mx.uam.ayd.proyecto.presentacion.gestionarEventos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.negocio.ServicioCliente;
import mx.uam.ayd.proyecto.negocio.ServicioEvento;
import mx.uam.ayd.proyecto.negocio.modelo.Cliente;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;
import mx.uam.ayd.proyecto.negocio.modelo.Evento.TipoEvento;
import mx.uam.ayd.proyecto.presentacion.calendario.ControlCalendario;

/**
 * Módulo de control para la HU-5
 * @author JLCB
 */
@Component
public class ControlGestionEvento {
    
    private final ServicioEvento servicioEvento;
    private final ServicioCliente servicioCliente;
    private final VentanaGestionEvento ventana;
    private final ControlCalendario controlCalendario;

    @Autowired
    public ControlGestionEvento(ServicioEvento servicioEvento, ServicioCliente servicioCliente, VentanaGestionEvento ventanaGestion, @Lazy ControlCalendario controlCalendario){
        this.servicioEvento = servicioEvento;
        this.servicioCliente = servicioCliente;
        this.ventana = ventanaGestion;
        this.controlCalendario = controlCalendario;
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
        List<Cliente> clientes = servicioCliente.recupera();
        ventana.muestraCreacionFecha(fecha, clientes);
    }

    public void seleccionaCliente(String nombre){
        try {
            String numero = servicioCliente.obtieneInfoCliente(nombre);
            ventana.clienteUsado(numero);
        } catch (Exception e) {
            ventana.clienteNuevo();
        }
    }    

    public void guardaEvento(TipoEvento tipo, String nombre, String num, LocalDate fecha, LocalTime hora, String lugar, String direccion, String referencias, String notas){
        boolean exito = servicioEvento.guardaEvento(nombre, num, fecha, tipo, hora, lugar, direccion, referencias, "imagen", notas);
        if(exito){
            System.out.println("Se guardó el evento");
        }else{
            ventana.muestraErrorEventoExistente();
        }
    }

    
    public void iniciaModificacionEvento(Evento evento){
        // Inicia Modificación de Evento
    }

    public void regresar(){
        controlCalendario.iniciaCalendario();
        ventana.cierra();
    }
}
