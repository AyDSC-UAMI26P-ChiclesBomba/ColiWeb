package mx.uam.ayd.proyecto.presentacion.calendario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.negocio.ServicioEvento;
import mx.uam.ayd.proyecto.negocio.modelo.Cliente;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;
import mx.uam.ayd.proyecto.presentacion.gestionarEventos.ControlGestionEvento;

/**
 * Módulo de control para la HU-1
 * @author JLCB
 */
@Component
public class ControlCalendario {

    private final ServicioEvento servicioEvento;
    private final VentanaCalendario ventana;
    private final ControlGestionEvento controlGestion;

    @Autowired
    public ControlCalendario(ServicioEvento servicioEvento, VentanaCalendario ventana, ControlGestionEvento controlGestion) {
        this.servicioEvento = servicioEvento;
        this.ventana = ventana;
        this.controlGestion = controlGestion;
    }

    /**
	 * Método que se ejecuta después de la construcción del bean
	 * y realiza la conexión bidireccional entre el control y la ventana
	 */
    @PostConstruct
    public void init() {
        ventana.setControlCalendario(this);
    }

    /**
	 * Inicia la historia de usuario
	 */
	public void iniciaCalendario() {
        LocalDate diaActual = servicioEvento.obtenerDiaActual();
        LocalDate diaLimite = servicioEvento.obtenerDiaLimite();
		List<Evento> eventos = servicioEvento.recuperaEventosPorMes(diaActual);
        List<Evento> eventosTotales = servicioEvento.recupera();
        ventana.muestra(eventos, eventosTotales, diaActual, diaLimite);
	}

    public void anteriorMes(LocalDate fecha){
        LocalDate fechaNueva = servicioEvento.disminuirMes(fecha);
        LocalDate diaLimite = servicioEvento.obtenerDiaLimite();
        List<Evento> eventos = servicioEvento.recuperaEventosPorMes(fechaNueva);
        List<Evento> eventosTotales = servicioEvento.recupera();
        ventana.muestra(eventos, eventosTotales, fechaNueva, diaLimite);
    }

    public void siguienteMes(LocalDate fecha){
        LocalDate fechaNueva = servicioEvento.aumentarMes(fecha);
        LocalDate diaLimite = servicioEvento.obtenerDiaLimite();
        List<Evento> eventos = servicioEvento.recuperaEventosPorMes(fechaNueva);
        List<Evento> eventosTotales = servicioEvento.recupera();
        ventana.muestra(eventos, eventosTotales, fechaNueva, diaLimite);
    }

    public Cliente obtenerCliente(Evento evento){
        return servicioEvento.obtenerCliente(evento);
    }

    public void eventoPresionado(Evento evento){
        List<Object> datos = new ArrayList<>();
        datos = servicioEvento.diaPresionado(evento);
        if(datos.get(0).toString().equals("FINALIZADO")){
            ventana.muestraDetallesFinalizado(datos, evento);
        }else{
            ventana.muestraDetallesEvento(datos, evento);
        }
    }

    public void seleccionaFecha(LocalDate fecha){
        boolean fechaDisponible = servicioEvento.fechaDisponible(fecha);
        if(fechaDisponible){
            ventana.habilitaContinuar(fecha);
        }else{
            ventana.muestraErrorFechaOcupada();
        }
    }

    public void abrirCreacionEvento(LocalDate fecha){
        System.out.println("Cambio a HU-5, creación");
        ventana.cierra();
        controlGestion.inicia();
    }

    public void verCotizacion(Evento evento){
        Object[] datos = new Object[2];
        datos = servicioEvento.obtenerCotizacionDetalles(evento);

        ventana.cierra();
        // Llama al control correspondiente
    }
    public void verGestion(Evento evento){
        System.out.println("Cambio a HU-5, modificación");
        ventana.cierra();
        controlGestion.inicia();
    }
    public void verPagos(Evento evento){
        ventana.cierra();
        // Llama al control correspondiente
    }

    public void verPublicar(Evento evento){
        ventana.cierra();
        // Llama al control correspondiente
    }
    public void verLiquidacion(Evento evento){
        ventana.cierra();
        // Llama al control correspondiente
    }
    public void verMobiliario(Evento evento){
        ventana.cierra();
        // Llama al control correspondiente
    }
}
