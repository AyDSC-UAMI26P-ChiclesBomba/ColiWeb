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

    // ------------- Métodos en la creación -------------
    /**
     * Manda a llamar a la creación de un evento en la fecha que le es enviada
     * @param fecha
     */
    public void iniciaCreacion(LocalDate fecha){
        List<Cliente> clientes = servicioCliente.recupera();
        ventana.muestraCreacion(fecha, clientes);
    }
    /**
     * Manda servicio a obtener el número de teléfono del cliente dado. Si existe el cliente entonces manda a clienteUsado, si no, a clienteNuevo
     * @param nombre Es el nombre al cliente que se buscará
     */
    public void seleccionaCliente(String nombre){
        try {
            String numero = servicioCliente.obtieneNumCliente(nombre);
            ventana.clienteUsado(numero);
        } catch (Exception e) {
            ventana.clienteNuevo();
        }
    }    
    /**
     * Lleva el proceso en qué hacer al momento de intentar crear un evento
     */
    public void guardaEvento(TipoEvento tipo, String nombre, String num, LocalDate fecha, LocalTime hora, String lugar, String direccion, String referencias, String imagen, String notas){
        Cotizacion exito = servicioEvento.guardaEvento(nombre, num, fecha, tipo, hora, lugar, direccion, referencias, imagen, notas);
        if(exito != null){
            System.out.println("Se guardó el evento");
            controlCatalogo.inicia(exito);
        }else{
            ventana.muestraErrorEventoExistente();
        }
    }

    // ------------- Métodos en la modificación -------------
    /**
     * Manda a llamar a la modificación de un evento de acuerdo al mismo que le es enviado
     * @param evento
     */
    public void iniciaGestion(Evento evento){
        ventana.muestraModificacion(evento);
    }

    /**
     * Manda a Servicio a guardar el evento y a decidir qué hacer de acuerdo a si se logra guardar o no
     * @param evento Es el evento que se modificará
     * @param fecha Es la fecha a asignar al evento
     * @param tipoEvento Es el tipo del evento que se guardará
     * @param hora Es la nueva hora que se guardará en el evento
     * @param lugar Es el nuevo lugar del evento
     * @param direccion Es la nueva dirección del evento
     * @param referencias Es la aactualización en las referencias que se guardarán
     * @param imagen Es la imagen del evento
     * @param notas Son las nuevas notas del eveto
     * @param estadoEvento Es el nuevo estado del evento
     */
    public void modificaEvento(Evento evento, LocalDate fecha, TipoEvento tipoEvento, LocalTime hora, String lugar, String direccion, String referencias, String imagen, String notas, EstadoEvento estadoEvento) {
        boolean exito = servicioEvento.modificaEvento(evento, fecha, tipoEvento, hora, lugar, direccion, referencias, imagen, notas, estadoEvento);
        if(exito){
            System.out.println("Se modificó el evento");
            ventana.muestraModificacionExitosa();
        }else{
            System.err.println("Error al modificar el evento");
        }
    }

    /**
     * Se le solicitó la eliminación de un evento y manda a ventana a mostrar una ventana de confirmación
     */
    public void solicitaEliminacionEvento() {
        ventana.muestraConfirmacionEliminar();
    }
    /**
     * Se manda a servicio a eliminar el evento que le es recibido
     * @param evento
     */
    public void eliminaEvento(Evento evento) {
        boolean exito = servicioEvento.eliminaEvento(evento);
        if(exito){
            System.out.println("Se eliminó el evento");
            ventana.muestraEliminacionExitosa();
        }else{
            System.err.println("Error al eliminar el evento");
        }
    }

    /**
     * Manda a abrir el calendario en su respectivo control
     */
    public void abreCalendario(){
        controlCalendario.iniciaCalendario();
        ventana.cierra();
    }
}