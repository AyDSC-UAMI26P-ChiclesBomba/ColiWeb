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


    public void muestraCreacionFecha(LocalDate fecha, List<Cliente> clientes){
        if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> this.muestraCreacionFecha(fecha, clientes));
			return;
		}
		initializeUI();
		
		textTipoCreacion.setItems(FXCollections.observableArrayList(TipoEvento.values()));
		List<String> nombresClientes = new ArrayList<>();
		for(Cliente cliente : clientes){
			nombresClientes.add(cliente.getNombre());
		}
		textNombreCreacion.setItems(FXCollections.observableArrayList(nombresClientes));
		textFechaCreacion.setText(fecha.format(formatoFecha));
		textFechaCreacion.setUserData(fecha);
		SpinnerValueFactory<Integer> horasFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23, 00);
		SpinnerValueFactory<Integer> minutosFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59, 00);
		horasFactory.setWrapAround(true);
		minutosFactory.setWrapAround(true);
		horaCreacion.setValueFactory(horasFactory);
        minutoCreacion.setValueFactory(minutosFactory);


		
		stage.show();
    }


	private boolean validaDatos(){
		if(textTipoCreacion.getValue() == null ||
			textNombreCreacion.getValue() == null || textNombreCreacion.getValue().equals("") ||
			textTelefonoCreacion.getText() == null || textTelefonoCreacion.getText().equals("") ||
			textFechaCreacion.getText() == null || textFechaCreacion.getText().equals("") ||
			horaCreacion.getValue() == null ||
			minutoCreacion.getValue() == null ||
			textDireccionCreacion.getText() == null || textDireccionCreacion.getText().equals("")
		){
			muestraErrorDatos();
			return false;
		}
		return true;
	}

	private void muestraErrorDatos(){
		System.err.println("Los datos no están completos");
		errorDatosIncompletos.setVisible(true);
		errorDatosIncompletos.setManaged(true);
	}
	public void muestraErrorEventoExistente(){
		errorEventoExistente.setVisible(true);
		errorEventoExistente.setManaged(true);
	}

	@FXML
	private void accionClienteSeleccionado(){
		String nombre = textNombreCreacion.getValue();
		control.seleccionaCliente(nombre);
	}
	public void clienteUsado(String numero){
		textTelefonoCreacion.setEditable(false);
		textTelefonoCreacion.setText(numero);
	}
	
	public void clienteNuevo(){
		textTelefonoCreacion.setEditable(true);
	}

	// Presión de botones
	@FXML
	private void presionarBotonCancelar(){
		control.regresar();
	}
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


	public void cierra(){
		stage.close();
	}
	
}
