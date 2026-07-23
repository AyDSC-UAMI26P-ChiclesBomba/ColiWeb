package mx.uam.ayd.proyecto.presentacion.calendario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.negocio.ServicioEvento;
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

    /**
     * Se recibe una fecha y se consigue el mes anterior junto a lo necesario para mostrar el mes anterior a la fecha
     * @param fecha Es la fecha de la que se requiere el mes anterior
     */
    public void anteriorMes(LocalDate fecha){
        LocalDate fechaNueva = servicioEvento.disminuirMes(fecha);
        LocalDate diaLimite = servicioEvento.obtenerDiaLimite();
        List<Evento> eventos = servicioEvento.recuperaEventosPorMes(fechaNueva);
        List<Evento> eventosTotales = servicioEvento.recupera();
        ventana.muestra(eventos, eventosTotales, fechaNueva, diaLimite);
    }
    /**
     * Se recibe una fecha y se consigue el mes siguiente junto a lo necesario para mostrar el mes siguiente a la fecha
     * @param fecha Es la fecha de la que se quiere el mes siguiente
     */
    public void siguienteMes(LocalDate fecha){
        LocalDate fechaNueva = servicioEvento.aumentarMes(fecha);
        LocalDate diaLimite = servicioEvento.obtenerDiaLimite();
        List<Evento> eventos = servicioEvento.recuperaEventosPorMes(fechaNueva);
        List<Evento> eventosTotales = servicioEvento.recupera();
        ventana.muestra(eventos, eventosTotales, fechaNueva, diaLimite);
    }

    /**
     * Manda a servicio que busque los datos que serán necesarios para mostrar los detalles y regresa a ventana dependiendo del tipo de evento que se da
     * @param evento evento del que se requiere la información
     */
    public void eventoPresionado(Evento evento){
        List<Object> datos = new ArrayList<>();
        datos = servicioEvento.diaPresionado(evento);
        if(datos.get(0).toString().equals("FINALIZADO")){
            ventana.muestraDetallesFinalizado(datos, evento);
        }else{
            ventana.muestraDetallesEvento(datos, evento);
        }
    }

    /**
     * Toma una fecha y verifica si está disponible en el repositorio (llamando a servicio) y decide si se muestra un error o se habilita Continuar
     * @param fecha
     */
    public void seleccionaFecha(LocalDate fecha){
        boolean fechaDisponible = servicioEvento.fechaDisponible(fecha);
        if(fechaDisponible){
            ventana.habilitaContinuar(fecha);
        }else{
            ventana.muestraErrorFechaOcupada();
        }
    }

    /**
     * Se cambia a la HU-5 para crear un evento
     */
    public void abrirCreacionEvento(LocalDate fecha){
        System.out.println("Cambio a HU-5, creación");
        ventana.cierra();
        controlGestion.inicia();
    }

    /**
     * Se cambia a la HU-2 para ver una cotización
     */
    public void verCotizacion(Evento evento){
        Object[] datos = new Object[2];
        datos = servicioEvento.obtenerCotizacionDetalles(evento);

        ventana.cierra();
        // Llama al control correspondiente
    }
    /**
     * Se cambia a gestionar un evento en la HU-5
     * @param evento
     */
    public void verGestion(Evento evento){
        System.out.println("Cambio a HU-5, modificación");
        ventana.cierra();
        controlGestion.inicia();
    }
    /**
     * Se manda a ver pagos
     */
    public void verPagos(Evento evento){
        ventana.cierra();
        // Llama al control correspondiente
    }
    /**
     * Se manda a ver publicar
     * @param evento
     */
    public void verPublicar(Evento evento){
        ventana.cierra();
        // Llama al control correspondiente
    }
    /**
     * Se manda a ver liquidación
     * @param evento
     */
    public void verLiquidacion(Evento evento){
        ventana.cierra();
        // Llama al control correspondiente
    }
    /**
     * Se manda a ver mobiliario
     * @param evento
     */
    public void verMobiliario(Evento evento){
        ventana.cierra();
        // Llama al control correspondiente
    }
}
