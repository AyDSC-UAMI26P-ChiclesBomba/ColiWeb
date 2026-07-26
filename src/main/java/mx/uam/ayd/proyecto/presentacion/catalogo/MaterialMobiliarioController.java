package mx.uam.ayd.proyecto.presentacion.catalogo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import mx.uam.ayd.proyecto.negocio.modelo.Mobiliario;
import mx.uam.ayd.proyecto.negocio.modelo.Material;


public class MaterialMobiliarioController {

    @FXML
    private ImageView imgMaterial;

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblPrecio;

    @FXML
    private Label lblEstado;

    @FXML
    private Label lblTipoDano;

    @FXML
    private Label lblCantidad;

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnQuitar;

    private ControlCatalogo controlCatalogo;

    private Mobiliario mobiliario;

    private int cantidad = 0;

    /**
     * Carga la información del mobiliario.
     */
    public void setMaterial(Mobiliario mobiliario, ControlCatalogo controlCatalogo) { // <--- RECIBIR ControlCatalogo
        this.mobiliario = mobiliario;
        this.controlCatalogo = controlCatalogo; // <--- GUARDAR LA REFERENCIA

        lblNombre.setText(mobiliario.getNombre());

        if (mobiliario.getPrecio() != null) {
            lblPrecio.setText("$" + mobiliario.getPrecio());
        } else {
            lblPrecio.setText("Sin precio");
        }

        lblEstado.setText(
                mobiliario.getEstadoMobiliario()
                        ? "Disponible"
                        : "No disponible");

        if (mobiliario.getTipoDano() != null) {
            lblTipoDano.setText(mobiliario.getTipoDano().name());
        }

        lblCantidad.setText("0");
    }

    @FXML
    private void agregarMaterial() {
        cantidad++;
        lblCantidad.setText(String.valueOf(cantidad));
        if (controlCatalogo != null) {
        controlCatalogo.agregarMaterialLista(mobiliario);
    }
}


    @FXML
    private void quitarMaterial() {
        if (cantidad > 0) {
            cantidad--;
            lblCantidad.setText(String.valueOf(cantidad));
            if (controlCatalogo != null) {
                controlCatalogo.agregarMaterialLista(mobiliario);
            }
        }
    }
}
