package mx.uam.ayd.proyecto.negocio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.RepositorioEvento;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;
import mx.uam.ayd.proyecto.negocio.modelo.Evento.EstadoEvento;

/**
 * Servicio de negocio de la HU-10 Publicaciones.
 *
 * Se encarga de:
 *
 * 1. Consultar el último evento finalizado.
 * 2. Preparar los datos que se mostrarán en la ventana.
 * 3. Generar el mensaje inicial de la publicación.
 * 4. Guardar temporalmente las publicaciones.
 * 5. Consultar las publicaciones realizadas durante la ejecución.
 *
 * Las publicaciones solamente permanecen disponibles mientras
 * la aplicación está abierta. Al cerrar la aplicación, se pierden.
 */
@Service
public class ServicioPublicacion {

    /**
     * Repositorio utilizado para consultar los eventos
     * almacenados en la base de datos.
     */
    private final RepositorioEvento repositorioEvento;

    /**
     * Repositorio temporal utilizado para guardar las
     * publicaciones mientras la aplicación está abierta.
     *
     * Este repositorio no utiliza base de datos.
     */
    private final RepositorioPublicacion repositorioPublicacion;

    /**
     * Formato utilizado para mostrar las fechas.
     */
    private final DateTimeFormatter formatoFecha =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Formato utilizado para mostrar las horas.
     */
    private final DateTimeFormatter formatoHora =
            DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Constructor utilizado por Spring para inyectar los
     * repositorios necesarios.
     *
     * @param repositorioEvento repositorio de eventos
     * @param repositorioPublicacion repositorio temporal
     *                               de publicaciones
     */
    public ServicioPublicacion(
            RepositorioEvento repositorioEvento,
            RepositorioPublicacion repositorioPublicacion) {

        this.repositorioEvento = repositorioEvento;
        this.repositorioPublicacion = repositorioPublicacion;
    }

    /**
     * Obtiene el evento FINALIZADO más reciente.
     *
     * @return evento finalizado más reciente o un Optional vacío
     *         cuando no existe ninguno
     */
    public Optional<Evento> obtenerUltimoEventoFinalizado() {

        List<Evento> eventos =
                repositorioEvento.findByOrderByFechaAsc();

        if (eventos == null || eventos.isEmpty()) {
            return Optional.empty();
        }

        return eventos.stream()
                .filter(evento -> evento != null)
                .filter(evento ->
                        evento.getEstadoEvento()
                                == EstadoEvento.FINALIZADO)
                .filter(evento ->
                        evento.getFecha() != null
                                && evento.getHora() != null)
                .max(
                        Comparator.comparing(
                                evento -> LocalDateTime.of(
                                        evento.getFecha(),
                                        evento.getHora()
                                )
                        )
                );
    }

    /**
     * Obtiene los datos que se mostrarán en la ventana.
     *
     * El orden de la lista es:
     *
     * 0. Tipo de evento.
     * 1. Fecha.
     * 2. Hora.
     * 3. Lugar.
     * 4. Dirección.
     * 5. Detalles.
     *
     * @param evento evento que se desea mostrar
     * @return lista con los datos del evento
     */
    public List<String> obtenerDatosEvento(Evento evento) {

        List<String> datos = new ArrayList<>();

        if (evento == null) {
            return datos;
        }

        datos.add(
                evento.getTipoEvento() != null
                        ? evento.getTipoEvento().toString()
                        : ""
        );

        datos.add(
                evento.getFecha() != null
                        ? evento.getFecha().format(formatoFecha)
                        : ""
        );

        datos.add(
                evento.getHora() != null
                        ? evento.getHora().format(formatoHora)
                        : ""
        );

        datos.add(
                evento.getLugar() != null
                        ? evento.getLugar()
                        : ""
        );

        datos.add(
                evento.getDireccion() != null
                        ? evento.getDireccion()
                        : ""
        );

        datos.add(
                evento.getDetalles() != null
                        ? evento.getDetalles()
                        : ""
        );

        return datos;
    }

    /**
     * Obtiene directamente los datos del último evento
     * finalizado.
     *
     * @return datos del último evento finalizado o una lista vacía
     */
    public List<String> obtenerDatosUltimoEventoFinalizado() {

        Optional<Evento> evento =
                obtenerUltimoEventoFinalizado();

        if (evento.isEmpty()) {
            return new ArrayList<>();
        }

        return obtenerDatosEvento(evento.get());
    }

    /**
     * Genera el mensaje inicial de la publicación.
     *
     * @param evento evento que será publicado
     * @return mensaje inicial
     */
    public String generarMensaje(Evento evento) {

        if (evento == null) {
            return "";
        }

        String tipo =
                evento.getTipoEvento() != null
                        ? evento.getTipoEvento().toString()
                        : "EVENTO";

        String fecha =
                evento.getFecha() != null
                        ? evento.getFecha().format(formatoFecha)
                        : "";

        String lugar =
                evento.getLugar() != null
                        ? evento.getLugar()
                        : "";

        return "Evento: " + tipo
                + "\nFecha: " + fecha
                + "\nLugar: " + lugar;
    }

    /**
     * Genera el mensaje del último evento finalizado.
     *
     * @return mensaje generado o una cadena vacía
     */
    public String generarMensajeUltimoEventoFinalizado() {

        Optional<Evento> evento =
                obtenerUltimoEventoFinalizado();

        if (evento.isEmpty()) {
            return "";
        }

        return generarMensaje(evento.get());
    }

    /**
     * Guarda temporalmente la publicación del evento.
     *
     * La publicación se conserva únicamente mientras la
     * aplicación permanezca abierta.
     *
     * No se utiliza una base de datos y no existe persistencia
     * entre ejecuciones.
     *
     * @param mensaje contenido de la publicación
     * @return true si la publicación fue guardada correctamente;
     *         false si el mensaje estaba vacío
     */
    public boolean generarPublicacion(String mensaje) {

        if (mensaje == null || mensaje.isBlank()) {
            return false;
        }

        boolean publicacionGuardada =
                repositorioPublicacion.guardar(mensaje);

        if (!publicacionGuardada) {
            return false;
        }

        System.out.println("===== PUBLICACIÓN GUARDADA =====");
        System.out.println(mensaje);
        System.out.println("================================");

        return true;
    }

    /**
     * Obtiene todas las publicaciones realizadas durante
     * la ejecución actual.
     *
     * La publicación más reciente aparecerá primero.
     *
     * @return lista de publicaciones temporales
     */
    public List<String> obtenerPublicaciones() {

        return repositorioPublicacion
                .obtenerMasRecientesPrimero();
    }

    /**
     * Indica si todavía no existen publicaciones realizadas.
     *
     * @return true si no hay publicaciones; false si ya existe
     *         al menos una
     */
    public boolean noHayPublicaciones() {

        return repositorioPublicacion.estaVacio();
    }

    /**
     * Obtiene la cantidad de publicaciones realizadas durante
     * la ejecución actual.
     *
     * @return cantidad de publicaciones temporales
     */
    public int obtenerCantidadPublicaciones() {

        return repositorioPublicacion.obtenerCantidad();
    }
}