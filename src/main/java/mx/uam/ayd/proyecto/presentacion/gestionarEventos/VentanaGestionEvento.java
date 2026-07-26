package mx.uam.ayd.proyecto.presentacion.gestionarEventos;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.modelo.Cliente;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;
import mx.uam.ayd.proyecto.negocio.modelo.Evento.EstadoEvento;
import mx.uam.ayd.proyecto.negocio.modelo.Evento.TipoEvento;

@Component
public class VentanaGestionEvento {
    private Stage stage;
    private ControlGestionEvento control;
    private boolean initialized = false;

	// Formato para la fecha
	private DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("es", "MX"));

    public VentanaGestionEvento(){}

	/* ---------------- Variables FXML ---------------- */
	// Ventanas
	@FXML
	private VBox creacionBox;
	@FXML
	private VBox modificacionBox;

	// Errores
	@FXML
	private Label errorDatosIncompletos;
	@FXML
	private Label errorEventoExistente;
	@FXML
	private Label errorDatosIncompletosModificacion;

	// Recuadros Creación
	@FXML
	private ComboBox<TipoEvento> textTipoCreacion;
	@FXML
	private ComboBox<String> textNombreCreacion;
	@FXML
	private TextField textTelefonoCreacion;
	@FXML
	private TextField textFechaCreacion;
	@FXML
	private Spinner<Integer> horaCreacion;
	@FXML
	private Spinner<Integer> minutoCreacion;
	@FXML
	private TextField textLugarCreacion;
	@FXML
	private TextField textDireccionCreacion;
	@FXML
	private TextField textReferenciasCreacion;
	@FXML
	private ImageView imageVisualizacionCreacion;
	@FXML
	private TextArea textNotasCreacion;
	@FXML
	private VBox subirFotoCreacion;

	// Recuadros Modificación
	@FXML
	private ComboBox<TipoEvento> textTipoModificacion;
	@FXML
	private TextField textNombreModificacion;
	@FXML
	private TextField textTelefonoModificacion;
	@FXML
	private TextField textFechaModificacion;
	@FXML
	private Spinner<Integer> horaModificacion;
	@FXML
	private Spinner<Integer> minutoModificacion;
	@FXML
	private TextField textLugarModificacion;
	@FXML
	private TextField textDireccionModificacion;
	@FXML
	private TextField textReferenciasModificacion;
	@FXML
	private ImageView imageVisualizacionModificacion;
	private File archivoImagenSeleccionado;
	@FXML
	private TextArea textNotasModificacion;
	@FXML
	private ToggleButton switchConfirmadoModificacion;
	@FXML
	private ToggleButton switchBorradorModificacion;
	@FXML
	private Button botonModificar;
	@FXML
	private Button botonEliminar;
	@FXML
	private VBox subirFotoModificacion;

	/**
	 * Initialize UI components on the JavaFX application thread
	 */
	private void initializeUI() {
		if (initialized) {
			return;
		}
		
		// Create UI only if we're on JavaFX thread
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(this::initializeUI);
			return;
		}
		
		try {
			stage = new Stage();
			stage.setTitle("ColiWeb: Gestionar Evento");
			stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/img/logo.png")));
			
			// Load FXML
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-gestion-evento.fxml"));
			loader.setController(this);
			Scene scene = new Scene(loader.load(), 1024, 768);
			scene.getStylesheets().add(getClass().getResource("/css/estilos.css").toExternalForm());
			
			stage.setScene(scene);
			stage.setMaximized(true);
			
			initialized = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    /**
	 * Establece el controlador asociado a esta ventana
	 * 
	 * @param control El controlador asociado
	 */
	public void setControlGestionEvento(ControlGestionEvento control) {
		this.control = control;
	}

	/**
	 * Inicializa la ventana y algunos componentes, tratando los hilos
	 */
	private void inicializacion() {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(this::inicializacion);
			return;
		}
		initializeUI();

		// Se da qué valores pueden tener los Spinners (usados en la hora y minuto), necesarios ahorita para evitar errores al reiniciar los valores
		SpinnerValueFactory<Integer> horasFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23, 00);
		SpinnerValueFactory<Integer> minutosFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59, 00);
		horasFactory.setWrapAround(true);
		minutosFactory.setWrapAround(true);
		horaCreacion.setValueFactory(horasFactory);
        minutoCreacion.setValueFactory(minutosFactory);
		horaModificacion.setValueFactory(horasFactory);
		minutoModificacion.setValueFactory(minutosFactory);
		System.out.println("Se inician los Spinner para la creación y modificación");
	}

	// --------------- MÉTODOS DE MUESTRAS CREACIÓN Y MODIFICACIÓN ---------------
	/**
	 * Método encargado de iniciar la pantalla de Creación, reseteando e iniciando los datos que deben ir en las opciones elegibles y de escritura para el usuario
	 * @param fecha Es la fecha sobre la que se trabaja la creación del evento
	 * @param clientes Es la lista de clientes que ya existen en el repositorio
	 */
    public void muestraCreacion(LocalDate fecha, List<Cliente> clientes){
        inicializacion();
		System.out.println("Se inicia la ventana de creación de evento");

		// Nos aseguramos de que el contenedor de creación sea el visible y el manejado en lugar del de modificación
		creacionBox.setVisible(true);
		creacionBox.setManaged(true);
		modificacionBox.setVisible(false);
		modificacionBox.setManaged(false);

		// Se reinician los valores que tienen los textos
		reiniciarValores();
		
		// Creamos una lista con los nombres de los clientes para poder asignarla al ComboBox que los contendrá
		textTipoCreacion.setItems(FXCollections.observableArrayList(TipoEvento.values()));
		List<String> nombresClientes = new ArrayList<>();
		for(Cliente cliente : clientes){
			nombresClientes.add(cliente.getNombre());
		}
		textNombreCreacion.setItems(FXCollections.observableArrayList(nombresClientes));
		textFechaCreacion.setText(fecha.format(formatoFecha));
		textFechaCreacion.setUserData(fecha);
		System.out.println("Se asignan los valores posibles para el tipo de evento y los nombres de clientes");
		
		stage.show();
    }

	/**
	 * Método encargado de iniciar la pantalla de Modificación, reseteando e iniciando los datos que deben ir en las opciones elegibles y de escritura para el usuario
	 * @param evento Es el evento que se modificará y por lo tanto, del que se toman los datos
	 */
	public void muestraModificacion(Evento evento) {
        inicializacion();
		System.out.println("Se inicia la ventana de creación de evento");

		// Nos aseguramos de que el contenedor de modificación sea el visible y el manejado en lugar del de creación
		creacionBox.setVisible(false);
		creacionBox.setManaged(false);
		modificacionBox.setVisible(true);
		modificacionBox.setManaged(true);

		// Se reinician los valores que tienen los textos
		reiniciarValores();

		// Damos los valores posibles para el tipo de evento
		textTipoModificacion.setItems(FXCollections.observableArrayList(TipoEvento.values()));

		// Se asignan los valores que ya tiene el evento a los textos correspondientes
		textTipoModificacion.setValue(evento.getTipoEvento());
		textNombreModificacion.setText(evento.getCliente().getNombre());
		textNombreModificacion.setEditable(false); // Nos aseguramos de que no sea editable puesto que el cliente ya está asignado
		textTelefonoModificacion.setText(evento.getCliente().getNumTelefono());
		textTelefonoModificacion.setEditable(false); // Nos aseguramos de que no sea editable debido a que el cliente ya está asignado
		textFechaModificacion.setText(evento.getFecha().format(formatoFecha));
		textFechaModificacion.setUserData(evento.getFecha());
		horaModificacion.getValueFactory().setValue(evento.getHora().getHour());
		minutoModificacion.getValueFactory().setValue(evento.getHora().getMinute());
		textLugarModificacion.setText(evento.getLugar());
		textDireccionModificacion.setText(evento.getDireccion());
		textReferenciasModificacion.setText(evento.getReferencias());
		textNotasModificacion.setText(evento.getDetalles());
		if(evento.getEstadoEvento().equals(EstadoEvento.CONFIRMADO)){
			switchConfirmadoModificacion.setSelected(true);
		}else{
			switchBorradorModificacion.setSelected(true);
		}

		// Muestra la imagen si es que el evento tiene una ruta guardada
        String rutaImagen = evento.getVisualRecinto();
        if (rutaImagen != null && !rutaImagen.isBlank()) {
            File archivoFisico = new File(rutaImagen);
            if (archivoFisico.exists()) {
                Image img = new Image(archivoFisico.toURI().toString());
                imageVisualizacionModificacion.setImage(img);
				subirFotoModificacion.setVisible(false);
            }
        } else {
            // Si no tiene imagen, la deja en blanco
            imageVisualizacionModificacion.setImage(null);
			subirFotoModificacion.setVisible(true);
        }

		System.out.println("Se asignan los valores del evento a los textos correspondientes");

		botonModificar.setUserData(evento); // Guardamos el evento en el botón para poder usarlo al presionarlo
		botonEliminar.setUserData(evento); // Guardamos el evento en el botón para poder usarlo al presionarlo
		
		stage.show();
	}

	// --------------- Ventanas Emergentes de avisos ---------------
	/**
	 * Muestra una ventana emergente de aviso de que la modificación fue exitosa
	 */
	public void muestraModificacionExitosa() {
		System.out.println("Se muestra el mensaje de modificación exitosa");
		Alert alerta = new Alert(Alert.AlertType.INFORMATION);
		alerta.setTitle("Modificación Exitosa");
		alerta.setHeaderText(null);
		alerta.setContentText("Se modificó el evento exitosamente.");
		alerta.showAndWait();
		System.out.println("Se modificó el evento exitosamente");
		control.abreCalendario();
	}
	public void muestraEliminacionExitosa() {
		Alert alerta = new Alert(Alert.AlertType.INFORMATION);
		alerta.setTitle("Eliminación Exitosa");
		alerta.setHeaderText(null);
		alerta.setContentText("Se eliminó el evento exitosamente.");
		alerta.showAndWait();
		System.out.println("Se eliminó el evento exitosamente");
		control.abreCalendario();
	}
	public void muestraConfirmacionEliminar() {
		Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
		alerta.setTitle("Confirmación de Eliminación");
		alerta.setHeaderText(null);
		alerta.setContentText("¿Está seguro de que desea eliminar este evento?");
		alerta.showAndWait().ifPresent(response -> {
			if (response == javafx.scene.control.ButtonType.OK) {
				control.eliminaEvento((Evento) botonEliminar.getUserData());
				System.out.println("Evento eliminado");
			} else {
				System.out.println("Eliminación cancelada");
			}
		});
	}


	// --------------- Métodos de apoyo en las muestras ---------------
	/**
	 * Método encargado de reiniciar los valores de los textos y ocultar los errores
	 */
	private void reiniciarValores(){
		System.out.println("Se reinician los valores de los textos y se ocultan los errores");
		// Los errores son limpiados y ocultados
		errorDatosIncompletos.setVisible(false);
		errorDatosIncompletos.setManaged(false);
		errorEventoExistente.setVisible(false);
		errorEventoExistente.setManaged(false);
		errorDatosIncompletosModificacion.setVisible(false);
		errorDatosIncompletosModificacion.setManaged(false);
		
		// Se limpian todos los textos de creación
		textTipoCreacion.setValue(null);
		textNombreCreacion.setValue(null);
		textTelefonoCreacion.setText(null);
		textFechaCreacion.setText(null);
		textFechaCreacion.setUserData(null);
		horaCreacion.getValueFactory().setValue(00);
		minutoCreacion.getValueFactory().setValue(00);
		textLugarCreacion.setText(null);
		textDireccionCreacion.setText(null);
		textReferenciasCreacion.setText(null);
		textNotasCreacion.setText(null);
		imageVisualizacionCreacion.setImage(null);;
		subirFotoCreacion.setVisible(true);

		// Se limpian todos los textos de modificación
		textTipoModificacion.setValue(null);
		textNombreModificacion.setText(null);
		textTelefonoModificacion.setText(null);
		textFechaModificacion.setText(null);
		textFechaModificacion.setUserData(null);
		horaModificacion.getValueFactory().setValue(00);
		minutoModificacion.getValueFactory().setValue(00);
		textLugarModificacion.setText(null);
		textDireccionModificacion.setText(null);
		textReferenciasModificacion.setText(null);
		textNotasModificacion.setText(null);
		switchConfirmadoModificacion.setSelected(false);
		switchBorradorModificacion.setSelected(false);
		imageVisualizacionModificacion.setImage(null);
		subirFotoModificacion.setVisible(true);

		// Se limpian los eventos guardados en los botones de modificación y eliminación
		botonModificar.setUserData(null);
		botonEliminar.setUserData(null);
	}
	/**
	 * Método encargado de validar los datos que son obligatorios para un evento. Dependiendo de si se cumplen todos, se manda true, de lo contrario, false y se manda el error correspondiente
	 * @return es el estado conseguido de la validación de todos los campos obligatorios
	 */
	private boolean validaDatos(ComboBox<TipoEvento> textTipo, ComboBox<String> textNombre, TextField textTelefono, TextField textFecha, Spinner<Integer> hora, Spinner<Integer> minuto, TextField textDireccion, TextField textFieldNombre, ToggleButton switchConfirmado, ToggleButton switchBorrador){
		// Validación Tipo de Evento
		if(textTipo.getValue() == null){
			muestraErrorDatos();
			return false;
		}
		// Validación Nombre del Cliente
		try {
			if(textNombre.getValue() == null || textNombre.getValue().isBlank()){
				muestraErrorDatos();
				return false;
			}
		} catch (Exception e) {
			if(textFieldNombre.getText() == null || textFieldNombre.getText().isBlank()){
				muestraErrorDatos();
				return false;
			}
		}
		// Validación Número telefónico del cliente
		if(textTelefono.getText() == null || textTelefono.getText().isBlank() || !(textTelefono.getText().matches("^[0-9]{10}$"))){
			muestraErrorDatos();
			return false;
		}
		// Validación Fecha del evento
		if(textFecha.getText() == null || textFecha.getText().isBlank()){
			muestraErrorDatos();
			return false;
		}
		// Validación Hora
		if(hora.getValue() == null || !(hora.getValue()<=23 && hora.getValue()>=00)){
			muestraErrorDatos();
			return false;
		}
		// Validación Minutos
		if(minuto.getValue() == null || !(minuto.getValue()<=59 && minuto.getValue()>=00)){
			muestraErrorDatos();
			return false;
		}
		// Validación Dirección del evento
		if(textDireccion.getText() == null || textDireccion.getText().isBlank()){
			muestraErrorDatos();
			return false;
		}
		// Validación de que al menos uno de los switches esté seleccionado en caso del modo de modificación
		if(switchConfirmado != null && switchBorrador != null) {
			if(!switchConfirmado.isSelected() && !switchBorrador.isSelected()){
				muestraErrorDatos();
				return false;
			}
		}

		return true;
	}

	// --------------- Métodos que muestran errores ---------------
	/**
	 * Muestra el error de que los datos no están completos
	 */
	private void muestraErrorDatos(){
		System.err.println("Los datos no están completos");
		errorDatosIncompletos.setVisible(true);
		errorDatosIncompletos.setManaged(true);
		errorDatosIncompletosModificacion.setVisible(true);
		errorDatosIncompletosModificacion.setManaged(true);
	}
	/**
	 * Muestra el error de que el evento ya existe
	 */
	public void muestraErrorEventoExistente(){
		System.err.println("El evento ya existe");
		errorEventoExistente.setVisible(true);
		errorEventoExistente.setManaged(true);
	}

	// --------------- Métodos de acción de elementos ---------------
	/**
	 * Cuando un cliente es seleccionado se manda a control a revisar si el cliente ya existe en el repositorio para mostrar y bloquear su número
	 */
	@FXML
	private void accionClienteSeleccionado(){
		String nombre = textNombreCreacion.getValue();
		if(nombre == null || nombre.isBlank()) return;
		control.seleccionaCliente(nombre);
	}
	/**
	 * En caso de que se haya encontrado un cliente con ese nombre, se deshabilita la editabilidad del número telefóncio y se muestra el número asociado al cliente
	 */
	public void clienteUsado(String numero){
		textTelefonoCreacion.setEditable(false);
		textTelefonoCreacion.setText(numero);
	}
	/**
	 * En caso de que no se haya encontrado un cliente con el nombre ingresado, se habilita la editabilidad del número telefónico para que el usuario pueda ingresar uno nuevo
	 */
	public void clienteNuevo(){
		textTelefonoCreacion.setEditable(true);
		textTelefonoCreacion.setText(null);
	}
	
	/**
	 * En caso de presionar el botón Cancelar manda a regresar al calendario
	 */
	@FXML
	private void presionarBotonCancelar(){
		reiniciarValores();
		control.regresar();
	}
	/**
	 * Cuando se presiona el botón para crear, se validan los datos y se cambian de tipos en caso de ser neceario para mandarlos al control a guardarlos
	 */
	@FXML
	private void presionarBotonCrear(){
		// Se valida si los datos mínimos están completos
		if(!validaDatos(textTipoCreacion, textNombreCreacion, textTelefonoCreacion, textFechaCreacion, horaCreacion, minutoCreacion, textDireccionCreacion, null, null, null)) return;
		System.out.println("Se guardarán los datos");

		// Se asignan en variables todos los valores
		TipoEvento tipo = textTipoCreacion.getValue();
		String nombre = textNombreCreacion.getValue();
		String num = textTelefonoCreacion.getText();
		Object datoFecha = textFechaCreacion.getUserData();
		int hora = horaCreacion.getValue();
		int minuto = minutoCreacion.getValue();
		String lugar = textLugarCreacion.getText();
		String direccion = textDireccionCreacion.getText();
		String referencias = textReferenciasCreacion.getText();
		String notas = textNotasCreacion.getText();

		LocalTime horaEvento = LocalTime.of(hora, minuto);

		String rutaImagen = "";
		if(archivoImagenSeleccionado != null) {
			try {
                // 1. Creamos la carpeta donde vivirá la imagen (si no existe)
                Path carpetaDestino = Path.of("imagenes_eventos");
                Files.createDirectories(carpetaDestino);

                // 2. Definimos el nombre final del archivo (le dejamos el nombre original)
                Path archivoDestino = carpetaDestino.resolve(archivoImagenSeleccionado.getName());

                // 3. Copiamos el archivo de la compu del usuario a nuestra carpeta
                Files.copy(archivoImagenSeleccionado.toPath(), archivoDestino, StandardCopyOption.REPLACE_EXISTING);

                // 4. Guardamos la ruta en formato texto para la base de datos
                rutaImagen = archivoDestino.toString();
            } catch (IOException e) {
                System.err.println("Error al guardar la imagen: " + e.getMessage());
            }
		}

		if(datoFecha instanceof LocalDate fecha)
			control.guardaEvento(tipo, nombre, num, fecha, horaEvento, lugar, direccion, referencias, rutaImagen, notas);
	}

	/**
	 * Cuando se presiona el botón para modificar, se valida que los datos mínimos sigan presentes y en formato para proceder a la modificación
	 */
	@FXML
	private void presionarBotonModificar(){
		if(!validaDatos(textTipoModificacion, null, textTelefonoModificacion, textFechaModificacion, horaModificacion, minutoModificacion, textDireccionModificacion, textNombreModificacion, switchConfirmadoModificacion, switchBorradorModificacion)) return;
		System.out.println("Se guardarán los datos modificados");
		// Se asignan en variables todos los valores
		TipoEvento tipo = textTipoModificacion.getValue();
		LocalDate fecha = (LocalDate) textFechaModificacion.getUserData();
		int hora = horaModificacion.getValue();
		int minuto = minutoModificacion.getValue();
		String lugar = textLugarModificacion.getText();
		String direccion = textDireccionModificacion.getText();
		String referencias = textReferenciasModificacion.getText();
		String notas = textNotasModificacion.getText();
		EstadoEvento estado;
		if(switchConfirmadoModificacion.isSelected())
			estado = EstadoEvento.CONFIRMADO;
		else
			estado = EstadoEvento.BORRADOR;

		Evento evento = (Evento) botonModificar.getUserData(); // Recuperamos el evento que estaba guardado en el botón para poder modificarlo

		String rutaImagen = evento.getVisualRecinto(); // Por defecto, se mantiene la ruta de la imagen que ya tenía el evento
		// Si fuera nulo significa que el usuario no seleccionó una imagen nueva, por lo que se mantiene la que ya tenía el evento. En caso de que sí haya seleccionado una nueva, se guarda la nueva ruta
		if(archivoImagenSeleccionado != null) {
			try {
				Path carpetaDestino = Path.of("imagenes_eventos");
				Files.createDirectories(carpetaDestino);

				Path archivoDestino = carpetaDestino.resolve(archivoImagenSeleccionado.getName());

				// StandardCopyOption.REPLACE_EXISTING sobreescribe el archivo si ya existía con el mismo nombre
				Files.copy(archivoImagenSeleccionado.toPath(), archivoDestino, StandardCopyOption.REPLACE_EXISTING);

				// Actualizamos la ruta String para guardar en la BD
				rutaImagen = archivoDestino.toString();

			} catch (IOException e) {
				System.err.println("Error al reemplazar la imagen: " + e.getMessage());
			}
		}
		control.modificaEvento(evento, fecha, tipo, LocalTime.of(hora, minuto), lugar, direccion, referencias, rutaImagen, notas, estado);
	}	

	/**
	 * Cuando se presiona el botón para eliminar, sigue el camino de pantallas de confirmación para eliminarlo
	 */
	@FXML
	private void presionarBotonEliminar(){
		control.solicitaEliminacionEvento();
	}

	/**
	 * Método encargado de tomar la imágen para el image de la visualización del recinto
	 */
	@FXML
	private void accionSeleccionarImagen(){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Seleccionar Imagen");

		// Filtro para que el usuario solo pueda elegir imágenes
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

		// Abre la ventana de selección de Windows/Mac
        archivoImagenSeleccionado = fileChooser.showOpenDialog(stage);

		if (archivoImagenSeleccionado != null) {
            // Si el usuario sí eligió una foto, la mostramos en el ImageView
            Image img = new Image(archivoImagenSeleccionado.toURI().toString());
            imageVisualizacionCreacion.setImage(img);
			imageVisualizacionModificacion.setImage(img);
			subirFotoCreacion.setVisible(false);
			subirFotoModificacion.setVisible(false);
        }
	}


	/**
	 * Cierra la ventana
	 */
	public void cierra(){
		stage.close();
	}
	
}
