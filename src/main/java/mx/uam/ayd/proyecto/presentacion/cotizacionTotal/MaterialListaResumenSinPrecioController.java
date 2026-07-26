package mx.uam.ayd.proyecto.presentacion.cotizacionTotal;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import mx.uam.ayd.proyecto.negocio.modelo.DetalleCotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Material;

public class MaterialListaResumenSinPrecioController {

    @FXML
    private Label IdlNombre;

    @FXML
    private Label IdlCantidad;

    @FXML 
    private TextField IdlPrecio;

    @FXML 
    private Label IdlCosto;

    @FXML
    private ImageView imgMaterial;

    private DetalleCotizacion detalleCotizacion;
    private Material material;
    private ControlCotizacionTotal controlCotizacionTotal;

    /**
     * Muestra la información del material y configura el listener para validar el precio ingresado.
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

        // Imagen del Material (si aplica)
        if (material != null && imgMaterial != null && material.getImagen() != null && !material.getImagen().isBlank()) {
            try {
                Image image = new Image(getClass().getResourceAsStream(material.getImagen()));
                imgMaterial.setImage(image);
            } catch (Exception e) {
                // Si la imagen no se encuentra, la ignora
            }
        }

        // Configurar la validación del TextField para recibir solo números
        configurarValidacionPrecio();
    }

    /**
     * Agrega un ChangeListener al TextField de precio para validar mientras el usuario escribe.
     */
    private void configurarValidacionPrecio() {
        if (IdlPrecio != null) {
            IdlPrecio.textProperty().addListener((observable, oldValue, newValue) -> {
                validarYProcesarPrecio(newValue);
            });
        }
    }

    /**
     * Valida si el texto ingresado es un número flotante válido mayor o igual a cero.
     * Si no es válido, marca el campo con un borde rojo y muestra un mensaje de error.
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
     * Cambia el estilo del TextField a borde rojo si hay error o lo restaura si es válido.
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
     * Calcula y muestra el costo total (Cantidad * Precio unitario).
     */
    private void actualizarCostoTotal(float precio) {
        if (detalleCotizacion != null && IdlCosto != null) {
            float costoTotal = detalleCotizacion.getCantidad() * precio;
            IdlCosto.setText(String.format("$ %.2f", costoTotal));
        }
    }

    /**
     * Limpia el campo de costo total en caso de entrada inválida.
     */
    private void limpiarCostoTotal() {
        if (IdlCosto != null) {
            IdlCosto.setText("$ 0.00");
        }
    }
}