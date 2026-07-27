package mx.uam.ayd.proyecto.presentacion.cotizacionTotal;

import javafx.scene.Node;

import java.net.URL;
import java.util.List;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import mx.uam.ayd.proyecto.negocio.modelo.Cliente;
import mx.uam.ayd.proyecto.negocio.modelo.Cotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Cotizacion.Tamano;
import mx.uam.ayd.proyecto.negocio.modelo.DetalleCotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;
import mx.uam.ayd.proyecto.negocio.modelo.Material;

@Component
public class VentanaCotizacionTotal {

    private ControlCotizacionTotal controlCotizacionTotal;
    private Stage stage;

    // Estado local para los parámetros requeridos por el Control
    private Cotizacion cotizacionActual;
    private Evento eventoActual;
    private Cliente clienteActual;
    private List<DetalleCotizacion> listaMaterialesActual;

    // ==========================================
    // Componentes FXML - Vista 1: Resumen Cotización
    // ==========================================
    @FXML private Label IdlFecha;
    @FXML private Label IdlDireccion;

    @FXML private TextField txtTransporte;
    @FXML private TextField txtMaterialPersonalizado;
    @FXML private TextField txtMaterialCliente;

    @FXML private ToggleGroup tipoEvento;
    @FXML private RadioButton IdlTamano;
    @FXML private TextArea txtAreaDetalles;

    @FXML private Button btnGuardarBorrador;
    @FXML private Button btnGenerarCotizacion;
    @FXML private Button btnConsultarPrecios;
    @FXML private Button btnContinuarCotizacion;

    // ==========================================
    // Componentes FXML - Vista 2: Cotización Total
    // ==========================================
    @FXML private Label IdlTotal;
    @FXML private Label IdlTotalMaterial;
    @FXML private Label IdlExtra;
    @FXML private Label IdlConsumibles;
    @FXML private Label IdlManoObra;
    @FXML private Label IdlGanancia;
    @FXML private Label IdlDetalles;

    @FXML private Button btnVolverCatalogo;
    @FXML private Button btnGenerarContrato;
    @FXML private FlowPane flowMateriales;

    private boolean materialesCargados = false;

    public void setControlCotizacionTotal(ControlCotizacionTotal control) {
        this.controlCotizacionTotal = control;
    }

    private void inicializarVentana() {
        if (stage == null) {
            stage = new Stage();
            stage.setTitle("Cotización");
        }
    }

    private void cambiarVista(String vista) {
        try {
            inicializarVentana();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(vista));
            loader.setController(this);

            Parent root = loader.load();
            stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==========================================
    // Métodos invocados desde ControlCotizacionTotal
    // ==========================================

    public void muestraResumenCotizacion(List<DetalleCotizacion> listaMaterialSeleccionado, Cotizacion cotizacion) {
    this.listaMaterialesActual = listaMaterialSeleccionado;
    this.cotizacionActual = cotizacion;
    
    // 1. Cambiar la vista primero
    cambiarVista("/fxml/ventana-resumen-cotizacion.fxml");

    // 2. Mostrar la ventana para asegurar que la escena y la jerarquía de nodos estén activas
    if (stage != null && !stage.isShowing()) {
        stage.show();
    }

    // 3. Cargar la lista
    if (listaMaterialSeleccionado != null) {
        cargarMateriales(listaMaterialSeleccionado);
    }
}

    public void cargarMateriales(List<DetalleCotizacion> materiales) {
    // Si la inyección falló, intentamos buscar el FlowPane directamente en la escena actual
    if (flowMateriales == null && stage != null && stage.getScene() != null) {
        flowMateriales = (FlowPane) stage.getScene().lookup("#flowMateriales");
    }

    // Si sigue siendo nulo tras la búsqueda, cancelamos para evitar NullPointerException
    if (flowMateriales == null) {
        System.err.println("Advertencia: No se encontró el componente 'flowMateriales' en la vista actual.");
        return;
    }

    flowMateriales.getChildren().clear();
        System.out.println("Cantidad de detalles: " + materiales.size());

    for (DetalleCotizacion detalle : materiales) {
        try {
            FXMLLoader loader;
            Material material = detalle.getMaterial();

            System.out.println("Precios completos: " + detalle.getPreciosCompletos());

            System.out.println("Con precio: " +
            getClass().getResource("/fxml/materialListaResumenConPrecio.fxml"));

            System.out.println("Sin precio: " +
            getClass().getResource("/fxml/materialListaResumenSinPrecio.fxml"));

            if (detalle.getPreciosCompletos()) {
                loader = new FXMLLoader(getClass().getResource("/fxm/materialListaResumenSinPrecio.fxml"));
                URL url = getClass().getResource("/fxml/materialListaResumenConPrecio.fxml");
                System.out.println(url);
                loader = new FXMLLoader(url);
                Node nodo = loader.load();

                MaterialListaResumenConPrecioController controller = loader.getController();
                if (controller != null) {
                    controller.setMaterialListaResumen(material, controlCotizacionTotal, detalle);
                }
                flowMateriales.getChildren().add(nodo);

            } else {
                loader = new FXMLLoader(getClass().getResource("/fxml/materialListaResumenSinPrecio.fxml"));
                URL url = getClass().getResource("/fxml/materialListaResumenConPrecio.fxml");
                System.out.println(url);
                loader = new FXMLLoader(url);
                Node nodo = loader.load();

                MaterialListaResumenSinPrecioController controller = loader.getController();
                if (controller != null) {
                    controller.setMaterialListaResumen(material, controlCotizacionTotal, detalle);
                }
                flowMateriales.getChildren().add(nodo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    materialesCargados = true;
    actualizarEstadoBotones();
}

    private void actualizarEstadoBotones() {
        if (materialesCargados) {
            if (btnGuardarBorrador != null) btnGuardarBorrador.setDisable(true);
            if (btnConsultarPrecios != null) btnConsultarPrecios.setDisable(true);
            if (btnContinuarCotizacion != null) btnContinuarCotizacion.setDisable(false);
        }
    }

    public void muestraDetallesCotizacionLlenos(Cliente cliente, Evento evento) {
        this.eventoActual = evento;
        if (evento != null) {
            if (IdlFecha != null && evento.getFecha() != null) IdlFecha.setText(evento.getFecha().toString());
            if (IdlDireccion != null) IdlDireccion.setText(evento.getDireccion());
        }
    }

    public void muestraCotizacionTotal(Cotizacion cotizacion) {
    this.cotizacionActual = cotizacion;

    // 1. Inflar la pantalla primero para que los @FXML se vinculen
    cambiarVista("/fxml/ventana-cotizacion-total.fxml");

    // 2. Desplegar valores en las etiquetas vinculadas
    if (cotizacion != null) {
        if (IdlTotal != null) IdlTotal.setText(String.format("$ %.2f", cotizacion.getTotal()));
        if (IdlTotalMaterial != null) IdlTotalMaterial.setText(String.format("$ %.2f", cotizacion.getTotalMaterial()));
        if (IdlExtra != null) IdlExtra.setText(String.format("$ %.2f", cotizacion.getExtra())); // <-- Se agrega
        if (IdlConsumibles != null) IdlConsumibles.setText(String.format("$ %.2f", cotizacion.getConsumibles()));
        if (IdlManoObra != null) IdlManoObra.setText(String.format("$ %.2f", cotizacion.getManoObra())); // <-- Corregido
        if (IdlGanancia != null) IdlGanancia.setText(String.format("$ %.2f", cotizacion.getGanancia()));
    }
}

    public void muestraDetallesCotizacion(Cliente cliente, Evento evento) {
        this.clienteActual = cliente; // Guardar cliente en la variable de estado
        this.eventoActual = evento;
        System.out.println("--- DIAGNÓSTICO ---");
        poblarDatosClienteYEvento();
    }

    private void poblarDatosClienteYEvento() {
        if (eventoActual != null) {
            if (IdlFecha != null && eventoActual.getFecha() != null) {
                IdlFecha.setText(eventoActual.getFecha().toString());
            }
            if (IdlDireccion != null) IdlDireccion.setText(eventoActual.getDireccion());
        }
    }

    public void muestraListaMaterialActualizada(DetalleCotizacion material) {
        // Callback para refrescar la lista
    }

    public void copiaTotalCotizacion(float total) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(String.format("$ %.2f", total));
        clipboard.setContent(content);
    }

    public void deshabilitaVolverCatalogo(boolean estado) {
        if (btnVolverCatalogo != null) {
            btnVolverCatalogo.setDisable(estado);
        }
    }

    public void deshabilitaContinuarCotizacion() {
        if (btnGenerarCotizacion != null) {
            btnGenerarCotizacion.setDisable(true);
        }
    }

    public void deshabilitaGuardarBorradorYConsultarPrecio() {
        if (btnGuardarBorrador != null) btnGuardarBorrador.setDisable(true);
        if (btnConsultarPrecios != null) btnConsultarPrecios.setDisable(true);
        if (btnContinuarCotizacion != null) btnContinuarCotizacion.setDisable(false);
    }

    public void muestraMensajeBorradoExito(boolean exito) {
        if (exito) {
            cierra();
            controlCotizacionTotal.guardarCotizacion();
        }
    }

    public void cierra() {
        if (stage != null) {
            stage.close();
        }
    }

    // ==========================================
    // Eventos FXML (onAction)
    // ==========================================

    @FXML
    void muestraDetallesCotizacionLlenos(ActionEvent event) {
        
    }

    @FXML
    void generarCotizacion(ActionEvent event) {
        Float transporte = parseFloat(txtTransporte != null ? txtTransporte.getText() : null);
        Float materialPersonalizado = parseFloat(txtMaterialPersonalizado != null ? txtMaterialPersonalizado.getText() : null);
        Float materialCliente = parseFloat(txtMaterialCliente != null ? txtMaterialCliente.getText() : null);

        Tamano tamano = null;
        if (tipoEvento != null && tipoEvento.getSelectedToggle() != null) {
            RadioButton rb = (RadioButton) tipoEvento.getSelectedToggle();
            try {
                tamano = Tamano.valueOf(rb.getText().toUpperCase());
            } catch (IllegalArgumentException e) {
                // Si el texto del RadioButton difiere del Enum
            }
        }

        String detalles = (txtAreaDetalles != null) ? txtAreaDetalles.getText() : "";

        // Solo se notifica al controlador. Es el controlador el que llama a muestraCotizacionTotal(...)
        controlCotizacionTotal.actualizaCostosExtra(
            transporte, 
            materialPersonalizado, 
            materialCliente, 
            tamano, 
            detalles, 
            listaMaterialesActual, 
            cotizacionActual, 
            eventoActual
        );
    }

    @FXML
    void continuarCotizacion(ActionEvent event) {
        cambiarVista("/fxml/ventana-detalle-cotizacion.fxml");
        poblarDatosClienteYEvento();
    }

    @FXML
    void copiaTotalCotizacion(ActionEvent event) {
        controlCotizacionTotal.copiarTotal(cotizacionActual);
    }

    @FXML
    void deshabilitaVolverCatalogo(ActionEvent event) {
        controlCotizacionTotal.deshabilitarVolverCatalogo(cotizacionActual, true);
    }

    @FXML
    void muestraCatalogo(ActionEvent event) {
        controlCotizacionTotal.volverCatalogo(cotizacionActual);
    }

    @FXML
    void muestraMensajeBorradoExito(ActionEvent event) {
        controlCotizacionTotal.borrarCotizacion(cotizacionActual);
    }


    // ===== INICIO CAMBIO HU-6 =====

    @FXML
    void botonGenerarContrato(ActionEvent event) {
        controlCotizacionTotal.iniciaContrato(
            listaMaterialesActual,
            eventoActual,
            cotizacionActual
        );
    }
    @FXML
    void generarContrato(ActionEvent event){
        controlCotizacionTotal.iniciaContrato(listaMaterialesActual, eventoActual, cotizacionActual);
    }

    // ===== FIN CAMBIO HU-6 =====

    // ==========================================
    // Métodos Auxiliares
    // ==========================================

    private Float parseFloat(String texto) {
        if (texto == null || texto.trim().isEmpty()) return 0.0f;
        try {
            return Float.parseFloat(texto.trim());
        } catch (NumberFormatException e) {
            return 0.0f;
        }
    }
}