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
import mx.uam.ayd.proyecto.negocio.modelo.Cotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;
import mx.uam.ayd.proyecto.negocio.modelo.Evento.EstadoEvento;
import mx.uam.ayd.proyecto.negocio.modelo.Evento.TipoEvento;
import mx.uam.ayd.proyecto.presentacion.calendario.ControlCalendario;
import mx.uam.ayd.proyecto.presentacion.catalogo.ControlCatalogo;

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
    private final ControlCatalogo controlCatalogo;

    @Autowired
    public ControlGestionEvento(ServicioEvento servicioEvento, ServicioCliente servicioCliente, VentanaGestionEvento ventanaGestion, @Lazy ControlCalendario controlCalendario, ControlCatalogo controlCatalogo){
        this.servicioEvento = servicioEvento;
        this.servicioCliente = servicioCliente;
        this.ventana = ventanaGestion;
        this.controlCalendario = controlCalendario;
        this.controlCatalogo = controlCatalogo;
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
        ventana.muestraCreacion(fecha, clientes);
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
        Cotizacion exito = servicioEvento.guardaEvento(nombre, num, fecha, tipo, hora, lugar, direccion, referencias, "imagen", notas);
        if(exito != null){
            System.out.println("Se guardó el evento");
            controlCatalogo.inicia(exito);
        }else{
            ventana.muestraErrorEventoExistente();
        }
    }

    
    public void iniciaModificacionEvento(Evento evento){
        ventana.muestraModificacion(evento);
    }
    public void modificaEvento(Evento evento, LocalDate fecha, TipoEvento tipoEvento, LocalTime hora, String lugar, String direccion, String referencias, String imagen, String notas, EstadoEvento estadoEvento) {
        boolean exito = servicioEvento.modificaEvento(evento, fecha, tipoEvento, hora, lugar, direccion, referencias, imagen, notas, estadoEvento);
        if(exito){
            System.out.println("Se modificó el evento");
            ventana.muestraModificacionExitosa();
        }else{
            System.err.println("Error al modificar el evento");
        }
    }

    public void solicitaEliminacionEvento() {
        ventana.muestraConfirmacionEliminar();
    }
    public void eliminaEvento(Evento evento) {
        boolean exito = servicioEvento.eliminaEvento(evento);
        if(exito){
            System.out.println("Se eliminó el evento");
            ventana.muestraEliminacionExitosa();
        }else{
            System.err.println("Error al eliminar el evento");
        }
    }

    public void abreCalendario(){
        controlCalendario.iniciaCalendario();
        ventana.cierra();
    }

    public void regresar(){
        controlCalendario.iniciaCalendario();
        ventana.cierra();
    }
}
