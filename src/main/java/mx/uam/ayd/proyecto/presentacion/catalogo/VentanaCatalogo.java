package mx.uam.ayd.proyecto.presentacion.catalogo;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.modelo.DetalleCotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Material;
import mx.uam.ayd.proyecto.negocio.modelo.Mobiliario;

@Component
public class VentanaCatalogo {

    private Stage stage;

    @FXML
    private FlowPane flowMateriales;

    @FXML
    private ListView<String> listaMaterialSeleccionado;

    private ControlCatalogo controlCatalogo;

    private boolean initialized = false;

    public void setControlCatalogo(ControlCatalogo controlCatalogo) {
        this.controlCatalogo = controlCatalogo;
    }

    private void initializeUI() {
        if (initialized) return;

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::initializeUI);
            return;
        }

        try {
            stage = new Stage();
            stage.setTitle("Catálogo");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-catalogo.fxml"));
            // Importante: No forzar setController(this) si ya está declarado en FXML o si maneja Spring
            loader.setController(this);

            Scene scene = new Scene(loader.load(), 1200, 800);
            stage.setScene(scene);
            initialized = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void muestra(List<Material> materiales) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> muestra(materiales));
            return;
        }

        if (!initialized) {
            initializeUI();
        }

        actualizarCatalogo(materiales);
        stage.show();
    }

    public void actualizarCatalogo(List<? extends Material> materiales) {
    cargarMateriales(materiales);
    }

    private void cargarMateriales(List<? extends Material> materiales) {
    flowMateriales.getChildren().clear();

    for (Material material : materiales) {
        try {
            FXMLLoader loader;
            Parent tarjeta;

            if (material instanceof Mobiliario) {
                loader = new FXMLLoader(
                        getClass().getResource("/fxml/materialMobiliario.fxml"));

                tarjeta = loader.load();

                MaterialMobiliarioController controller =
                        loader.getController();

                controller.setMaterial((Mobiliario) material, controlCatalogo);

            } else {
                loader = new FXMLLoader(
                        getClass().getResource("/fxml/material.fxml"));

                tarjeta = loader.load();

                MaterialController controller =
                        loader.getController();

                controller.setMaterial(material);
            }

            flowMateriales.getChildren().add(tarjeta);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

    public void limpiarListaSeleccionados() {
        if (listaMaterialSeleccionado != null) {
            listaMaterialSeleccionado.getItems().clear();
        }
    }

    public void agregarItemSeleccionado(String item) {
        if (listaMaterialSeleccionado != null) {
            listaMaterialSeleccionado.getItems().add(item);
        }
    }

    public void actualizarListaResumen(List<DetalleCotizacion> detalles) {
    if (!Platform.isFxApplicationThread()) {
        Platform.runLater(() -> actualizarListaResumen(detalles));
        return;
    }

    listaMaterialSeleccionado.getItems().clear();
    for (DetalleCotizacion detalle : detalles) {
        if (detalle.getMaterial() != null) {
            String itemText = detalle.getMaterial().getNombre() + " x" + detalle.getCantidad() +  " ($" + detalle.getCosto() + ")";
            listaMaterialSeleccionado.getItems().add(itemText);
        }
    }
}

    @FXML private void mostrarTodos() { controlCatalogo.mostrarTodos(); }
    @FXML private void mostrarGlobos() { controlCatalogo.mostrarGlobos(); }
    @FXML private void mostrarDecoraciones() { controlCatalogo.mostrarDecoraciones(); }
    @FXML private void mostrarMobiliario() { controlCatalogo.mostrarMobiliario(); }
    @FXML private void mostrarComestibles() { controlCatalogo.mostrarComestibles(); }
}