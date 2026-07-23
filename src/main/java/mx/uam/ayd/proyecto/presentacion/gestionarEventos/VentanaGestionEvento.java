package mx.uam.ayd.proyecto.presentacion.gestionarEventos;

import java.io.IOException;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

@Component
public class VentanaGestionEvento {
    private Stage stage;
    private ControlGestionEvento control;
    private boolean initialized = false;

    public VentanaGestionEvento(){}


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


    public void inicia(){
        if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> this.inicia());
			return;
		}
		initializeUI();
















		
		stage.show();
    }
}
