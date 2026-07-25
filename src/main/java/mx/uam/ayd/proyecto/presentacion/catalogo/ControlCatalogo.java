package mx.uam.ayd.proyecto.presentacion.catalogo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.negocio.ServicioComestible;
import mx.uam.ayd.proyecto.negocio.ServicioDetalleCotizacion;
import mx.uam.ayd.proyecto.negocio.ServicioGlobo;
import mx.uam.ayd.proyecto.negocio.ServicioMaterial;
import mx.uam.ayd.proyecto.negocio.ServicioMaterialDecorativo;
import mx.uam.ayd.proyecto.negocio.ServicioMobiliario;
import mx.uam.ayd.proyecto.negocio.modelo.Cotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.DetalleCotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Material;

@Component
public class ControlCatalogo {

    private static final Logger log = LoggerFactory.getLogger(ControlCatalogo.class);

    private final ServicioMaterial servicioMaterial;
    private final ServicioGlobo servicioGlobo;
    private final ServicioMobiliario servicioMobiliario;
    private final ServicioMaterialDecorativo servicioMaterialDecorativo;
    private final ServicioComestible servicioComestible;
    private final ServicioDetalleCotizacion servicioDetalleCotizacion;

    private final VentanaCatalogo ventanaCatalogo;

    @Autowired
    public ControlCatalogo(
            ServicioMaterial servicioMaterial,
            ServicioGlobo servicioGlobo,
            ServicioMobiliario servicioMobiliario,
            ServicioMaterialDecorativo servicioMaterialDecorativo,
            ServicioComestible servicioComestible,
            ServicioDetalleCotizacion servicioDetalleCotizacion,
            VentanaCatalogo ventanaCatalogo) {

        this.servicioMaterial = servicioMaterial;
        this.servicioGlobo = servicioGlobo;
        this.servicioMobiliario = servicioMobiliario;
        this.servicioMaterialDecorativo = servicioMaterialDecorativo;
        this.servicioComestible = servicioComestible;
        this.servicioDetalleCotizacion = servicioDetalleCotizacion;
        this.ventanaCatalogo = ventanaCatalogo;
    }

    @PostConstruct
    public void init() {
        ventanaCatalogo.setControlCatalogo(this);
    }

    public void inicia(Cotizacion cotizacion) {
        log.info("Abriendo catálogo");
        ventanaCatalogo.muestra(servicioMaterial.recuperaTodoMaterial());
    }

    /**
     * Agrega un nuevo material llamando al servicio de negocio.
     */
    public void agregarMaterialLista(Material material) {
        List<DetalleCotizacion> listaActualizada = servicioDetalleCotizacion.agregaMaterialLista(material);
        ventanaCatalogo.actualizarListaResumen(listaActualizada);
    }

    public void mostrarTodos() {
        ventanaCatalogo.actualizarCatalogo(servicioMaterial.recuperaTodoMaterial());
    }

    public void mostrarGlobos() {
        ventanaCatalogo.actualizarCatalogo(servicioGlobo.recuperaTodoGlobo());
    }

    public void mostrarDecoraciones() {
        ventanaCatalogo.actualizarCatalogo(servicioMaterialDecorativo.recuperaTodoMaterialDecorativo());
    }

    public void mostrarMobiliario() {
        ventanaCatalogo.actualizarCatalogo(servicioMobiliario.recuperaTodoMobiliario());
    }

    public void mostrarComestibles() {
        ventanaCatalogo.actualizarCatalogo(servicioComestible.recuperaTodoComestible());
    }
}