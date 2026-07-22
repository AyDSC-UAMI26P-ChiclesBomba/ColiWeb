package mx.uam.ayd.proyecto.presentacion.Contrato;

import java.io.IOException;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;

@Component
public class VentanaContrato {

    private Stage stage;
    private ControlContrato control;

    @FXML
    private TextArea textAreaClausulas;

    private Evento eventoActual;

    private boolean initialized = false;

    public VentanaContrato() {

    }

    private void initializeUI() {

        if (initialized) {
            return;
        }

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::initializeUI);
            return;
        }

        try {

            stage = new Stage();
            stage.setTitle("Editar contrato");

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/ventana-contrato.fxml"));

            loader.setController(this);

            Scene scene = new Scene(loader.load(), 500, 400);

            stage.setScene(scene);

            initialized = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setControlContrato(ControlContrato control) {
        this.control = control;
    }

    public void muestraContrato(Evento evento, String clausulas) {

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> muestraContrato(evento, clausulas));
            return;
        }

        initializeUI();

        this.eventoActual = evento;

        textAreaClausulas.setText(clausulas);

        stage.show();

    }

    public void muestraDialogoConMensaje(String mensaje) {

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> muestraDialogoConMensaje(mensaje));
            return;
        }

        Alert alert = new Alert(AlertType.INFORMATION);

        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        alert.showAndWait();

    }

    public void setVisible(boolean visible) {

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> setVisible(visible));
            return;
        }

        if (!initialized) {

            if (visible) {
                initializeUI();
            } else {
                return;
            }

        }

        if (visible) {
            stage.show();
        } else {
            stage.hide();
        }

    }

    @FXML
    private void handleGuardar() {

        control.guardarClausulas(
                eventoActual,
                textAreaClausulas.getText());

    }

    @FXML
    private void handleFirmar() {

        control.firmarContrato(eventoActual);

    }

    @FXML
    private void handleCancelar() {

        control.termina();

    }

}