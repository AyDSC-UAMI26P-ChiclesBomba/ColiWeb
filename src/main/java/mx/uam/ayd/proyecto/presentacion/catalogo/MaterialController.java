package mx.uam.ayd.proyecto.presentacion.catalogo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private Label lblCantidad;

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnQuitar;

    private Material material;

    private int cantidad = 0;

    private ControlCatalogo controlCatalogo;

    /**
     * Carga la información del material en la tarjeta.
     */
    public void setMaterial(Material material) {

        this.material = material;

        lblNombre.setText(material.getNombre());

        if(material.getPrecio() != null){
            lblPrecio.setText("$" + material.getPrecio());
        }else{
            lblPrecio.setText("Sin precio");
        }

        /*
         * La imagen la dejaremos para el final,
         * cuando revisemos cómo se almacenan.
         */

        // if(material.getImagen() != null){
        //     Image imagen = new Image(
        //         getClass().getResourceAsStream(material.getImagen()));
        //     imgMaterial.setImage(imagen);
        // }

        lblCantidad.setText("0");

    }

    @FXML
    private void agregarMaterial() {
        cantidad++;
        lblCantidad.setText(String.valueOf(cantidad));
        if (controlCatalogo != null) {
        controlCatalogo.agregarMaterialLista(material);
    }
}

    @FXML
    private void quitarMaterial() {
        if (cantidad > 0) {
            cantidad--;
            lblCantidad.setText(String.valueOf(cantidad));
            if (controlCatalogo != null) {
                controlCatalogo.agregarMaterialLista(material);
            }
        }
    }

}