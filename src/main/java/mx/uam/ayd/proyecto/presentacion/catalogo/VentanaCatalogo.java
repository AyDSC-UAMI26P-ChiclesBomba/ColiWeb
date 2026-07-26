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
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import mx.uam.ayd.proyecto.negocio.modelo.Comestible;
import mx.uam.ayd.proyecto.negocio.modelo.DetalleCotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Globo;
import mx.uam.ayd.proyecto.negocio.modelo.Material;
import mx.uam.ayd.proyecto.negocio.modelo.MaterialDecorativo;
import mx.uam.ayd.proyecto.negocio.modelo.Mobiliario;

@Component
public class VentanaCatalogo {

    private Stage stage;

    @FXML
    private FlowPane flowMateriales;

    @FXML
    private ListView<String> listaMaterialSeleccionado;

    @FXML
    private Button btnContinuarCotizacion;

    private ControlCatalogo controlCatalogo;

    public void setControlCatalogo(ControlCatalogo controlCatalogo) {
        this.controlCatalogo = controlCatalogo;
    }

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

    /* =========================================================================
     * MÉTODOS DEL DIAGRAMA DE CLASES (VistaCatalogo)
     * ========================================================================= */

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

    public void muestraMaterialLista(List<DetalleCotizacion> listaMaterialSeleccionado) {
        muestraListaMaterial(listaMaterialSeleccionado);
    }

    public void muestraListaMaterial(List<DetalleCotizacion> listaSeleccionada) {
        ejecutarEnHiloJavaFX(() -> {
            validaListaVacia(listaSeleccionada);
            if (this.listaMaterialSeleccionado != null) {
                this.listaMaterialSeleccionado.getItems().clear();
                if (listaSeleccionada != null) {
                    for (DetalleCotizacion detalle : listaSeleccionada) {
                        if (detalle.getMaterial() != null) {
                            String renglon = detalle.getMaterial().getNombre() + " x" + detalle.getCantidad() + " ($" + detalle.getCosto() + ")";
                            this.listaMaterialSeleccionado.getItems().add(renglon);
                        }
                    }
                }
            }
        });
    }

    public void muestraMensajeNoAgregarMobiliarioDanoTotal() {
        ejecutarEnHiloJavaFX(() -> {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Mobiliario No Disponible");
            alert.setHeaderText("Daño Total Detectado");
            alert.setContentText("El mobiliario seleccionado presenta daño total y no puede ser agregado a la cotización.");
            alert.showAndWait();
        });
    }

    public void muestraCatalogoGlobos(List<Globo> todoGlobo) {
        ejecutarEnHiloJavaFX(() -> cargarTarjetas(todoGlobo));
    }

    public void muestraCatalogoGloboFiltro(List<Globo> todoGloboFiltro) {
        ejecutarEnHiloJavaFX(() -> cargarTarjetas(todoGloboFiltro));
    }

    public void muestraCatalogoDecoraciones(List<MaterialDecorativo> todoDecoracion) {
        ejecutarEnHiloJavaFX(() -> cargarTarjetas(todoDecoracion));
    }

    public void muestraCatalogoComestible(List<Comestible> todoComestible) {
        ejecutarEnHiloJavaFX(() -> cargarTarjetas(todoComestible));
    }

    public void muestraCatalogoMobiliario(List<Mobiliario> todoMobiliario) {
        ejecutarEnHiloJavaFX(() -> cargarTarjetas(todoMobiliario));
    }
    
    public void cierra() {
        ejecutarEnHiloJavaFX(() -> {
            if (stage != null) {
                stage.close();
            }
        });
    }

    /* =========================================================================
     * MÉTODOS PRIVADOS DE VALIDACIÓN Y CARGA
     * ========================================================================= */

    private void validaListaVacia(List<DetalleCotizacion> listaSeleccionada) {
        if (listaSeleccionada == null || listaSeleccionada.isEmpty()) {
            deshabiliataContinuarACotizacion();
        } else if (btnContinuarCotizacion != null) {
            btnContinuarCotizacion.setDisable(false);
        }
    }

    private void deshabiliataContinuarACotizacion() {
        if (btnContinuarCotizacion != null) {
            btnContinuarCotizacion.setDisable(true);
        }
    }

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

    private void ejecutarEnHiloJavaFX(Runnable accion) {
        if (Platform.isFxApplicationThread()) {
            accion.run();
        } else {
            Platform.runLater(accion);
        }
    }

    @FXML
    public void initialize() {

    System.out.println("Ventana cargada");

    }   
    @FXML 
    public void continuarCotizacion() { 
        if (controlCatalogo != null) {
            controlCatalogo.iniciarCotizacion(); 
        }
    }

    /* =========================================================================
     * MANEJO DE EVENTOS FXML (Botones OnAction)
     * ========================================================================= */

    @FXML private void muestraCatalogo() { controlCatalogo.recuperarTodoMaterial(); }
    @FXML private void muestraCatalogoGlobos() { controlCatalogo.recuperarTodoGlobos(); }
    @FXML private void muestraCatalogoDecoraciones() { controlCatalogo.recuperarTodoMaterialDecorativo(); }
    @FXML private void muestraCatalogoMobiliario() { controlCatalogo.recuperarTodoMobiliario(); }
    @FXML private void muestraCatalogoComestible() { controlCatalogo.recuperarTodoComestibles(); }
}