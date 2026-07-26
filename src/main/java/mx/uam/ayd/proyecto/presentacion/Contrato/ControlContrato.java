package mx.uam.ayd.proyecto.presentacion.Contrato;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.negocio.ServicioContrato;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;

/**
 * Controlador correspondiente a la Historia de Usuario 6:
 * Gestión de Contratos.
 *
 * Esta clase funciona como intermediaria entre la capa de presentación
 * y la capa de negocio.
 *
 * El flujo principal del módulo es:
 *
 * VentanaContrato
 *       ↓
 * ControlContrato
 *       ↓
 * ServicioContrato
 *
 * La ventana informa las acciones realizadas por el usuario.
 * El controlador recibe dichas acciones y las delega al servicio.
 *
 * Esta clase no contiene lógica de persistencia ni genera directamente
 * archivos PDF. Esas responsabilidades pertenecen a ServicioContrato.
 *
 * La firma física del contrato tampoco se administra en esta clase.
 */
@Component
public class ControlContrato {

    /**
     * Servicio encargado de ejecutar la lógica de negocio
     * relacionada con los contratos.
     *
     * Por medio de este objeto se consultan y actualizan cláusulas,
     * se modifica la plantilla general y se genera el archivo PDF.
     */
    private final ServicioContrato servicioContrato;

    /**
     * Ventana correspondiente a la Historia de Usuario 6.
     *
     * El controlador utiliza esta referencia para solicitar que se
     * muestre el contrato de un evento.
     */
    private final VentanaContrato ventana;

    /**
     * Constructor utilizado por Spring para realizar la inyección
     * de dependencias.
     *
     * Spring proporciona automáticamente una instancia de
     * ServicioContrato y una instancia de VentanaContrato.
     *
     * @param servicioContrato servicio que contiene la lógica de negocio
     *                        de los contratos
     * @param ventana ventana utilizada para mostrar y editar el contrato
     */
    @Autowired
    public ControlContrato(
            ServicioContrato servicioContrato,
            VentanaContrato ventana) {

        /*
         * Guarda la referencia al servicio para poder delegar
         * las operaciones de negocio.
         */
        this.servicioContrato = servicioContrato;

        /*
         * Guarda la referencia a la ventana para poder mostrar
         * la interfaz de gestión de contratos.
         */
        this.ventana = ventana;
    }

    /**
     * Método ejecutado automáticamente después de que Spring
     * termina de construir el controlador.
     *
     * Su función es entregar esta instancia de ControlContrato
     * a VentanaContrato.
     *
     * De esta manera, la ventana puede enviar al controlador
     * las acciones realizadas por el usuario.
     */
    @PostConstruct
    public void init() {

        /*
         * Establece la comunicación:
         *
         * VentanaContrato → ControlContrato
         */
        ventana.setControlContrato(this);
    }

    /**
     * Punto de entrada público de la Historia de Usuario 6.
     *
     * Este método es el único que necesitan utilizar otros módulos
     * para abrir la ventana de contratos.
     *
     * Recibe el evento seleccionado, obtiene sus cláusulas mediante
     * ServicioContrato y solicita a VentanaContrato que muestre
     * la información.
     *
     * @param evento evento cuyo contrato será administrado
     *
     * @throws IllegalArgumentException si el evento recibido es nulo
     */
    public void iniciaContrato(Evento evento) {

        /*
         * Un contrato siempre debe estar asociado a un evento.
         *
         * Si el evento es nulo, no es posible consultar, guardar
         * ni generar el contrato.
         */
        if (evento == null) {

            throw new IllegalArgumentException(
                "El evento no puede ser nulo."
            );
        }

        /*
         * Solicita al servicio las cláusulas correspondientes
         * al evento seleccionado.
         *
         * El servicio determina si debe devolver las cláusulas
         * particulares del evento o la plantilla general.
         */
        String clausulas =
            servicioContrato.obtenerClausulas(evento);

        /*
         * Envía a la ventana el evento seleccionado y las cláusulas
         * que deben mostrarse en el área de texto.
         */
        ventana.muestraContrato(
            evento,
            clausulas
        );
    }

    /**
     * Solicita el guardado de las cláusulas del contrato actual.
     *
     * Esta operación modifica únicamente el contrato asociado
     * al evento recibido.
     *
     * No modifica la plantilla general que se utilizará
     * en contratos posteriores.
     *
     * @param evento evento cuyo contrato será actualizado
     * @param clausulas nuevo texto de las cláusulas
     *
     * @throws IllegalArgumentException si los datos recibidos no son válidos
     * @throws IllegalStateException si ocurre un problema durante el guardado
     */
    public void guardarClausulas(
            Evento evento,
            String clausulas) {

        /*
         * Delega el guardado a ServicioContrato.
         *
         * El servicio realiza las validaciones necesarias
         * y persiste los cambios del evento.
         */
        servicioContrato.actualizarClausulas(
            evento,
            clausulas
        );
    }

    /**
     * Solicita la actualización de la plantilla general
     * de contratos.
     *
     * La nueva plantilla será utilizada en contratos posteriores.
     * Los contratos que ya tengan cláusulas particulares
     * no deben modificarse con esta operación.
     *
     * @param nuevaPlantilla nuevo contenido de la plantilla general
     *
     * @throws IllegalArgumentException si la plantilla no es válida
     * @throws IllegalStateException si no puede guardarse la plantilla
     */
    public void actualizarPlantilla(
            String nuevaPlantilla) {

        /*
         * Delega la actualización del archivo de plantilla
         * a ServicioContrato.
         */
        servicioContrato.actualizarPlantilla(
            nuevaPlantilla
        );
    }

    /**
     * Solicita la generación del contrato en formato PDF.
     *
     * El controlador no crea directamente el archivo.
     * La generación y escritura del PDF son responsabilidad
     * de ServicioContrato.
     *
     * @param evento evento cuyo contrato será generado
     * @param rutaArchivo ruta completa donde se guardará el PDF
     *
     * @throws IllegalArgumentException si el evento o la ruta no son válidos
     * @throws IllegalStateException si ocurre un error al generar el archivo
     */
    public void generarPDF(
            Evento evento,
            String rutaArchivo) {

        /*
         * Delega al servicio la creación y el guardado
         * del documento PDF.
         */
        servicioContrato.generarPDF(
            evento,
            rutaArchivo
        );
    }

    /**
     * Informa que terminó la interacción con la ventana
     * de contratos.
     *
     * Actualmente no es necesario ejecutar una operación adicional.
     * El método se conserva para mantener explícito el cierre
     * de la Historia de Usuario y permitir agregar posteriormente
     * alguna acción sin modificar VentanaContrato.
     */
    public void termina() {

        /*
         * Por el momento no se requiere ninguna acción.
         */
    }
}