package mx.uam.ayd.proyecto.presentacion.Contrato;

import java.io.File;

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
 * Ventana correspondiente a la HU-6: Contratos.
 *
 * Esta clase pertenece a la capa de presentación.
 *
 * Se encarga de:
 *
 * 1. Mostrar las cláusulas del contrato.
 * 2. Permitir la edición de las cláusulas.
 * 3. Solicitar que se guarde el contrato actual.
 * 4. Solicitar que se actualice la plantilla general.
 * 5. Elegir dónde guardar el PDF.
 * 6. Mostrar mensajes al usuario.
 *
 * La firma física del contrato no se administra en esta clase.
 */
@Component
public class VentanaContrato {

    /**
     * Área de texto definida en el archivo FXML.
     *
     * Debe coincidir con:
     *
     * fx:id="textAreaClausulas"
     */
    @FXML
    private TextArea textAreaClausulas;

    /**
     * Controlador de la historia de usuario.
     *
     * La ventana envía las acciones del usuario al controlador.
     */
    private ControlContrato control;

    /**
     * Evento cuyo contrato se está editando actualmente.
     */
    private Evento eventoActual;

    /**
     * Ventana principal de JavaFX para esta historia de usuario.
     */
    private Stage stage;

    /**
     * Asigna el controlador de contratos a esta ventana.
     *
     * Este método es llamado desde el método init()
     * de ControlContrato.
     *
     * @param control controlador de la HU-6
     */
    public void setControlContrato(ControlContrato control) {

        this.control = control;
    }

    /**
     * Muestra la ventana del contrato.
     *
     * Recibe el evento seleccionado y las cláusulas que deben
     * aparecer en el área de texto.
     *
     * @param evento evento cuyo contrato se editará
     * @param clausulas cláusulas actuales del contrato
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
         * Guarda el evento para utilizarlo cuando el usuario
         * presione Guardar o Generar PDF.
         */
        this.eventoActual = evento;

        /*
         * La ventana se crea solamente la primera vez.
         *
         * Si ya existe, se reutiliza.
         */
        if (stage == null) {

            crearVentana();
        }

        /*
         * Si las cláusulas son nulas, se muestra un texto vacío.
         *
         * Esto evita enviar null al TextArea.
         */
        if (clausulas == null) {

            textAreaClausulas.setText("");

        } else {

            textAreaClausulas.setText(clausulas);
        }

        /*
         * Coloca el cursor al inicio del texto.
         */
        textAreaClausulas.positionCaret(0);

        /*
         * Muestra la ventana.
         */
        stage.show();

        /*
         * Lleva la ventana al frente.
         */
        stage.toFront();
    }

    /**
     * Carga el archivo FXML y crea la ventana.
     *
     * Es privado porque solamente se utiliza dentro
     * de VentanaContrato.
     */
    private void crearVentana() {

        try {
            // ===== INICIO CORRECCIÓN DE CARGA FXML =====
            java.net.URL rutaFXML =
                getClass().getResource("/fxml/ventana-contrato.fxml");

            if (rutaFXML == null) {
                throw new IllegalStateException(
                    "No se encontró /fxml/ventana-contrato.fxml. "
                    + "Debe estar en src/main/resources/fxml/"
                );
            }

            FXMLLoader loader = new FXMLLoader(rutaFXML);
            loader.setController(this);

            Parent root = loader.load();

            stage = new Stage();
            stage.setTitle("Gestión de contrato");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            // ===== FIN CORRECCIÓN DE CARGA FXML =====

        } catch (Exception e) {

            System.err.println("ERROR AL ABRIR VENTANA CONTRATO");
            e.printStackTrace();

            mostrarError(
                "Error al abrir la ventana",
                "No fue posible cargar ventana-contrato.fxml."
            );

            throw new IllegalStateException(
                "No fue posible cargar la ventana de contratos.",
                e
            );
        }
    }

    /**
     * Se ejecuta al presionar el botón Guardar contrato.
     *
     * Guarda únicamente las cláusulas del evento actual.
     *
     * No modifica la plantilla general.
     */
    @FXML
    private void handleGuardar() {

        /*
         * Verifica que la ventana tenga un controlador.
         */
        if (control == null) {

            mostrarError(
                "Error de configuración",
                "No se ha asignado el controlador de contratos."
            );

            return;
        }

        /*
         * Verifica que exista un evento actual.
         */
        if (eventoActual == null) {

            mostrarError(
                "Evento no seleccionado",
                "No existe un evento para guardar el contrato."
            );

            return;
        }

        /*
         * Obtiene el texto escrito dentro del TextArea.
         */
        String clausulas = textAreaClausulas.getText();

        /*
         * Realiza una validación sencilla antes
         * de enviar la información al controlador.
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
             * El controlador las enviará al ServicioContrato.
             */
            control.guardarClausulas(
                eventoActual,
                clausulas
            );

            /*
             * Informa que el guardado terminó correctamente.
             */
            mostrarInformacion(
                "Contrato guardado",
                "Las cláusulas del contrato se guardaron correctamente."
            );

        } catch (IllegalArgumentException e) {

            /*
             * Captura errores relacionados con datos inválidos.
             */
            mostrarError(
                "No se pudo guardar el contrato",
                e.getMessage()
            );

        } catch (IllegalStateException e) {

            /*
             * Captura errores ocurridos al guardar
             * en la base de datos.
             */
            mostrarError(
                "Error al guardar",
                e.getMessage()
            );
        }
    }

    /**
     * Se ejecuta al presionar Guardar plantilla.
     *
     * Guarda el contenido actual como la plantilla que utilizarán
     * los contratos que se creen posteriormente.
     *
     * No modifica contratos anteriores.
     */
    @FXML
    private void handleGuardarPlantilla() {

        /*
         * Comprueba que exista el controlador.
         */
        if (control == null) {

            mostrarError(
                "Error de configuración",
                "No se ha asignado el controlador de contratos."
            );

            return;
        }

        /*
         * Obtiene el texto que se desea guardar como plantilla.
         */
        String nuevaPlantilla = textAreaClausulas.getText();

        /*
         * Evita guardar una plantilla vacía.
         */
        if (nuevaPlantilla == null || nuevaPlantilla.isBlank()) {

            mostrarError(
                "Plantilla no válida",
                "La plantilla no puede estar vacía."
            );

            return;
        }

        /*
         * Muestra una alerta de confirmación.
         */
        Alert confirmacion = new Alert(
            Alert.AlertType.CONFIRMATION
        );

        confirmacion.setTitle("Guardar plantilla");

        confirmacion.setHeaderText(
            "¿Desea actualizar la plantilla general?"
        );

        confirmacion.setContentText(
            "Esta plantilla será utilizada en los contratos siguientes. "
            + "Los contratos anteriores no serán modificados."
        );

        /*
         * Espera la respuesta del usuario.
         *
         * El resultado será true solamente si presiona Aceptar.
         */
        boolean aceptado = confirmacion.showAndWait()
            .filter(respuesta -> respuesta == ButtonType.OK)
            .isPresent();

        /*
         * Si el usuario cancela, no se realiza ningún cambio.
         */
        if (!aceptado) {

            return;
        }

        try {

            /*
             * Envía el texto al controlador.
             */
            control.actualizarPlantilla(nuevaPlantilla);

            /*
             * Muestra la confirmación del guardado.
             */
            mostrarInformacion(
                "Plantilla actualizada",
                "La plantilla se utilizará en los contratos siguientes."
            );

        } catch (IllegalArgumentException e) {

            mostrarError(
                "No se pudo actualizar la plantilla",
                e.getMessage()
            );

        } catch (IllegalStateException e) {

            mostrarError(
                "Error al actualizar la plantilla",
                e.getMessage()
            );
        }
    }

    /**
     * Se ejecuta al presionar Generar PDF.
     *
     * Permite que el usuario seleccione el nombre y la ubicación
     * donde se guardará el archivo.
     */
    @FXML
    private void handleGenerarPDF() {

        /*
         * Comprueba que exista el controlador.
         */
        if (control == null) {

            mostrarError(
                "Error de configuración",
                "No se ha asignado el controlador de contratos."
            );

            return;
        }

        /*
         * Comprueba que exista un evento.
         */
        if (eventoActual == null) {

            mostrarError(
                "Evento no seleccionado",
                "No existe un evento para generar el contrato."
            );

            return;
        }

        /*
         * Crea el selector de archivos.
         */
        FileChooser selector = new FileChooser();

        /*
         * Establece el título del selector.
         */
        selector.setTitle("Guardar contrato en PDF");

        /*
         * Crea el nombre inicial del archivo.
         */
        String nombreArchivo = "contrato";

        /*
         * Si el evento tiene fecha, se agrega al nombre.
         */
        if (eventoActual.getFecha() != null) {

            nombreArchivo += "-" + eventoActual.getFecha();
        }

        /*
         * Agrega la extensión PDF.
         */
        nombreArchivo += ".pdf";

        /*
         * Coloca el nombre sugerido.
         */
        selector.setInitialFileName(nombreArchivo);

        /*
         * Configura el selector para archivos PDF.
         */
        selector.getExtensionFilters().add(
            new FileChooser.ExtensionFilter(
                "Archivos PDF",
                "*.pdf"
            )
        );

        /*
         * Abre el selector para guardar el archivo.
         *
         * Si el usuario presiona Cancelar, el resultado será null.
         */
        File archivo = selector.showSaveDialog(stage);

        if (archivo == null) {

            return;
        }

        /*
         * Obtiene la ruta completa seleccionada.
         */
        String rutaArchivo = archivo.getAbsolutePath();

        /*
         * Asegura que el nombre termine con .pdf.
         */
        if (!rutaArchivo.toLowerCase().endsWith(".pdf")) {

            rutaArchivo += ".pdf";
        }

        try {

            /*
             * Solicita al controlador que genere el PDF.
             */
            control.generarPDF(
                eventoActual,
                rutaArchivo
            );

            /*
             * Muestra la ubicación donde fue guardado.
             */
            mostrarInformacion(
                "PDF generado",
                "El contrato se guardó correctamente en:\n"
                + rutaArchivo
            );

        } catch (IllegalArgumentException e) {

            mostrarError(
                "No se pudo generar el PDF",
                e.getMessage()
            );

        } catch (IllegalStateException e) {

            mostrarError(
                "Error al generar el PDF",
                e.getMessage()
            );
        }
    }

    /**
     * Se ejecuta al presionar el botón Cancelar.
     */
    @FXML
    private void handleCancelar() {

        cerrarVentana();
    }

    /**
     * Oculta la ventana y limpia el evento actual.
     */
    private void cerrarVentana() {

        /*
         * Oculta el Stage si ya fue creado.
         *
         * Se utiliza hide() para poder reutilizarlo posteriormente.
         */
        if (stage != null) {

            stage.hide();
        }

        /*
         * Informa al controlador que terminó la interacción.
         */
        if (control != null) {

            control.termina();
        }

        /*
         * Limpia la referencia para evitar utilizar
         * accidentalmente el evento anterior.
         */
        eventoActual = null;
    }

    /**
     * Muestra una alerta informativa.
     *
     * @param titulo título de la alerta
     * @param mensaje texto que se mostrará
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

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        /*
         * Muestra la alerta y espera que el usuario la cierre.
         */
        alerta.showAndWait();
    }

    /**
     * Muestra una alerta de error.
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

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);

        /*
         * Evita mostrar null o un mensaje completamente vacío.
         */
        if (mensaje == null || mensaje.isBlank()) {

            alerta.setContentText(
                "Ocurrió un error inesperado."
            );

        } else {

            alerta.setContentText(mensaje);
        }

        /*
         * Muestra la alerta.
         */
        alerta.showAndWait();
    }
}