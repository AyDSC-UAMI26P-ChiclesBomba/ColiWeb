package mx.uam.ayd.proyecto.presentacion.catalogo;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import mx.uam.ayd.proyecto.negocio.modelo.Comestible;
import mx.uam.ayd.proyecto.negocio.modelo.DetalleCotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Globo;
import mx.uam.ayd.proyecto.negocio.modelo.Material;
import mx.uam.ayd.proyecto.negocio.modelo.MaterialDecorativo;
import mx.uam.ayd.proyecto.negocio.modelo.Mobiliario;

/**
 * Ventana que muestra el catálogo de materiales disponibles para una cotización.
 */

@Component
public class VentanaCatalogo {

    private Stage stage;

    @FXML
    private FlowPane flowMateriales;
    @FXML
    private FlowPane flowListaMaterial;

    @FXML
    private Button btnContinuarCotizacion;

    private ControlCatalogo controlCatalogo;

    public void setControlCatalogo(ControlCatalogo controlCatalogo) {
        this.controlCatalogo = controlCatalogo;
    }

    /**
    * Inicializa la ventana del catálogo únicamente si aún no ha sido creada
    */
    private void inicializarVentanaSiEsNecesario() {
        if (stage == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-catalogo.fxml"));
                loader.setController(this);
                // Carga la vista (Recuerda: el FXML debe tener fx:controller asignado)
                Parent root = loader.load();

                stage = new Stage();
                stage.setTitle("Catálogo de Materiales");
                
                // Dimensiones explícitas para evitar la pantalla negra
                Scene scene = new Scene(root, 1100, 750);
                stage.setScene(scene);
                
                stage.setMaximized(true);
                
                // Bloquea ventanas anteriores mientras el catálogo esté abierto
                stage.initModality(Modality.APPLICATION_MODAL);

            } catch (Exception e) {
                System.err.println("============== ERROR CRÍTICO AL CARGAR FXML ==============");
                System.err.println("Causa principal: " + e.getMessage());
                e.printStackTrace();
                System.err.println("==========================================================");
            }
        }
    }
    /**
    * Muestra el catálogo con la lista de materiales proporcionada.
    *
    * @param todoMaterial lista de materiales que se mostrarán en el catálogo.
    */

    public void muestraCatalogo(List<Material> todoMaterial) {
        ejecutarEnHiloJavaFX(() -> {
            inicializarVentanaSiEsNecesario();
            cargarTarjetas(todoMaterial);
            if (stage != null) {
                stage.show();
                stage.toFront();
            }
        });
    }
    /**
    * Actualiza la lista de materiales seleccionados para la cotización.
    *          
    * @param listaMaterialSeleccionado lista de materiales agregados a la cotización.
    */
    public void muestraMaterialLista(List<DetalleCotizacion> listaMaterialSeleccionado) {
        muestraListaMaterial(listaMaterialSeleccionado);
    } 
    /**
    * Muestra en la interfaz la lista de materiales seleccionados.
    * @param listaMaterialSeleccionado lista de detalles de la cotización.
    */

    public void muestraListaMaterial(List<DetalleCotizacion> listaMaterialSeleccionado) {
        // Es una forma abreviada de crear un objeto que implementa una interfaz funcional
    ejecutarEnHiloJavaFX(() -> {


        if (flowListaMaterial == null) {
            return;
        }

        flowListaMaterial.getChildren().clear();

        if (listaMaterialSeleccionado == null) {
            return;
        }

        for (DetalleCotizacion detalle : listaMaterialSeleccionado) {

            try {

                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/fxml/detalleCotizacion.fxml"));

                Parent tarjeta = loader.load();

                DetalleCotizacionController controller =
                        loader.getController();

                controller.setDetalle(detalle, controlCatalogo);

                flowListaMaterial.getChildren().add(tarjeta);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    });
    actualizarBotonContinuar();

}
/**
 * Actualiza el estado del botón para continuar con la cotización.
 */
private void actualizarBotonContinuar() {

    if (btnContinuarCotizacion == null || flowListaMaterial == null) {
        return;
    }

    btnContinuarCotizacion.setDisable(flowListaMaterial.getChildren().isEmpty());

}

/**
 * Muestra un mensaje de advertencia cuando se intenta agregar un
 * mobiliario con daño total a la cotización.
 */
    public void muestraMensajeNoAgregarMobiliarioDanoTotal() {
        ejecutarEnHiloJavaFX(() -> {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Mobiliario No Disponible");
            alert.setHeaderText("Daño Total Detectado");
            alert.setContentText("El mobiliario seleccionado presenta daño total y no puede ser agregado a la cotización.");
            alert.showAndWait();
        });
    }
/**
 * Muestra únicamente los globos disponibles en el catálogo.
 *
 * @param todoGlobo lista de globos.
 */
    public void muestraCatalogoGlobos(List<Globo> todoGlobo) {
        ejecutarEnHiloJavaFX(() -> cargarTarjetas(todoGlobo));
    }
/**
 * Muestra únicamente los materiales decorativos disponibles.
 *
 * @param todoDecoracion lista de materiales decorativos.
 */
    public void muestraCatalogoDecoraciones(List<MaterialDecorativo> todoDecoracion) {
        ejecutarEnHiloJavaFX(() -> cargarTarjetas(todoDecoracion));
    }
/**
 * Muestra únicamente los materiales comestibles disponibles.
 *
 * @param todoComestible lista de materiales comestibles.
 */
    public void muestraCatalogoComestible(List<Comestible> todoComestible) {
        ejecutarEnHiloJavaFX(() -> cargarTarjetas(todoComestible));
    }
/**
 * Muestra únicamente el mobiliario disponible.
 *
 * @param todoMobiliario lista de mobiliario.
 */
    public void muestraCatalogoMobiliario(List<Mobiliario> todoMobiliario) {
        ejecutarEnHiloJavaFX(() -> cargarTarjetas(todoMobiliario));
    }
    /**
    * Cierra la ventana del catálogo si ésta se encuentra abierta.
    */
    public void cierra() {
        ejecutarEnHiloJavaFX(() -> {
            if (stage != null) {
                stage.close();
            }
        });
    }
    /**
    * Deshabilita el botón que permite continuar hacia la cotización.
    */
    private void deshabiliataContinuarACotizacion() {
        if (btnContinuarCotizacion != null) {
            btnContinuarCotizacion.setDisable(true);
        }
    }
/**
 * Carga las tarjetas gráficas correspondientes a los materiales recibidos.
 * @param materiales lista de materiales que serán mostrados.
 */
    private void cargarTarjetas(List<? extends Material> materiales) {
        if (flowMateriales == null || materiales == null) return;

        flowMateriales.getChildren().clear();

        for (Material material : materiales) {
            try {
                FXMLLoader loader;
                Parent tarjeta;

                // Si es Mobiliario carga su FXML específico, sino carga el general
                if (material instanceof Mobiliario) {
                    loader = new FXMLLoader(getClass().getResource("/fxml/materialMobiliario.fxml"));
                    tarjeta = loader.load();
                    MaterialMobiliarioController controller = loader.getController();
                    if (controller != null) {
                        controller.setMaterial((Mobiliario) material, controlCatalogo);
                    }
                } else {
                    loader = new FXMLLoader(getClass().getResource("/fxml/material.fxml"));
                    tarjeta = loader.load();
                    MaterialController controller = loader.getController();
                    if (controller != null) {
                        controller.setMaterial(material, controlCatalogo);
                    }
                }

                flowMateriales.getChildren().add(tarjeta);

            } catch (IOException e) {
                System.err.println("Error al cargar la tarjeta del material: " + material.getNombre());
                e.printStackTrace();
            }
        }
    }
/**
 * Carga las tarjetas gráficas correspondientes a los materiales recibidos.
 * @param materiales lista de materiales que serán mostrados.
 */
    private void ejecutarEnHiloJavaFX(Runnable accion) {
        if (Platform.isFxApplicationThread()) {
            accion.run();
        } else {
            Platform.runLater(accion);
        }
    }
/**
 * Inicializa los componentes de la ventana después de cargar el archivo FXML.
 */
    @FXML
    public void initialize() {

        btnContinuarCotizacion.setDisable(true);
    }   
    /**
    * Atiende el evento del botón "Continuar Cotización".
    */
    @FXML 
    public void continuarCotizacion() { 
        System.out.println("Se presionó Continuar Cotización");
        if (controlCatalogo != null) {
            System.out.println("controlCatalogo NO es null");
            controlCatalogo.iniciarCotizacion(); 
        }else{
            System.out.println("controlCatalogo ES null");
        }
    }
    /**
    * Muetran el material correspondiente
    */
    @FXML private void muestraCatalogo() { controlCatalogo.recuperarTodoMaterial(); }
    @FXML private void muestraCatalogoGlobos() { controlCatalogo.recuperarTodoGlobos(); }
    @FXML private void muestraCatalogoDecoraciones() { controlCatalogo.recuperarTodoMaterialDecorativo(); }
    @FXML private void muestraCatalogoMobiliario() { controlCatalogo.recuperarTodoMobiliario(); }
    @FXML private void muestraCatalogoComestible() { controlCatalogo.recuperarTodoComestibles(); }
}