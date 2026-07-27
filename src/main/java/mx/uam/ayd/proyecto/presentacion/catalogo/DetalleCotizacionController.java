package mx.uam.ayd.proyecto.presentacion.catalogo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import mx.uam.ayd.proyecto.negocio.modelo.DetalleCotizacion;

/**
 * Controlador de la tarjeta que representa un material dentro de la lista de
 * materiales seleccionados para una cotización
 */

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
    private Button btnBorrar;

    private DetalleCotizacion detalle;
    private ControlCatalogo controlCatalogo;

    /**
     * Asigna el detalle de cotización que será mostrado en la tarjeta y
     * actualiza la información visual correspondiente.
     *
     * @param detalle detalle de cotización que contiene el material, la cantidad
     *                y el costo.
     * @param controlCatalogo controlador del catálogo encargado de procesar las
     *                        acciones del usuario.
     */
    public void setDetalle(DetalleCotizacion detalle, ControlCatalogo controlCatalogo) {

        this.detalle = detalle;
        this.controlCatalogo = controlCatalogo;

        IdlNombre.setText(detalle.getMaterial().getNombre());
        IdlCantidad.setText(String.valueOf(detalle.getCantidad()));
        IdlCosto.setText("$" + detalle.getCosto());
    }

    /**
     * Incrementa la cantidad del material asociado al detalle de cotización.
     */
    @FXML
    private void aumentar() {
        if (detalle != null) {
        controlCatalogo.aumentarMaterial(detalle);
    }
    }
    /**
     * Disminuye la cantidad del material asociado al detalle de cotización
     */
    @FXML
    private void disminuir() {
        if (detalle != null && detalle.getCantidad() > 1) {
        controlCatalogo.disminuirMaterial(detalle);
    }
    }
    /**
     * Elimina el material asociado del listado de materiales de la cotización
     */
    @FXML
    private void borrar() {
        if (detalle != null) {
        controlCatalogo.borrarMaterialLista(detalle);
    }
    }
}