package mx.uam.ayd.proyecto.presentacion.cotizacionTotal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.negocio.ServicioCotizacion;
import mx.uam.ayd.proyecto.negocio.ServicioDetalleCotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Cliente;
import mx.uam.ayd.proyecto.negocio.modelo.Cotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Cotizacion.Tamano;
import mx.uam.ayd.proyecto.negocio.modelo.Evento.EstadoEvento;
import mx.uam.ayd.proyecto.presentacion.Contrato.ControlContrato;
import mx.uam.ayd.proyecto.presentacion.calendario.ControlCalendario;
import mx.uam.ayd.proyecto.presentacion.catalogo.ControlCatalogo;
import mx.uam.ayd.proyecto.negocio.modelo.DetalleCotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;

@Component
public class ControlCotizacionTotal {
    
    private final ServicioDetalleCotizacion servicioDetalleCotizacion;
    private final ServicioCotizacion servicioCotizacion;
    private final VentanaCotizacionTotal ventanaCotizacionTotal;
    private final ControlCatalogo controlCatalogo;
    private final ControlCalendario controlCalendario;
    private final ControlContrato contrato;

    @Autowired
    public ControlCotizacionTotal(ServicioCotizacion servicioCotizacion, ServicioDetalleCotizacion servicioDetalleCotizacion, VentanaCotizacionTotal ventanaCotizacionTotal, @Lazy ControlCatalogo controlCatalogo, ControlCalendario controlCalendario, ControlContrato contrato){
        this.servicioCotizacion  = servicioCotizacion;
        this.servicioDetalleCotizacion = servicioDetalleCotizacion;
        this.ventanaCotizacionTotal = ventanaCotizacionTotal;
        this.controlCatalogo = controlCatalogo;
        this.controlCalendario = controlCalendario;
        this.contrato = contrato;
    }

    @PostConstruct
    public void init() {
        ventanaCotizacionTotal.setControlCotizacionTotal(this);
    }

    public void iniciaCotizacionTotal(
    List<DetalleCotizacion> listaMaterialSeleccionado, 
    Cotizacion cotizacion)
{
    Evento evento = servicioCotizacion.recuperaEvento(cotizacion);

    ventanaCotizacionTotal.muestraResumenCotizacion(listaMaterialSeleccionado,cotizacion);

    ventanaCotizacionTotal.muestraDetallesCotizacion(
        servicioCotizacion.recuperaCliente(cotizacion),
        evento
    );
}
    public void mostrarDetallesCotizacion(Cotizacion cotizacion){
        Evento evento = servicioCotizacion.recuperaEvento(cotizacion);
        Cliente cliente = servicioCotizacion.recuperaCliente(cotizacion);
        ventanaCotizacionTotal.muestraDetallesCotizacion(cliente, evento);
    }

    public void actualizaPrecio(float precio, DetalleCotizacion materialLista){
        ventanaCotizacionTotal.muestraListaMaterialActualizada(servicioDetalleCotizacion.actualizaPrecio(precio, materialLista));
    }

    public void actualizaCostosExtra(Float transporte, Float materialPersonalizado, Float materialCliente, Tamano tamano, String detalles, List<DetalleCotizacion> listaMaterialSeleccionado, Cotizacion cotizacion, Evento evento){
    Cotizacion cotizacionActualizada = servicioCotizacion.actualizaDetallesCotizacion(transporte, materialPersonalizado, materialCliente,tamano, detalles, listaMaterialSeleccionado, cotizacion, evento);
    ventanaCotizacionTotal.muestraCotizacionTotal(cotizacionActualizada);
}

    public void copiarTotal(Cotizacion cotizacion){
        ventanaCotizacionTotal.copiaTotalCotizacion(servicioCotizacion.copiaTotal(cotizacion));
    }

    public void deshabilitarVolverCatalogo(Cotizacion cotizacion, boolean estado){
        ventanaCotizacionTotal.deshabilitaVolverCatalogo(servicioCotizacion.aprobarCotizacion(cotizacion, estado));
    }
    public void iniciaContrato(List<DetalleCotizacion> listaMaterialSeleccionado, Evento evento, Cotizacion cotizacion){
        contrato.iniciaContrato(evento);
    }

    public void validarPrecios(List<DetalleCotizacion> listaMaterialSeleccionado){
        boolean hayPreciosFaltantes = servicioDetalleCotizacion.validaPrecios(listaMaterialSeleccionado);
        if (hayPreciosFaltantes) {
            ventanaCotizacionTotal.deshabilitaContinuarCotizacion();
        } else {
            ventanaCotizacionTotal.deshabilitaGuardarBorradorYConsultarPrecio();
        }
    }
/** 
    public void consultarPrecios(List<DetalleCotizacion> listaMaterialSeleccionado){

    }
    */

    public void volverCatalogo(Cotizacion cotizacion){
        ventanaCotizacionTotal.cierra();
        controlCatalogo.inicia(cotizacion);
    }
    public void volverCalendario(){
        ventanaCotizacionTotal.cierra();
        controlCalendario.iniciaCalendario();
    }
    public void borrarCotizacion(Cotizacion cotizacion){
        ventanaCotizacionTotal.muestraMensajeBorradoExito(servicioCotizacion.borraCotizazion(cotizacion));
    }

}
