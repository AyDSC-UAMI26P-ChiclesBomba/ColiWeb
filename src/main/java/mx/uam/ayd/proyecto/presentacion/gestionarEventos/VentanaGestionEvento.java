package mx.uam.ayd.proyecto.presentacion.gestionarEventos;

import java.io.IOException;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.modelo.Cliente;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;
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

	// Recuadros Modificación
	@FXML
	private ComboBox<TipoEvento> textTipoModificacion;
	@FXML
	private ComboBox<String> textNombreModificacion;
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
	@FXML
	private TextArea textNotasModificacion;

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
	}

	// --------------- MÉTODOS DE MUESTRAS CREACIÓN Y MODIFICACIÓN ---------------
	/**
	 * Método encargado de iniciar la pantalla de Creación, reseteando e iniciando los datos que deben ir en las opciones elegibles y de escritura para el usuario
	 * @param fecha Es la fecha sobre la que se trabaja la creación del evento
	 * @param clientes Es la lista de clientes que ya existen en el repositorio
	 */
    public void muestraCreacionFecha(LocalDate fecha, List<Cliente> clientes){
        inicializacion();

		// Nos aseguramos de que el contenedor de creación sea el visible y el manejado en lugar del de modificación
		modificacionBox.setVisible(false);
		modificacionBox.setManaged(false);
		creacionBox.setVisible(true);
		creacionBox.setManaged(true);

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
		
		stage.show();
    }

	public void muestraGestionEvento(Evento evento) {
        inicializacion();

		// Nos aseguramos de que el contenedor de modificación sea el visible y el manejado en lugar del de creación
		creacionBox.setVisible(false);
		creacionBox.setManaged(false);
		modificacionBox.setVisible(true);
		modificacionBox.setManaged(true);

		// Se reinician los valores que tienen los textos
		reiniciarValores();

		stage.show();
	}



	// --------------- Métodos de apoyo en las muestras ---------------
	/**
	 * Método encargado de reiniciar los valores de los textos y ocultar los errores
	 */
	private void reiniciarValores(){
		// Los errores son limpiados y ocultados
		errorDatosIncompletos.setVisible(false);
		errorDatosIncompletos.setManaged(false);
		errorEventoExistente.setVisible(false);
		errorEventoExistente.setManaged(false);
		
		// Se limpian todos los textos
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
	}
	/**
	 * Método encargado de validar los datos que son obligatorios para un evento. Dependiendo de si se cumplen todos, se manda true, de lo contrario, false y se manda el error correspondiente
	 * @return es el estado conseguido de la validación de todos los campos obligatorios
	 */
	private boolean validaDatos(){
		// Validación Tipo de Evento
		if(textTipoCreacion.getValue() == null){
			muestraErrorDatos();
			return false;
		}
		// Validación Nombre del Cliente
		if(textNombreCreacion.getValue() == null || textNombreCreacion.getValue().isBlank()){
			muestraErrorDatos();
			return false;
		}
		// Validación Número telefónico del cliente
		if(textTelefonoCreacion.getText() == null || textTelefonoCreacion.getText().isBlank() || !(textTelefonoCreacion.getText().matches("^[0-9]{10}$"))){
			muestraErrorDatos();
			return false;
		}
		// Validación Fecha del evento
		if(textFechaCreacion.getText() == null || textFechaCreacion.getText().isBlank()){
			muestraErrorDatos();
			return false;
		}
		// Validación Hora
		if(horaCreacion.getValue() == null || !(horaCreacion.getValue()<=23 && horaCreacion.getValue()>=00)){
			muestraErrorDatos();
			return false;
		}
		// Validación Minutos
		if(minutoCreacion.getValue() == null || !(minutoCreacion.getValue()<=59 && minutoCreacion.getValue()>=00)){
			muestraErrorDatos();
			return false;
		}
		// Validación Dirección del evento
		if(textDireccionCreacion.getText() == null || textDireccionCreacion.getText().isBlank()){
			muestraErrorDatos();
			return false;
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
		if(!validaDatos()) return;
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

		if(datoFecha instanceof LocalDate fecha)
			control.guardaEvento(tipo, nombre, num, fecha, horaEvento, lugar, direccion, referencias, notas);
	}


	/**
	 * Cierra la ventana
	 */
	public void cierra(){
		stage.close();
	}
	
}
