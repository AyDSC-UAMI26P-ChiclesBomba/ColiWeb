package mx.uam.ayd.proyecto.negocio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Repositorio temporal de publicaciones de la HU-10.
 *
 * Las publicaciones se almacenan únicamente en memoria.
 * Al cerrar la aplicación, todas desaparecen.
 */
@Component
public class RepositorioPublicacion {

    /**
     * Lista interna de publicaciones realizadas durante
     * la ejecución actual.
     */
    private final List<String> publicaciones =
            new ArrayList<>();

    /**
     * Guarda una publicación temporalmente.
     *
     * @param mensaje contenido de la publicación
     * @return true si se guardó correctamente
     */
    public boolean guardar(String mensaje) {

        if (mensaje == null || mensaje.isBlank()) {
            return false;
        }

        publicaciones.add(mensaje);

        return true;
    }

    /**
     * Obtiene todas las publicaciones guardadas.
     *
     * @return copia de la lista de publicaciones
     */
    public List<String> obtenerTodas() {

        return new ArrayList<>(publicaciones);
    }

    /**
     * Obtiene las publicaciones colocando primero
     * la más reciente.
     *
     * @return publicaciones ordenadas de reciente a antigua
     */
    public List<String> obtenerMasRecientesPrimero() {

        List<String> copia =
                new ArrayList<>(publicaciones);

        Collections.reverse(copia);

        return copia;
    }

    /**
     * Indica si no existen publicaciones.
     *
     * @return true si la lista está vacía
     */
    public boolean estaVacio() {
        return publicaciones.isEmpty();
    }

    /**
     * Obtiene la cantidad de publicaciones guardadas.
     *
     * @return cantidad de publicaciones
     */
    public int obtenerCantidad() {
        return publicaciones.size();
    }

    /**
     * Elimina todas las publicaciones temporales.
     */
    public void limpiar() {
        publicaciones.clear();
    }
}