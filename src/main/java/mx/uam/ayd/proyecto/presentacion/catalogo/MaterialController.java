package mx.uam.ayd.proyecto.presentacion.catalogo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import mx.uam.ayd.proyecto.negocio.modelo.Material;

public class MaterialController {

    @FXML
    private ImageView imgMaterial;

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblPrecio;

    @FXML
    private Button btnAgregar;

    private Material material;

    private ControlCatalogo controlCatalogo;

    /**
     * Inicializa la tarjeta del material.
     */
    public void setMaterial(Material material, ControlCatalogo controlCatalogo) {

        this.material = material;
        this.controlCatalogo = controlCatalogo;

        lblNombre.setText(material.getNombre());

        if (material.getPrecio() != null) {
            lblPrecio.setText("$" + material.getPrecio());
        } else {
            lblPrecio.setText("Sin precio");
        }

        // Imagen (si existe)
        if (material.getImagen() != null && !material.getImagen().isBlank()) {
            try {
                Image image = new Image(getClass().getResourceAsStream(material.getImagen()));
                imgMaterial.setImage(image);
            } catch (Exception e) {
                // Si no encuentra la imagen simplemente no la muestra.
            }
        }
    }

    /**
     * Agrega el material a la lista de cotización.
     */
    @FXML
    private void agregarMaterial() {

        if (controlCatalogo != null) {
            controlCatalogo.agregarMaterialLista(material);
        }

    }

}