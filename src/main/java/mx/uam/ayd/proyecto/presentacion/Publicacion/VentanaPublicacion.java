package mx.uam.ayd.proyecto.presentacion.Publicacion;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.negocio.ServicioPublicacion;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * ===========================================================
 * VentanaPublicacion
 * ===========================================================
 *
 * Esta clase pertenece a la capa de presentación de la HU-10.
 *
 * Su responsabilidad es controlar los elementos gráficos de la
 * ventana de publicación.
 *
 * La ventana muestra:
 *
 * 1. Los datos del evento seleccionado.
 * 2. El mensaje que se desea publicar.
 * 3. El botón para confirmar la publicación.
 * 4. El botón para cancelar y cerrar la ventana.
 *
 * Esta clase no contiene lógica de negocio.
 * Cuando el usuario realiza una acción, la ventana llama al
 * ControlPublicacion para que el controlador decida qué hacer.
 */
@Component
public class VentanaPublicacion {

    /**
     * Ventana principal de JavaFX correspondiente a la HU-10.
     *
     * El Stage representa la ventana que se muestra en pantalla.
     */
    private Stage stage;

    /**
     * Controlador asociado a esta ventana.
     *
     * La ventana utiliza esta referencia para comunicar las
     * acciones realizadas por el usuario.
     */
    private ControlPublicacion control;

    /**
     * Servicio de negocio utilizado para consultar las publicaciones
     * guardadas temporalmente durante la ejecución actual.
     */
    private final ServicioPublicacion servicioPublicacion;

    /**
     * Indica si la interfaz gráfica ya fue creada.
     *
     * Esto evita cargar varias veces el mismo archivo FXML y
     * crear varias ventanas innecesariamente.
     */
    private boolean initialized = false;

    /*
     * =======================================================
     * ELEMENTOS GRÁFICOS DEL ARCHIVO FXML
     * =======================================================
     *
     * Cada atributo anotado con @FXML debe tener el mismo
     * fx:id dentro del archivo ventana-publicacion.fxml.
     */

    /**
     * Campo editable donde se muestra el tipo de evento.
     *
     * Ejemplo: BODA.
     */
    @FXML
    private TextField tipoEvento;

    /**
     * Selector editable de la fecha del evento.
     */
    @FXML
    private DatePicker fechaEvento;

    /**
     * Campo editable donde se muestra la hora del evento.
     *
     * Ejemplo: 18:30.
     */
    @FXML
    private TextField horaEvento;

    /**
     * Campo editable donde se muestra el lugar del evento.
     */
    @FXML
    private TextField lugarEvento;

    /**
     * Campo editable donde se muestra la dirección del evento.
     */
    @FXML
    private TextField direccionEvento;

    /**
     * Área editable donde se muestran los detalles adicionales.
     */
    @FXML
    private TextArea detallesEvento;

    /**
     * Área de texto donde se mostrará el mensaje inicial.
     *
     * El usuario podrá modificar este texto antes de presionar
     * el botón Publicar.
     */
    @FXML
    private TextArea mensajePublicacion;

    /**
     * Botón que confirma la publicación del evento.
     */
    @FXML
    private Button publicar;

    /**
     * Botón que cancela la operación y cierra la ventana.
     */
    @FXML
    private Button cancelar;

    /**
     * Botón que abre el explorador de archivos para elegir fotografías.
     */
    @FXML
    private Button agregarFotos;

    /**
     * Botón que permite consultar las publicaciones realizadas
     * durante la ejecución actual de la aplicación.
     */
    @FXML
    private Button verPublicaciones;

    /**
     * Contenedor donde se muestran las miniaturas de las fotografías.
     */
    @FXML
    private HBox contenedorFotos;

    /**
     * Mensaje que aparece cuando todavía no hay fotografías seleccionadas.
     */
    @FXML
    private Label mensajeSinFotos;

    /**
     * Fotografías seleccionadas temporalmente para la publicación.
     *
     * No se guardan en la base de datos ni modifican el modelo Evento.
     * La lista solamente existe mientras la aplicación está ejecutándose.
     */
    private final List<File> fotografiasSeleccionadas = new ArrayList<>();

    /**
     * Constructor vacío requerido para que Spring pueda crear
     * esta clase como componente.
     */
    public VentanaPublicacion(
            ServicioPublicacion servicioPublicacion) {

        this.servicioPublicacion = servicioPublicacion;
    }

    /**
     * Establece la referencia al controlador de la HU-10.
     *
     * Este método es llamado desde ControlPublicacion en su
     * método init().
     *
     * Gracias a esta conexión, la ventana puede notificar al
     * controlador cuando el usuario presiona un botón.
     *
     * @param control controlador asociado a esta ventana
     */
    public void setControlPublicacion(ControlPublicacion control) {
        this.control = control;
    }

    /**
     * Inicializa todos los elementos gráficos de la ventana.
     *
     * Este método:
     *
     * 1. Crea el Stage.
     * 2. Carga el archivo FXML.
     * 3. Asocia esta clase como controlador del FXML.
     * 4. Crea la escena.
     * 5. Aplica la hoja de estilos del proyecto.
     *
     * El método también verifica que la creación de la interfaz
     * ocurra dentro del hilo gráfico de JavaFX.
     */
    private void initializeUI() {

        /*
         * Si la ventana ya fue inicializada, no es necesario
         * volver a cargar el archivo FXML.
         */
        if (initialized) {
            return;
        }

        /*
         * JavaFX exige que las modificaciones gráficas se hagan
         * en su propio hilo de ejecución.
         *
         * Si actualmente no estamos en ese hilo, la operación se
         * programa mediante Platform.runLater().
         */
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::initializeUI);
            return;
        }

        try {
            /*
             * Se crea la ventana principal de la HU-10.
             */
            stage = new Stage();

            /*
             * Se establece el título que aparecerá en la parte
             * superior de la ventana.
             */
            stage.setTitle("ColiWeb - Publicación de evento");

            /*
             * Se agrega el logotipo del proyecto a la ventana.
             *
             * Se utiliza la misma ruta que emplea la ventana del
             * calendario.
             */
            stage.getIcons().add(
                    new javafx.scene.image.Image(
                            getClass().getResourceAsStream("/img/logo.png")
                    )
            );

            /*
             * Se carga el archivo FXML que contendrá la estructura
             * visual de la ventana.
             */
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/fxml/ventana-publicacion.fxml"
                    )
            );

            /*
             * Se indica que esta misma instancia será el
             * controlador del archivo FXML.
             *
             * Por esta razón no se debe colocar fx:controller
             * dentro del archivo FXML.
             */
            loader.setController(this);

            /*
             * Se carga el FXML y se crea la escena.
             *
             * Las medidas iniciales serán de 900 por 650 píxeles.
             */
            Scene scene = new Scene(loader.load(), 900, 650);

            /*
             * Se utiliza la hoja de estilos general del proyecto.
             */
            scene.getStylesheets().add(
                    getClass().getResource(
                            "/css/estilos.css"
                    ).toExternalForm()
            );

            /*
             * Se asigna la escena a la ventana.
             */
            stage.setScene(scene);

            /*
             * Se indica que la ventana ya quedó inicializada.
             */
            initialized = true;

        } catch (IOException e) {
            /*
             * Este error puede ocurrir si el archivo FXML no
             * existe, tiene errores o no puede cargarse.
             */
            e.printStackTrace();
        }
    }

    /**
     * Muestra la ventana de publicación con la información del
     * evento seleccionado.
     *
     * La lista recibida conserva el siguiente orden:
     *
     * Posición 0: tipo de evento.
     * Posición 1: fecha.
     * Posición 2: hora.
     * Posición 3: lugar.
     * Posición 4: dirección.
     * Posición 5: detalles.
     *
     * @param datos datos preparados por ServicioPublicacion
     * @param mensaje mensaje inicial de la publicación
     */
    public void muestra(List<String> datos, String mensaje) {

        /*
         * Se verifica que la operación ocurra dentro del hilo
         * gráfico de JavaFX.
         */
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> muestra(datos, mensaje));
            return;
        }

        /*
         * Se crea la interfaz si todavía no ha sido creada.
         */
        initializeUI();

        /*
         * Antes de acceder a las posiciones de la lista se
         * verifica que existan los seis datos esperados.
         */
        if (datos != null && datos.size() >= 6) {
            tipoEvento.setText(datos.get(0));

            /*
             * La fecha recibida por el servicio tiene el formato
             * día/mes/año. Se convierte a LocalDate para mostrarla
             * dentro del DatePicker.
             */
            DateTimeFormatter formatoFecha =
                    DateTimeFormatter.ofPattern("dd/MM/yyyy");

            try {
                fechaEvento.setValue(
                        LocalDate.parse(datos.get(1), formatoFecha)
                );
            } catch (DateTimeParseException e) {
                /*
                 * Si el texto no tiene el formato esperado, se deja
                 * el selector vacío en lugar de impedir que abra la ventana.
                 */
                fechaEvento.setValue(null);
            }

            horaEvento.setText(datos.get(2));
            lugarEvento.setText(datos.get(3));
            direccionEvento.setText(datos.get(4));
            detallesEvento.setText(datos.get(5));
        }

        /*
         * Se coloca el mensaje inicial dentro del área de texto.
         *
         * Si el mensaje fuera null, se coloca una cadena vacía.
         */
        mensajePublicacion.setText(
                mensaje != null ? mensaje : ""
        );

        /*
         * Se muestra la ventana.
         */
        stage.show();
    }


    /**
     * Abre el explorador de archivos y permite seleccionar una o varias
     * fotografías para acompañar la publicación del evento.
     *
     * Se aceptan archivos PNG, JPG y JPEG. Si el usuario cancela la
     * selección, la lista actual de fotografías permanece sin cambios.
     *
     * @param event evento generado al presionar Agregar fotografías
     */
    @FXML
    private void botonAgregarFotos(ActionEvent event) {

        FileChooser selector = new FileChooser();
        selector.setTitle("Seleccionar fotografías del evento");

        selector.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Archivos de imagen",
                        "*.png",
                        "*.jpg",
                        "*.jpeg"
                )
        );

        List<File> archivos = selector.showOpenMultipleDialog(stage);

        if (archivos == null || archivos.isEmpty()) {
            return;
        }

        for (File archivo : archivos) {
            if (!fotografiasSeleccionadas.contains(archivo)) {
                fotografiasSeleccionadas.add(archivo);
            }
        }

        actualizarVistaPrevia();
    }

    /**
     * Reconstruye la sección de fotografías y muestra una miniatura por
     * cada archivo seleccionado. Cada miniatura incluye un botón Quitar.
     */
    private void actualizarVistaPrevia() {

        if (contenedorFotos == null) {
            return;
        }

        contenedorFotos.getChildren().clear();

        if (fotografiasSeleccionadas.isEmpty()) {
            if (mensajeSinFotos != null) {
                mensajeSinFotos.setText("No hay fotografías seleccionadas.");
                contenedorFotos.getChildren().add(mensajeSinFotos);
            }
            return;
        }

        for (File archivo : fotografiasSeleccionadas) {

            Image imagen = new Image(
                    archivo.toURI().toString(),
                    120,
                    100,
                    true,
                    true
            );

            ImageView vistaImagen = new ImageView(imagen);
            vistaImagen.setFitWidth(120);
            vistaImagen.setFitHeight(100);
            vistaImagen.setPreserveRatio(true);
            vistaImagen.setSmooth(true);

            Button eliminar = new Button("Quitar");
            eliminar.setMaxWidth(Double.MAX_VALUE);
            eliminar.setOnAction(accion -> {
                fotografiasSeleccionadas.remove(archivo);
                actualizarVistaPrevia();
            });

            Label nombreArchivo = new Label(archivo.getName());
            nombreArchivo.setMaxWidth(120);
            nombreArchivo.setWrapText(true);

            VBox tarjetaFoto = new VBox(5);
            tarjetaFoto.setPrefWidth(125);
            tarjetaFoto.getChildren().addAll(
                    vistaImagen,
                    nombreArchivo,
                    eliminar
            );

            contenedorFotos.getChildren().add(tarjetaFoto);
        }
    }

    /**
     * Método ejecutado cuando el usuario presiona el botón
     * Publicar.
     *
     * La ventana obtiene el texto escrito por el usuario y lo
     * manda al ControlPublicacion.
     *
     * @param event evento generado por JavaFX al presionar el botón
     */
   @FXML
private void botonPublicar(ActionEvent event) {

    DateTimeFormatter formato =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    String tipo = tipoEvento.getText() != null
            ? tipoEvento.getText().trim()
            : "";

    String fecha = fechaEvento.getValue() != null
            ? fechaEvento.getValue().format(formato)
            : "";

    String hora = horaEvento.getText() != null
            ? horaEvento.getText().trim()
            : "";

    String lugar = lugarEvento.getText() != null
            ? lugarEvento.getText().trim()
            : "";

    String direccion = direccionEvento.getText() != null
            ? direccionEvento.getText().trim()
            : "";

    String detalles = detallesEvento.getText() != null
            ? detallesEvento.getText().trim()
            : "";

    String mensaje = mensajePublicacion.getText() != null
            ? mensajePublicacion.getText().trim()
            : "";

    StringBuilder publicacion = new StringBuilder();

    publicacion.append("Evento: ")
            .append(tipo)
            .append("\n\n");

    publicacion.append("Fecha: ")
            .append(fecha)
            .append("\n\n");

    publicacion.append("Hora: ")
            .append(hora)
            .append("\n\n");

    publicacion.append("Lugar: ")
            .append(lugar)
            .append("\n\n");

    publicacion.append("Dirección: ")
            .append(direccion)
            .append("\n\n");

    publicacion.append("Detalles: ")
            .append(detalles)
            .append("\n\n");

    publicacion.append("Mensaje:\n")
            .append(mensaje);

    control.publicarEvento(publicacion.toString());
}

    /**
     * Muestra las publicaciones realizadas durante la ejecución actual.
     *
     * @param event evento generado al presionar Ver publicaciones
     */
    @FXML
    private void botonVerPublicaciones(ActionEvent event) {

        List<String> publicaciones =
                servicioPublicacion.obtenerPublicaciones();

        Stage ventanaHistorial = new Stage();
        ventanaHistorial.setTitle(
                "ColiWeb - Publicaciones realizadas"
        );

        VBox contenido = new VBox(12);
        contenido.setStyle("-fx-padding: 18;");

        Label titulo = new Label("Publicaciones realizadas");
        titulo.setStyle(
                "-fx-font-size: 20px; -fx-font-weight: bold;"
        );

        contenido.getChildren().add(titulo);

        if (publicaciones == null || publicaciones.isEmpty()) {

            Label sinPublicaciones = new Label(
                    "Todavía no hay publicaciones realizadas "
                            + "durante esta ejecución."
            );

            sinPublicaciones.setWrapText(true);
            contenido.getChildren().add(sinPublicaciones);

        } else {

            int numero = publicaciones.size();

            for (String mensaje : publicaciones) {

                Label encabezado =
                        new Label("Publicación " + numero);

                encabezado.setStyle(
                        "-fx-font-weight: bold;"
                );

                TextArea contenidoPublicacion =
                        new TextArea(mensaje);

                contenidoPublicacion.setEditable(false);
                contenidoPublicacion.setWrapText(true);
                contenidoPublicacion.setPrefRowCount(5);

                contenido.getChildren().addAll(
                        encabezado,
                        contenidoPublicacion
                );

                numero--;
            }
        }

        Button cerrar = new Button("Cerrar");
        cerrar.setOnAction(
                accion -> ventanaHistorial.close()
        );

        contenido.getChildren().add(cerrar);

        ScrollPane desplazamiento =
                new ScrollPane(contenido);

        desplazamiento.setFitToWidth(true);

        Scene escena =
                new Scene(desplazamiento, 650, 500);

        escena.getStylesheets().add(
                getClass().getResource(
                        "/css/estilos.css"
                ).toExternalForm()
        );

        ventanaHistorial.setScene(escena);
        ventanaHistorial.show();
    }

    /**
     * Método ejecutado cuando el usuario presiona el botón
     * Cancelar.
     *
     * No se realiza ninguna publicación y simplemente se solicita
     * al controlador que cierre la ventana.
     *
     * @param event evento generado al presionar el botón
     */
    @FXML
    private void botonCancelar(ActionEvent event) {
        control.cerrarVentana();
    }

    /**
     * Muestra una ventana emergente para confirmar que la
     * publicación fue realizada.
     *
     * Este método es llamado por ControlPublicacion después de
     * que ServicioPublicacion devuelve true.
     */
    public void muestraConfirmacion() {

        /*
         * Se crea una alerta informativa.
         */
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);

        /*
         * Se configura el título de la alerta.
         */
        alerta.setTitle("Publicación realizada");

        /*
         * Se elimina el encabezado adicional para que la alerta
         * sea más sencilla.
         */
        alerta.setHeaderText(null);

        /*
         * Se establece el mensaje que verá el usuario.
         */
        alerta.setContentText(
                "El evento fue publicado correctamente."
        );

        /*
         * Se muestra la alerta y se espera a que el usuario la
         * cierre.
         */
        alerta.showAndWait();
    }

    /**
     * Cierra la ventana de publicación.
     *
     * Antes de llamar a close(), se comprueba que el Stage haya
     * sido creado.
     */
    public void cierra() {
        if (stage != null) {
            stage.close();
        }
    }
}