package mx.uam.ayd.proyecto.presentacion.Contrato;

import java.io.File;
import java.net.URL;

import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;

/**
 * Ventana correspondiente a la Historia de Usuario 6:
 * Gestión de Contratos.
 *
 * Esta clase pertenece a la capa de presentación.
 *
 * Sus responsabilidades son:
 *
 * 1. Cargar la interfaz definida en ventana-contrato.fxml.
 * 2. Mostrar las cláusulas del contrato seleccionado.
 * 3. Permitir al usuario editar las cláusulas.
 * 4. Enviar al controlador las solicitudes de guardado.
 * 5. Permitir actualizar la plantilla general.
 * 6. Permitir seleccionar la ubicación del archivo PDF.
 * 7. Mostrar mensajes informativos y mensajes de error.
 *
 * La ventana no contiene lógica de negocio ni accede directamente
 * al repositorio. Todas las operaciones se delegan a ControlContrato.
 *
 * La firma física del contrato no se administra en esta clase.
 */
@Component
public class VentanaContrato {

    /**
     * Área de texto definida en el archivo FXML.
     *
     * En este componente se muestran y editan las cláusulas
     * correspondientes al contrato del evento actual.
     *
     * Debe coincidir con:
     *
     * fx:id="textAreaClausulas"
     */
    @FXML
    private TextArea textAreaClausulas;

    /**
     * Controlador de la Historia de Usuario 6.
     *
     * La ventana utiliza esta referencia para enviar las acciones
     * realizadas por el usuario.
     *
     * Por ejemplo:
     *
     * - Guardar cláusulas.
     * - Actualizar la plantilla.
     * - Generar el archivo PDF.
     */
    private ControlContrato control;

    /**
     * Evento cuyo contrato está siendo mostrado o editado.
     *
     * Se guarda temporalmente para utilizarlo cuando el usuario
     * presiona Guardar o Generar PDF.
     */
    private Evento eventoActual;

    /**
     * Ventana de JavaFX utilizada por esta Historia de Usuario.
     *
     * El Stage se crea solamente la primera vez y después se reutiliza.
     */
    private Stage stage;

    /**
     * Asigna el controlador de contratos a esta ventana.
     *
     * Este método es llamado desde el método init()
     * de ControlContrato después de que Spring crea los componentes.
     *
     * Con esta asignación se establece la comunicación:
     *
     * VentanaContrato → ControlContrato
     *
     * @param control controlador de la Historia de Usuario 6
     */
    public void setControlContrato(ControlContrato control) {

        /*
         * Guarda la referencia al controlador para poder enviar
         * posteriormente las acciones realizadas por el usuario.
         */
        this.control = control;
    }

    /**
     * Muestra la ventana del contrato de un evento.
     *
     * Recibe el evento seleccionado y las cláusulas que deben
     * aparecer en el área de texto.
     *
     * Si la ventana todavía no ha sido creada, carga el archivo FXML.
     * Si ya existe, reutiliza el mismo Stage.
     *
     * @param evento evento cuyo contrato será administrado
     * @param clausulas cláusulas que se mostrarán en la ventana
     */
    public void muestraContrato(Evento evento, String clausulas) {

        /*
         * No se puede abrir un contrato sin tener
         * un evento seleccionado.
         */
        if (evento == null) {

            mostrarError(
                "Evento no válido",
                "No existe un evento seleccionado."
            );

            return;
        }

        /*
         * Guarda el evento recibido como el evento actual.
         *
         * Esta referencia será utilizada en las operaciones
         * de guardado y generación del PDF.
         */
        this.eventoActual = evento;

        /*
         * La ventana se crea únicamente la primera vez.
         *
         * En llamadas posteriores se reutiliza el Stage,
         * evitando cargar nuevamente el archivo FXML.
         */
        if (stage == null) {

            crearVentana();
        }

        /*
         * JavaFX TextArea no debe recibir un valor nulo.
         *
         * Si las cláusulas son nulas, se muestra una cadena vacía.
         * En caso contrario, se muestra el texto recibido.
         */
        textAreaClausulas.setText(
            clausulas == null ? "" : clausulas
        );

        /*
         * Coloca el cursor al inicio del texto para que el usuario
         * vea el contrato desde la primera línea.
         */
        textAreaClausulas.positionCaret(0);

        /*
         * Muestra la ventana.
         */
        stage.show();

        /*
         * Lleva la ventana al frente en caso de que estuviera
         * detrás de otra ventana de la aplicación.
         */
        stage.toFront();
    }

    /**
     * Carga el archivo FXML y crea la ventana JavaFX.
     *
     * Este método es privado porque únicamente se utiliza
     * dentro de VentanaContrato.
     *
     * @throws IllegalStateException si el archivo FXML no existe
     *                               o no puede cargarse
     */
    private void crearVentana() {

        try {

            /*
             * Busca el archivo FXML dentro de:
             *
             * src/main/resources/fxml/ventana-contrato.fxml
             */
            URL rutaFXML =
                getClass().getResource("/fxml/ventana-contrato.fxml");

            /*
             * Si getResource devuelve null, significa que el archivo
             * no fue encontrado en los recursos de la aplicación.
             */
            if (rutaFXML == null) {

                throw new IllegalStateException(
                    "No se encontró /fxml/ventana-contrato.fxml. "
                    + "Debe estar en src/main/resources/fxml/"
                );
            }

            /*
             * Crea el cargador encargado de leer el archivo FXML.
             */
            FXMLLoader loader = new FXMLLoader(rutaFXML);

            /*
             * Indica que esta misma instancia de VentanaContrato
             * funcionará como controlador del archivo FXML.
             *
             * Por esta razón, el archivo FXML no debe declarar
             * otro fx:controller diferente.
             */
            loader.setController(this);

            /*
             * Carga la estructura visual definida en el FXML.
             */
            Parent root = loader.load();

            /*
             * Crea el Stage principal de esta Historia de Usuario.
             */
            stage = new Stage();

            /*
             * Establece el título mostrado en la barra de la ventana.
             */
            stage.setTitle("Gestión de contrato");

            /*
             * Configura la ventana como modal.
             *
             * Mientras esta ventana esté abierta, el usuario debe
             * terminar la interacción antes de volver a otra ventana.
             */
            stage.initModality(Modality.APPLICATION_MODAL);

            /*
             * Crea la escena con los componentes cargados desde FXML
             * y la asigna al Stage.
             */
            stage.setScene(new Scene(root));

            /*
             * Intercepta el cierre realizado desde la X de la ventana.
             *
             * En lugar de destruir el Stage, llama cerrarVentana()
             * para ocultarlo y permitir reutilizarlo posteriormente.
             */
            stage.setOnCloseRequest(eventoCierre -> {

                /*
                 * Evita que JavaFX destruya automáticamente la ventana.
                 */
                eventoCierre.consume();

                /*
                 * Ejecuta el mismo procedimiento utilizado
                 * por el botón Cancelar.
                 */
                cerrarVentana();
            });

        } catch (Exception e) {

            /*
             * Muestra un mensaje comprensible para el usuario.
             */
            mostrarError(
                "Error al abrir la ventana",
                "No fue posible cargar ventana-contrato.fxml."
            );

            /*
             * Convierte el error original en una excepción
             * de estado de la aplicación.
             *
             * La causa original se conserva dentro de la excepción
             * para facilitar el diagnóstico del problema.
             */
            throw new IllegalStateException(
                "No fue posible cargar la ventana de contratos.",
                e
            );
        }
    }

    /**
     * Se ejecuta cuando el usuario presiona el botón
     * Guardar contrato.
     *
     * Guarda únicamente las cláusulas asociadas al evento actual.
     * Esta operación no modifica la plantilla general.
     */
    @FXML
    private void handleGuardar() {

        /*
         * Comprueba que la ventana tenga un controlador asignado
         * y que exista un evento actual.
         */
        if (!configuracionValida()) {

            return;
        }

        /*
         * Obtiene el contenido escrito por el usuario
         * dentro del área de texto.
         */
        String clausulas = textAreaClausulas.getText();

        /*
         * Evita guardar un contrato sin cláusulas.
         *
         * isBlank también detecta textos formados únicamente
         * por espacios o saltos de línea.
         */
        if (clausulas == null || clausulas.isBlank()) {

            mostrarError(
                "Cláusulas no válidas",
                "Las cláusulas no pueden estar vacías."
            );

            return;
        }

        try {

            /*
             * Envía las cláusulas al controlador.
             *
             * El controlador delegará la operación
             * a ServicioContrato, quien realizará el guardado.
             */
            control.guardarClausulas(
                eventoActual,
                clausulas
            );

            /*
             * Informa al usuario que la operación terminó
             * correctamente.
             */
            mostrarInformacion(
                "Contrato guardado",
                "Las cláusulas del contrato se guardaron correctamente."
            );

        } catch (IllegalArgumentException e) {

            /*
             * Captura problemas relacionados con datos inválidos,
             * por ejemplo un evento nulo o cláusulas vacías.
             */
            mostrarError(
                "No se pudo guardar el contrato",
                e.getMessage()
            );

        } catch (IllegalStateException e) {

            /*
             * Captura problemas ocurridos durante la persistencia
             * de la información.
             */
            mostrarError(
                "Error al guardar",
                e.getMessage()
            );
        }
    }

    /**
     * Se ejecuta cuando el usuario presiona
     * el botón Guardar plantilla.
     *
     * Guarda el contenido actual del área de texto como
     * la plantilla general para contratos posteriores.
     *
     * Esta operación no modifica contratos anteriores.
     */
    @FXML
    private void handleGuardarPlantilla() {

        /*
         * Comprueba que el controlador haya sido asignado.
         *
         * Sin controlador no es posible enviar la solicitud
         * a la capa de negocio.
         */
        if (control == null) {

            mostrarError(
                "Error de configuración",
                "No se ha asignado el controlador de contratos."
            );

            return;
        }

        /*
         * Obtiene el texto que será utilizado
         * como nueva plantilla general.
         */
        String nuevaPlantilla = textAreaClausulas.getText();

        /*
         * Evita guardar una plantilla nula, vacía
         * o formada solamente por espacios.
         */
        if (nuevaPlantilla == null || nuevaPlantilla.isBlank()) {

            mostrarError(
                "Plantilla no válida",
                "La plantilla no puede estar vacía."
            );

            return;
        }

        /*
         * Crea una alerta de confirmación para evitar
         * que la plantilla general sea reemplazada accidentalmente.
         */
        Alert confirmacion = new Alert(
            Alert.AlertType.CONFIRMATION
        );

        /*
         * Configura el título de la alerta.
         */
        confirmacion.setTitle("Guardar plantilla");

        /*
         * Explica al usuario la operación que está por realizarse.
         */
        confirmacion.setHeaderText(
            "¿Desea actualizar la plantilla general?"
        );

        /*
         * Aclara que el cambio se aplicará solamente
         * a contratos posteriores.
         */
        confirmacion.setContentText(
            "Esta plantilla será utilizada en los contratos siguientes. "
            + "Los contratos anteriores no serán modificados."
        );

        /*
         * Muestra la alerta y espera la respuesta del usuario.
         *
         * El resultado será verdadero únicamente
         * si el usuario presiona Aceptar.
         */
        boolean aceptado = confirmacion.showAndWait()
            .filter(respuesta -> respuesta == ButtonType.OK)
            .isPresent();

        /*
         * Si el usuario cancela la confirmación,
         * no se realiza ningún cambio.
         */
        if (!aceptado) {

            return;
        }

        try {

            /*
             * Envía la nueva plantilla al controlador.
             *
             * El controlador delegará la escritura del archivo
             * a ServicioContrato.
             */
            control.actualizarPlantilla(nuevaPlantilla);

            /*
             * Informa que la plantilla fue actualizada.
             */
            mostrarInformacion(
                "Plantilla actualizada",
                "La plantilla se utilizará en los contratos siguientes."
            );

        } catch (IllegalArgumentException e) {

            /*
             * Captura errores relacionados con una plantilla inválida.
             */
            mostrarError(
                "No se pudo actualizar la plantilla",
                e.getMessage()
            );

        } catch (IllegalStateException e) {

            /*
             * Captura errores ocurridos al escribir
             * el archivo de la plantilla.
             */
            mostrarError(
                "Error al actualizar la plantilla",
                e.getMessage()
            );
        }
    }

    /**
     * Se ejecuta cuando el usuario presiona
     * el botón Generar PDF.
     *
     * Permite seleccionar el nombre y la ubicación
     * donde se guardará el contrato.
     */
    @FXML
    private void handleGenerarPDF() {

        /*
         * Comprueba que exista el controlador
         * y un evento actualmente seleccionado.
         */
        if (!configuracionValida()) {

            return;
        }

        /*
         * Crea el selector de archivos utilizado
         * para elegir la ubicación del PDF.
         */
        FileChooser selector = new FileChooser();

        /*
         * Establece el título mostrado
         * en el selector de archivos.
         */
        selector.setTitle("Guardar contrato en PDF");

        /*
         * Define el nombre inicial sugerido.
         */
        String nombreArchivo = "contrato";

        /*
         * Si el evento tiene una fecha, la agrega al nombre
         * para facilitar la identificación del archivo.
         */
        if (eventoActual.getFecha() != null) {

            nombreArchivo += "-" + eventoActual.getFecha();
        }

        /*
         * Agrega la extensión del formato PDF.
         */
        nombreArchivo += ".pdf";

        /*
         * Coloca el nombre sugerido en el selector.
         */
        selector.setInitialFileName(nombreArchivo);

        /*
         * Limita la selección a archivos con extensión .pdf.
         */
        selector.getExtensionFilters().add(
            new FileChooser.ExtensionFilter(
                "Archivos PDF",
                "*.pdf"
            )
        );

        /*
         * Muestra el selector de archivos.
         *
         * Si el usuario presiona Cancelar, el resultado será null.
         */
        File archivo = selector.showSaveDialog(stage);

        /*
         * Si no se eligió un archivo, termina la operación
         * sin generar ningún documento.
         */
        if (archivo == null) {

            return;
        }

        /*
         * Obtiene la ruta completa seleccionada por el usuario.
         */
        String rutaArchivo = archivo.getAbsolutePath();

        /*
         * Asegura que el nombre final termine con .pdf,
         * aunque el usuario no haya escrito la extensión.
         */
        if (!rutaArchivo.toLowerCase().endsWith(".pdf")) {

            rutaArchivo += ".pdf";
        }

        try {

            /*
             * Solicita al controlador la generación del documento.
             *
             * El controlador delegará la operación
             * a ServicioContrato.
             */
            control.generarPDF(
                eventoActual,
                rutaArchivo
            );

            /*
             * Informa al usuario la ubicación
             * donde fue guardado el archivo.
             */
            mostrarInformacion(
                "PDF generado",
                "El contrato se guardó correctamente en:\n"
                + rutaArchivo
            );

        } catch (IllegalArgumentException e) {

            /*
             * Captura errores relacionados con datos inválidos,
             * por ejemplo una ruta vacía o un evento nulo.
             */
            mostrarError(
                "No se pudo generar el PDF",
                e.getMessage()
            );

        } catch (IllegalStateException e) {

            /*
             * Captura errores ocurridos durante
             * la creación o escritura del documento.
             */
            mostrarError(
                "Error al generar el PDF",
                e.getMessage()
            );
        }
    }

    /**
     * Verifica que la ventana esté preparada para realizar
     * una operación sobre el contrato.
     *
     * Comprueba:
     *
     * 1. Que exista un controlador asignado.
     * 2. Que exista un evento actualmente seleccionado.
     *
     * @return true si la configuración es válida;
     *         false si falta el controlador o el evento
     */
    private boolean configuracionValida() {

        /*
         * Sin controlador, la ventana no puede comunicarse
         * con la capa de negocio.
         */
        if (control == null) {

            mostrarError(
                "Error de configuración",
                "No se ha asignado el controlador de contratos."
            );

            return false;
        }

        /*
         * Sin evento actual, no es posible guardar cláusulas
         * ni generar el contrato.
         */
        if (eventoActual == null) {

            mostrarError(
                "Evento no seleccionado",
                "No existe un evento para realizar esta operación."
            );

            return false;
        }

        /*
         * La ventana tiene todos los elementos necesarios
         * para continuar con la operación.
         */
        return true;
    }

    /**
     * Se ejecuta cuando el usuario presiona
     * el botón Cancelar.
     */
    @FXML
    private void handleCancelar() {

        /*
         * Cierra la interacción actual sin destruir el Stage.
         */
        cerrarVentana();
    }

    /**
     * Oculta la ventana y limpia la referencia
     * al evento actual.
     *
     * Se utiliza hide() en lugar de close() para poder
     * reutilizar el mismo Stage posteriormente.
     */
    private void cerrarVentana() {

        /*
         * Oculta el Stage solamente si ya fue creado.
         */
        if (stage != null) {

            stage.hide();
        }

        /*
         * Informa al controlador que terminó
         * la interacción con la ventana.
         */
        if (control != null) {

            control.termina();
        }

        /*
         * Elimina la referencia al evento anterior.
         *
         * Esto evita que una operación posterior utilice
         * accidentalmente un evento que ya no está seleccionado.
         */
        eventoActual = null;
    }

    /**
     * Muestra una alerta informativa al usuario.
     *
     * @param titulo título de la alerta
     * @param mensaje contenido que se mostrará
     */
    private void mostrarInformacion(
            String titulo,
            String mensaje) {

        /*
         * Crea una alerta de tipo información.
         */
        Alert alerta = new Alert(
            Alert.AlertType.INFORMATION
        );

        /*
         * Configura los textos de la alerta.
         */
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        /*
         * Muestra la alerta y espera a que el usuario la cierre.
         */
        alerta.showAndWait();
    }

    /**
     * Muestra una alerta de error al usuario.
     *
     * @param titulo título de la alerta
     * @param mensaje descripción del problema
     */
    private void mostrarError(
            String titulo,
            String mensaje) {

        /*
         * Crea una alerta de tipo error.
         */
        Alert alerta = new Alert(
            Alert.AlertType.ERROR
        );

        /*
         * Configura el título y elimina
         * el encabezado adicional.
         */
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);

        /*
         * Evita mostrar un mensaje nulo o completamente vacío.
         *
         * Si no existe un mensaje específico, se utiliza
         * una descripción genérica.
         */
        alerta.setContentText(
            mensaje == null || mensaje.isBlank()
                ? "Ocurrió un error inesperado."
                : mensaje
        );

        /*
         * Muestra la alerta y espera a que el usuario la cierre.
         */
        alerta.showAndWait();
    }
}