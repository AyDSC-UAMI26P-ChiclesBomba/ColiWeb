package mx.uam.ayd.proyecto.presentacion.catalogo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    /**
     * Construye el controlador del catálogo e inyecta las dependencias necesarias.
     *
     * @param servicioMaterial servicio para administrar materiales.
     * @param servicioGlobo servicio para administrar globos.
     * @param servicioMobiliario servicio para administrar mobiliario.
     * @param servicioMaterialDecorativo servicio para administrar materiales decorativos.
     * @param servicioComestible servicio para administrar materiales comestibles.
     * @param servicioDetalleCotizacion servicio para administrar los detalles de la cotización.
     * @param ventanaCatalogo ventana asociada al catálogo.
     */

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

    /**
     * Inyecta el controlador encargado de la pantalla de cotización total.
     *
     * @param controlCotizacionTotal controlador de la cotización total.
     */

    @Autowired
    public void setControlCotizacionTotal(@Lazy ControlCotizacionTotal controlCotizacionTotal) {
        this.controlCotizacionTotal = controlCotizacionTotal;
    }

    /**
     * Inicializa el controlador después de que Spring ha inyectado todas
     * sus dependencias y enlaza la ventana con este controlador.
     */
    @PostConstruct
    public void init() {
        ventanaCatalogo.setControlCatalogo(this);
    }

    /**
     * Inicia la ventana del catálogo para la cotización indicada y carga
     * todos los materiales disponibles.
     *
     * @param cotizacion cotización que será modificada
     */
    public void inicia(Cotizacion cotizacion) {
        this.cotizacion = cotizacion;
        ventanaCatalogo.muestraCatalogo(servicioMaterial.recuperaTodoMaterial());
    }

    /**
     * Agrega un nuevo material a la lista de material seleccionado
     * @param materialSeleccionado material seleccionado por el usuario
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
    /**
     * Recupera todos los materiales y los muestra en el catálogo
     */
    public void recuperarTodoMaterial() {
        ventanaCatalogo.muestraCatalogo(servicioMaterial.recuperaTodoMaterial());
    }
    /**
     * Recupera todos los globos y los muestra
     */
    public void recuperarTodoGlobos() {
        ventanaCatalogo.muestraCatalogoGlobos(servicioGlobo.recuperaTodoGlobo());
    }
    /**
     * Recupera todos los materiales decorativos y los muestra en el catálogo
     */
    public void recuperarTodoMaterialDecorativo() {
        ventanaCatalogo.muestraCatalogoDecoraciones(servicioMaterialDecorativo.recuperaTodoMaterialDecorativo());
    }
    /**
     * Recupera todo el mobiliario y los muestra en el catalogo
     */
    public void recuperarTodoMobiliario() {
        ventanaCatalogo.muestraCatalogoMobiliario(servicioMobiliario.recuperaTodoMobiliario());
    }
    /**
     * Recupera los comestibles y los muestra en el catalogo
     */
    public void recuperarTodoComestibles() {
        ventanaCatalogo.muestraCatalogoComestible(servicioComestible.recuperaTodoComestible());
    }
    /**
     * Recupera los materiales asociados a una cotización y actualiza la lista mostrada.
     *
     * @param cotizacion cotización cuyos materiales serán recuperados.
     */
    public void verificaListaMaterial(Cotizacion cotizacion){
        ventanaCatalogo.muestraListaMaterial(servicioDetalleCotizacion.verificarListaMaterial(cotizacion));
    }
    /**
     * Incrementa la cantidad de un material dentro de la lista de material seleccionado
     *
     * @param materialLista detalle de cotización cuyo material será incrementado.
     */
    public void aumentarMaterial(DetalleCotizacion materialLista){
        ventanaCatalogo.muestraMaterialLista(servicioDetalleCotizacion.aumentaCantidad(materialLista));
    }
    /**
     * Disminuye la cantidad de un material dentro de la lista de material seleccionado
     *
     * @param detalleCotizacion detalle de cotización cuyo material será decrementado.
     */
    public void disminuirMaterial(DetalleCotizacion detalleCotizacion){
        ventanaCatalogo.muestraMaterialLista(servicioDetalleCotizacion.disminuyeCantidad(detalleCotizacion));
    }
    /**
     * Elimina un material de la lista materiales seleccionados
     *
     * @param detalleCotizacion material de cotización que será eliminado.
     */
    public void borrarMaterialLista(DetalleCotizacion detalleCotizacion){
        ventanaCatalogo.muestraMaterialLista(servicioDetalleCotizacion.borraMaterialLista(detalleCotizacion));
    }
    /**
     * Finaliza la selección de materiales, cierra la ventana del catálogo
     * y abre la pantalla de cotización total con los materiales seleccionados.
     */
    public void iniciarCotizacion(){
        List<DetalleCotizacion> listaSeleccionada = servicioDetalleCotizacion.verificarListaMaterial(this.cotizacion);
        System.out.println("Lista del servicio: " + listaSeleccionada.size());
        ventanaCatalogo.cierra();
        controlCotizacionTotal.iniciaCotizacionTotal(listaSeleccionada, this.cotizacion);
    }

} 