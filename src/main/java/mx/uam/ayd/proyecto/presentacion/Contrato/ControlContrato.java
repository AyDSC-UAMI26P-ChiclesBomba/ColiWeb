package mx.uam.ayd.proyecto.presentacion.Contrato;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.negocio.ServicioContrato;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;

/**
 * Controlador de la HU-6.
 *
 * Sirve como intermediario entre la ventana y el servicio:
 *
 * VentanaContrato
 *       ↓
 * ControlContrato
 *       ↓
 * ServicioContrato
 *
 * La firma física del contrato no se administra aquí.
 */
@Component
public class ControlContrato {

    /**
     * Servicio que contiene la lógica de negocio del contrato.
     */
    private final ServicioContrato servicioContrato;

    /**
     * Ventana de la HU-6.
     */
    private final VentanaContrato ventana;

    /**
     * Constructor utilizado por Spring para inyectar
     * el servicio y la ventana.
     *
     * @param servicioContrato servicio de contratos
     * @param ventana ventana de contratos
     */
    @Autowired
    public ControlContrato(
            ServicioContrato servicioContrato,
            VentanaContrato ventana) {

        this.servicioContrato = servicioContrato;
        this.ventana = ventana;
    }

    /**
     * Se ejecuta después de que Spring crea el controlador.
     *
     * Entrega esta instancia del controlador a la ventana.
     */
    @PostConstruct
    public void init() {

        ventana.setControlContrato(this);
    }

    /**
     * Inicia la HU-6 mostrando el contrato de un evento.
     *
     * @param evento evento seleccionado
     */
   public void iniciaContrato(Evento evento) {

    System.out.println("=== ENTRÓ A ControlContrato.iniciaContrato ===");

    if (evento == null) {
        System.err.println("ERROR: el evento recibido es null");
        return;
    }

    System.out.println("Evento recibido: " + evento);

    String clausulas =
        servicioContrato.obtenerClausulas(evento);

    System.out.println("Cláusulas obtenidas: " + clausulas);
    System.out.println("Intentando abrir VentanaContrato...");

    ventana.muestraContrato(evento, clausulas);

    System.out.println("Terminó la llamada a muestraContrato");
}

    /**
     * Guarda las cláusulas únicamente en el contrato
     * del evento actual.
     *
     * No modifica la plantilla general.
     *
     * @param evento evento que será actualizado
     * @param clausulas nuevas cláusulas
     */
    public void guardarClausulas(
            Evento evento,
            String clausulas) {

        /*
         * Delega el guardado al servicio.
         */
        servicioContrato.actualizarClausulas(
            evento,
            clausulas
        );
    }

    /**
     * Actualiza la plantilla general que utilizarán
     * los contratos siguientes.
     *
     * @param nuevaPlantilla texto de la nueva plantilla
     */
    public void actualizarPlantilla(
            String nuevaPlantilla) {

        /*
         * Delega la actualización al servicio.
         */
        servicioContrato.actualizarPlantilla(
            nuevaPlantilla
        );
    }

    /**
     * Solicita la generación del PDF del contrato.
     *
     * @param evento evento cuyo contrato se generará
     * @param rutaArchivo ruta donde se guardará el PDF
     */
    public void generarPDF(
            Evento evento,
            String rutaArchivo) {

        /*
         * Delega la generación del archivo al servicio.
         */
        servicioContrato.generarPDF(
            evento,
            rutaArchivo
        );
    }

    /**
     * Finaliza la interacción con la ventana.
     *
     * Actualmente no necesita ejecutar alguna operación,
     * pero VentanaContrato lo llama cuando se cierra.
     */
    public void termina() {

        // Por ahora no se requiere ninguna acción.
    }
}