package mx.uam.ayd.proyecto.presentacion.Publicacion;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.negocio.ServicioPublicacion;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;

/**
 * Controlador de la HU-10 Publicaciones.
 *
 * Coordina la comunicación entre la ventana de publicación
 * y el servicio que contiene la lógica de negocio.
 */
@Component
public class ControlPublicacion {

    /**
     * Servicio de negocio de publicaciones.
     */
    private final ServicioPublicacion servicioPublicacion;

    /**
     * Ventana gráfica de publicaciones.
     */
    private final VentanaPublicacion ventana;

    /**
     * Evento que actualmente se está preparando para publicar.
     */
    private Evento eventoSeleccionado;

    /**
     * Constructor utilizado por Spring para inyectar las dependencias.
     *
     * @param servicioPublicacion servicio de publicaciones
     * @param ventana ventana gráfica de publicaciones
     */
    public ControlPublicacion(
            ServicioPublicacion servicioPublicacion,
            VentanaPublicacion ventana) {

        this.servicioPublicacion = servicioPublicacion;
        this.ventana = ventana;
    }

    /**
     * Conecta la ventana con el controlador después de que
     * Spring crea ambos componentes.
     */
    @PostConstruct
    public void init() {
        ventana.setControlPublicacion(this);
    }

    /**
     * Abre la ventana utilizando automáticamente el evento
     * FINALIZADO más reciente de la base de datos.
     */
    public void iniciaPublicacion() {

        Optional<Evento> eventoFinalizado =
                servicioPublicacion.obtenerUltimoEventoFinalizado();

        if (eventoFinalizado.isEmpty()) {

            eventoSeleccionado = null;

            ventana.muestra(
                    List.of(),
                    ""
            );

            return;
        }

        iniciaPublicacion(eventoFinalizado.get());
    }

    /**
     * Abre la ventana utilizando un evento específico.
     *
     * @param evento evento que se desea publicar
     */
    public void iniciaPublicacion(Evento evento) {

        if (evento == null) {

            eventoSeleccionado = null;

            ventana.muestra(
                    List.of(),
                    ""
            );

            return;
        }

        eventoSeleccionado = evento;

        List<String> datosEvento =
                servicioPublicacion.obtenerDatosEvento(evento);

        String mensaje =
                servicioPublicacion.generarMensaje(evento);

        ventana.muestra(datosEvento, mensaje);
    }

    /**
     * Solicita al servicio generar la publicación.
     *
     * @param mensaje mensaje escrito por el usuario
     */
    public void publicarEvento(String mensaje) {

        boolean publicacionRealizada =
                servicioPublicacion.generarPublicacion(mensaje);

        if (publicacionRealizada) {
            ventana.muestraConfirmacion();
        }
    }

    /**
     * Obtiene el evento seleccionado.
     *
     * @return evento seleccionado o null cuando no existe
     */
    public Evento getEventoSeleccionado() {
        return eventoSeleccionado;
    }

    /**
     * Cierra la ventana de publicación.
     */
    public void cerrarVentana() {
        ventana.cierra();
    }
}
