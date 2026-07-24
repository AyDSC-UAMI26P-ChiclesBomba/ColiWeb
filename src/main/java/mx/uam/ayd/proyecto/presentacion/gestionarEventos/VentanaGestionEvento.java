package mx.uam.ayd.proyecto.presentacion.gestionarEventos;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.modelo.Cliente;
import mx.uam.ayd.proyecto.negocio.modelo.Evento.TipoEvento;

@Component
public class VentanaGestionEvento {
    private Stage stage;
    private ControlGestionEvento control;
    private boolean initialized = false;

    public VentanaGestionEvento(){}

	/* ---------------- Variables FXML ---------------- */
	// Ventanas
	@FXML
	private VBox creacionBox;
	@FXML
	private VBox modificacionBox;

	// Recuadros Creación
	@FXML
	private ComboBox<TipoEvento> textTipoCreacion;
	@FXML
	private ComboBox<String> textNombreCreacion;
	@FXML
	private TextField textTelefonoCreacion;
	@FXML
	private TextField textFechaCreacion;



	// Botones
	private Button botonCrear;
	private Button botonCancelar;



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


    public void muestraCreacionFecha(LocalDate fecha, List<String> nombresClientes, List<String> numerosClientes){
        if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> this.muestraCreacionFecha(fecha, nombresClientes, numerosClientes));
			return;
		}
		initializeUI();
		
		textTipoCreacion.setItems(FXCollections.observableArrayList(TipoEvento.values()));
		textNombreCreacion.setItems(FXCollections.observableArrayList(nombresClientes));














		
		stage.show();
    }



	@FXML
	private void accionClienteSeleccionado(){
		String nombre = textNombreCreacion.getValue();
		control.buscarCliente(nombre);
	}
	public void regresoNumero(String numero){
		textTelefonoCreacion.setEditable(false);
		textTelefonoCreacion.setText(numero);
	}

	// Presión de botones
	@FXML
	private void presionarBotonCancelar(){

	}
	@FXML
	private void presionarBotonCrear(){

	}
}
