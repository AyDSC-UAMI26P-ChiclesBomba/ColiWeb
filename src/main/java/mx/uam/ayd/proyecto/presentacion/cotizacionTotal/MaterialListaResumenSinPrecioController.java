package mx.uam.ayd.proyecto.presentacion.cotizacionTotal;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import mx.uam.ayd.proyecto.negocio.modelo.DetalleCotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Material;

/**
 * Controlador de la tarjeta que muestra un material dentro del resumen
 * de la cotización cuando aún no tiene un precio asignado.
 */

public class MaterialListaResumenSinPrecioController {

    @FXML
    private Label IdlNombre;

    @FXML
    private Label IdlCantidad;

    @FXML 
    private TextField IdlPrecio;

    @FXML 
    private Label IdlCosto;


    private DetalleCotizacion detalleCotizacion;
    private Material material;
    private ControlCotizacionTotal controlCotizacionTotal;

    /**
     * Inicializa la tarjeta con la información del material y configura
     * la validación del precio ingresado por el usuario.
     * @param material material que será mostrado.
     * @param controlCotizacionTotal controlador principal de la ventana de
     *                               cotización total.
     * @param detalleCotizacion detalle de la cotización asociado al material.
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

        // Cargar precio inicial si existe en el material
        if (material != null && material.getPrecio() != null && IdlPrecio != null) {
            IdlPrecio.setText(String.valueOf(material.getPrecio()));
            actualizarCostoTotal(material.getPrecio());
        }
        configurarValidacionPrecio();
    }

    /**
     * Configura un listener sobre el campo de precio para validar el valor
     * cada vez que el usuario modifica su contenido.
     */
    private void configurarValidacionPrecio() {
        if (IdlPrecio != null) {
            IdlPrecio.textProperty().addListener((observable, oldValue, newValue) -> {
                validarYProcesarPrecio(newValue);
            });
        }
    }

    /**
     * Valida el precio capturado por el usuario.
     * @param texto texto ingresado por el usuario.
     * @return {@code true} si el precio es válido; {@code false} en caso contrario.
     */
    private boolean validarYProcesarPrecio(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            marcarError(true);
            limpiarCostoTotal();
            return false;
        }

        try {
            float precioIngresado = Float.parseFloat(texto.trim());

            if (precioIngresado < 0) {
                marcarError(true);
                limpiarCostoTotal();
                return false;
            }

            // Es un número válido: Quitar marca de error
            marcarError(false);

            // Recalcular Costo Total localmente
            actualizarCostoTotal(precioIngresado);

            // Notificar al control sobre la actualización del precio
            if (controlCotizacionTotal != null && detalleCotizacion != null) {
                controlCotizacionTotal.actualizaPrecio(precioIngresado, detalleCotizacion);
            }

            return true;

        } catch (NumberFormatException e) {
            // El texto no se puede parsear como un número flotante válido
            marcarError(true);
            limpiarCostoTotal();
            return false;
        }
    }

    /**
     * Marca o elimina el estado de error del campo de precio.
     *
     * @param conError {@code true} para mostrar un borde rojo indicando
     *                 un error de validación; {@code false} para restaurar
     *                 el estilo original.
     */
    private void marcarError(boolean conError) {
        if (IdlPrecio != null) {
            if (conError) {
                IdlPrecio.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            } else {
                IdlPrecio.setStyle(null); // Restaura el estilo predeterminado
            }
        }
    }

    /**
     * Calcula y muestra el costo total del material.
     * @param precio precio unitario del material.
     */
    private void actualizarCostoTotal(float precio) {
        if (detalleCotizacion != null && IdlCosto != null) {
            float costoTotal = detalleCotizacion.getCantidad() * precio;
            IdlCosto.setText(String.format("$ %.2f", costoTotal));
        }
    }

    /**
     * Limpia el valor mostrado en el costo total cuando el precio ingresado
     * no es válido.
     */
    private void limpiarCostoTotal() {
        if (IdlCosto != null) {
            IdlCosto.setText("$ 0.00");
        }
    }
}