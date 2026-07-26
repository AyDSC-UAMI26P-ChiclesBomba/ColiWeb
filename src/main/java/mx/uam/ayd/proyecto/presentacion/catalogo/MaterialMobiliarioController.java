package mx.uam.ayd.proyecto.presentacion.catalogo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import mx.uam.ayd.proyecto.negocio.modelo.Mobiliario;

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
    private Button btnAgregar;

    private Mobiliario mobiliario;

    private ControlCatalogo controlCatalogo;

    /**
     * Inicializa la tarjeta del mobiliario.
     */
    public void setMaterial(Mobiliario mobiliario, ControlCatalogo controlCatalogo) {

        this.mobiliario = mobiliario;
        this.controlCatalogo = controlCatalogo;

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
        } else {
            lblTipoDano.setText("NINGUNO");
        }

        // Imagen
        if (mobiliario.getImagen() != null && !mobiliario.getImagen().isBlank()) {
            try {
                Image image = new Image(getClass().getResourceAsStream(mobiliario.getImagen()));
                imgMaterial.setImage(image);
            } catch (Exception e) {
                // Ignorar si la imagen no existe.
            }
        }
    }

    /**
     * Agrega el mobiliario a la lista de cotización.
     */
    @FXML
    private void agregarMaterial() {

        if (controlCatalogo != null) {
            controlCatalogo.agregarMaterialLista(mobiliario);
        }

    }

}