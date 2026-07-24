package mx.uam.ayd.proyecto.negocio;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.negocio.modelo.Material;
import mx.uam.ayd.proyecto.datos.RepositorioDetalleCotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Cotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.DetalleCotizacion;

@Service
public class ServicioDetalleCotizacion {
    
    RepositorioDetalleCotizacion repositorioDetalleCotizacion;

    @Autowired
    ServicioDetalleCotizacion(RepositorioDetalleCotizacion repositorioDetalleCotizacion){
        this.repositorioDetalleCotizacion = repositorioDetalleCotizacion;
    }

    public List<DetalleCotizacion> verificarListaMaterial(Cotizacion cotizacion){
        ArrayList<DetalleCotizacion> materialesSeleccionados = new ArrayList<>();
        for (DetalleCotizacion materialSeleccionado:repositorioDetalleCotizacion.findByCotizacion(cotizacion)){
            materialesSeleccionados.add(materialSeleccionado);
        }
        return materialesSeleccionados;
    }

    public List<DetalleCotizacion> agregaMaterialLista(Material materialSeleccionado){
        DetalleCotizacion materialLista = new DetalleCotizacion();
        materialLista.setMaterial(materialSeleccionado);
        repositorioDetalleCotizacion.save(materialSeleccionado);

        aumentarCantidadUno(materialLista);
        calcularCostoMaterialElegido(materialLista);
        validarTienePrecio(materialLista);

        repositorioDetalleCotizacion.save(materialLista);
        ArrayList<DetalleCotizacion> materialesSeleccionados = new ArrayList<>();
        for(DetalleCotizacion materialSeleccionadoL:repositorioDetalleCotizacion.findAll()){
            materialesSeleccionados.add(materialSeleccionadoL);
        }
        return materialesSeleccionados;
    }

    private void aumentarCantidadUno(DetalleCotizacion materialLista){
        materialLista.setCantidad(materialLista.getCantidad()+1);        
    }

    private void calcularCostoMaterialElegido(DetalleCotizacion materialLista){
        materialLista.setCosto(materialLista.getCantidad()*materialLista.getMaterial().getPrecio());
    }

    private boolean validarTienePrecio(DetalleCotizacion materialLista){
        return materialLista.getMaterial().getPrecio() != null;
    }

    public List<DetalleCotizacion> agregaCantidad(DetalleCotizacion materialLista){
        aumentarCantidadUno(materialLista);
        calcularCostoMaterialElegido(materialLista);

        repositorioDetalleCotizacion.save(materialLista);
        ArrayList<DetalleCotizacion> materialesSeleccionados = new ArrayList<>();
        for(DetalleCotizacion materialSeleccionadoL:repositorioDetalleCotizacion.findAll()){
            materialesSeleccionados.add(materialSeleccionadoL);
        }
        return materialesSeleccionados;
    }

    public List<DetalleCotizacion> diminuyeCantidad(DetalleCotizacion materialLista){
        quitarCantidadUno(materialLista);
        calcularCostoMaterialElegido(materialLista);

        repositorioDetalleCotizacion.save(materialLista);
        ArrayList<DetalleCotizacion> materialesSeleccionados = new ArrayList<>();
        for(DetalleCotizacion materialSeleccionadoL:repositorioDetalleCotizacion.findAll()){
            materialesSeleccionados.add(materialSeleccionadoL);
        }
        return materialesSeleccionados;
    }

    private void quitarCantidadUno(DetalleCotizacion materialLista){
        materialLista.setCantidad(materialLista.getCantidad()-1); 
    }

    public List<DetalleCotizacion> borraMaterialLista(DetalleCotizacion materialLista){
        repositorioDetalleCotizacion.delete(materialLista);
        ArrayList<DetalleCotizacion> materialesSeleccionados = new ArrayList<>();
        for(DetalleCotizacion materialSeleccionadoL:repositorioDetalleCotizacion.findAll()){
            materialesSeleccionados.add(materialSeleccionadoL);
        }
        return materialesSeleccionados;
    }

    public boolean validaPrecios(List<DetalleCotizacion> listaMaterialSeleccionado){
        return validarPreciosFaltantes(listaMaterialSeleccionado);
    }

    private boolean validarPreciosFaltantes(List<DetalleCotizacion> listaMaterialSeleccionado){
        for(DetalleCotizacion material : listaMaterialSeleccionado){
            if(material.getMaterial().getPrecio()==null){
                return false;
            }
        }
        return true;
    }

}
