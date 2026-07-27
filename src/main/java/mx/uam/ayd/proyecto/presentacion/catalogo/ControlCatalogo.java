package mx.uam.ayd.proyecto.presentacion.catalogo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javafx.scene.paint.Color;
import mx.uam.ayd.proyecto.negocio.ServicioComestible;
import mx.uam.ayd.proyecto.negocio.ServicioDetalleCotizacion;
import mx.uam.ayd.proyecto.negocio.ServicioGlobo;
import mx.uam.ayd.proyecto.negocio.ServicioMaterial;
import mx.uam.ayd.proyecto.negocio.ServicioMaterialDecorativo;
import mx.uam.ayd.proyecto.negocio.ServicioMobiliario;
import mx.uam.ayd.proyecto.negocio.modelo.Cotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.DetalleCotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Globo.TipoGlobo;
import mx.uam.ayd.proyecto.presentacion.cotizacionTotal.ControlCotizacionTotal;
import mx.uam.ayd.proyecto.negocio.modelo.Material;
import mx.uam.ayd.proyecto.negocio.modelo.Mobiliario;

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
    private ControlCotizacionTotal controlCotizacionTotal;


    private Cotizacion cotizacion;

    @Autowired
    public ControlCatalogo(ServicioMaterial servicioMaterial, ServicioGlobo servicioGlobo, ServicioMobiliario servicioMobiliario, ServicioMaterialDecorativo servicioMaterialDecorativo, ServicioComestible servicioComestible, ServicioDetalleCotizacion servicioDetalleCotizacion,VentanaCatalogo ventanaCatalogo) {

        this.servicioMaterial = servicioMaterial;
        this.servicioGlobo = servicioGlobo;
        this.servicioMobiliario = servicioMobiliario;
        this.servicioMaterialDecorativo = servicioMaterialDecorativo;
        this.servicioComestible = servicioComestible;
        this.servicioDetalleCotizacion = servicioDetalleCotizacion;
        this.ventanaCatalogo = ventanaCatalogo;
    }

    @Autowired
    public void setControlCotizacionTotal(@Lazy ControlCotizacionTotal controlCotizacionTotal) {
        this.controlCotizacionTotal = controlCotizacionTotal;
    }

    @PostConstruct
    public void init() {
        ventanaCatalogo.setControlCatalogo(this);
    }

    public void inicia(Cotizacion cotizacion) {
        this.cotizacion = cotizacion;
        ventanaCatalogo.muestraCatalogo(servicioMaterial.recuperaTodoMaterial());
    }

    /**
     * Agrega un nuevo material llamando al servicio de negocio.
     */
    public void agregarMaterialLista(Material materialSeleccionado) {
        if (materialSeleccionado instanceof Mobiliario) {

        Mobiliario mobiliarioSeleccionado = (Mobiliario) materialSeleccionado;
            if (servicioMobiliario.verificarNoDanoTotal(mobiliarioSeleccionado)) { 
                ventanaCatalogo.muestraMensajeNoAgregarMobiliarioDanoTotal();
                return;
            }
        }
        ventanaCatalogo.muestraListaMaterial(servicioDetalleCotizacion.agregaMaterialLista(materialSeleccionado, this.cotizacion));
    }

    public void recuperarTodoMaterial() {
        ventanaCatalogo.muestraCatalogo(servicioMaterial.recuperaTodoMaterial());
    }

    public void recuperarTodoGlobos() {
        ventanaCatalogo.muestraCatalogoGlobos(servicioGlobo.recuperaTodoGlobo());
    }

    public void recuperarTodoMaterialDecorativo() {
        ventanaCatalogo.muestraCatalogoDecoraciones(servicioMaterialDecorativo.recuperaTodoMaterialDecorativo());
    }

    public void recuperarTodoMobiliario() {
        ventanaCatalogo.muestraCatalogoMobiliario(servicioMobiliario.recuperaTodoMobiliario());
    }

    public void recuperarTodoComestibles() {
        ventanaCatalogo.muestraCatalogoComestible(servicioComestible.recuperaTodoComestible());
    }

    public void verificaListaMaterial(Cotizacion cotizacion){
        ventanaCatalogo.muestraListaMaterial(servicioDetalleCotizacion.verificarListaMaterial(cotizacion));
    }

    public void aumentarMaterial(DetalleCotizacion materialLista){
        ventanaCatalogo.muestraMaterialLista(servicioDetalleCotizacion.aumentaCantidad(materialLista));
    }

    public void disminuirMaterial(DetalleCotizacion detalleCotizacion){
        ventanaCatalogo.muestraMaterialLista(servicioDetalleCotizacion.disminuyeCantidad(detalleCotizacion));
    }

    public void borrarMaterialLista(DetalleCotizacion detalleCotizacion){
        ventanaCatalogo.muestraMaterialLista(servicioDetalleCotizacion.borraMaterialLista(detalleCotizacion));
    }

    public void iniciarCotizacion(){
        List<DetalleCotizacion> listaSeleccionada = servicioDetalleCotizacion.verificarListaMaterial(this.cotizacion);
        System.out.println("Lista del servicio: " + listaSeleccionada.size());
        ventanaCatalogo.cierra();
        controlCotizacionTotal.iniciaCotizacionTotal(listaSeleccionada, this.cotizacion);
    }

} 