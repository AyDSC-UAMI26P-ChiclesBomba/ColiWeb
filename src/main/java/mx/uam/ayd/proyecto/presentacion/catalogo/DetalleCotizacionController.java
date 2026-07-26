package mx.uam.ayd.proyecto.presentacion.catalogo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import mx.uam.ayd.proyecto.negocio.modelo.DetalleCotizacion;

public class DetalleCotizacionController {

    @FXML
    private Label IdlNombre;

    @FXML
    private Label IdlCantidad;

    @FXML
    private Label IdlCosto;

    @FXML
    private Button btnMas;

    @FXML
    private Button btnMenos;

    @FXML
    private Button btnEliminar;

    private DetalleCotizacion detalle;
    private ControlCatalogo controlCatalogo;

    public void setDetalle(DetalleCotizacion detalle, ControlCatalogo controlCatalogo) {

        this.detalle = detalle;
        this.controlCatalogo = controlCatalogo;

        IdlNombre.setText(detalle.getMaterial().getNombre());
        IdlCantidad.setText(String.valueOf(detalle.getCantidad()));
        IdlCosto.setText("$" + detalle.getCosto());
    }

    @FXML
    private void aumentar() {
        controlCatalogo.aumentarMaterial(detalle);
    }

    @FXML
    private void disminuir() {
        controlCatalogo.disminuirMaterial(detalle);
    }

    @FXML
    private void eliminar() {
        controlCatalogo.borrarMaterialLista(detalle);
    }
}