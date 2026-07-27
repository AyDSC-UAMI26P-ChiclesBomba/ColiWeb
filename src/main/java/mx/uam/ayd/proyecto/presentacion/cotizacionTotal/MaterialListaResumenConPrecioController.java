package mx.uam.ayd.proyecto.presentacion.cotizacionTotal;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import mx.uam.ayd.proyecto.negocio.modelo.DetalleCotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Material;

public class MaterialListaResumenConPrecioController {

    @FXML
    private Label IdlNombre;

    @FXML
    private Label IdlCantidad;

    @FXML 
    private Label IdlPrecio;

    @FXML 
    private Label IdlCosto;


    private DetalleCotizacion detalleCotizacion;
    private Material material;
    private ControlCotizacionTotal controlCotizacionTotal;

    /**
     * Muestra la información del material y el detalle de la cotización.
     * Solo presenta la información en pantalla sin modificar ningún dato.
     */
    public void setMaterialListaResumen(Material material, ControlCotizacionTotal controlCotizacionTotal, DetalleCotizacion detalleCotizacion) {

        this.detalleCotizacion = detalleCotizacion;
        this.material = material;
        this.controlCotizacionTotal = controlCotizacionTotal;

        // Nombre del Material
        if (material != null && IdlNombre != null) {
            IdlNombre.setText(material.getNombre());
        }

        // Cantidad de la cotización
        if (detalleCotizacion != null && IdlCantidad != null) {
            IdlCantidad.setText(String.valueOf(detalleCotizacion.getCantidad()));
        }

        // Precio unitario
        if (material != null && material.getPrecio() != null && IdlPrecio != null) {
            IdlPrecio.setText(String.format("$ %.2f", material.getPrecio()));
        }

        // Costo Total (Cantidad * Precio unitario)
        if (material != null && material.getPrecio() != null && detalleCotizacion != null && IdlCosto != null) {
            float costoTotal = detalleCotizacion.getCantidad() * material.getPrecio();
            IdlCosto.setText(String.format("$ %.2f", costoTotal));
        }

    }
}