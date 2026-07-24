package mx.uam.ayd.proyecto.negocio;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.RepositorioDetalleCotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Cotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.DetalleCotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Material;



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
        repositorioDetalleCotizacion.save(materialLista);

        aumentarCantidadUno(materialLista);
        calcularCostoMaterialElegido(materialLista);
        validarTienePrecio(materialLista);
        repositorioDetalleCotizacion.save(materialLista);

        ArrayList<DetalleCotizacion> listaMaterialSeleccionado = new ArrayList<>();
        for (DetalleCotizacion detalle : repositorioDetalleCotizacion.findAll()) {
            listaMaterialSeleccionado.add(detalle);
        }
        return listaMaterialSeleccionado;
    }

    private void aumentarCantidadUno(DetalleCotizacion materialLista){
        materialLista.setCantidad(materialLista.getCantidad()+1);        
    }
     private void calcularCostoMaterialElegido(DetalleCotizacion materialLista){
        materialLista.setCosto(materialLista.getCantidad()*materialLista.getMaterial().getPrecio());
    }
    private void validarTienePrecio(DetalleCotizacion materialLista){
        materialLista.setPreciosCompletos(materialLista.getMaterial().getPrecio() != null);
    }

    public List<DetalleCotizacion> aumentaCantidad(DetalleCotizacion materialLista){
        materialLista.getCantidad();
        aumentarCantidadUno(materialLista);
        calcularCostoMaterialElegido(materialLista);
        repositorioDetalleCotizacion.save(materialLista);

        ArrayList<DetalleCotizacion> listaMaterialSeleccionado = new ArrayList<>();
        for (DetalleCotizacion detalle : repositorioDetalleCotizacion.findAll()) {
            listaMaterialSeleccionado.add(detalle);
        }
        return listaMaterialSeleccionado;
    }

    public List<DetalleCotizacion> disminuyeCantidad(DetalleCotizacion materialLista){
        materialLista.getCantidad();
        quitarCantidadUno(materialLista);
        calcularCostoMaterialElegido(materialLista);
        repositorioDetalleCotizacion.save(materialLista);

        ArrayList<DetalleCotizacion> listaMaterialSeleccionado = new ArrayList<>();
        for (DetalleCotizacion detalle : repositorioDetalleCotizacion.findAll()) {
            listaMaterialSeleccionado.add(detalle);
        }
        return listaMaterialSeleccionado;
    }

    private void quitarCantidadUno(DetalleCotizacion materialLista){
        materialLista.setCantidad(materialLista.getCantidad()-1);  
    }

    public boolean borraMaterialLista(DetalleCotizacion materialLista){
        if(materialLista == null){
            System.err.println("El material no existe en la lista");
            return false;
        }
        try {
            repositorioDetalleCotizacion.delete(materialLista);
            return true;
        } catch (Exception e) {
            System.err.println("No se encontró el material en la lista. "+e.getMessage());
            return false;
        }
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

    public boolean actualizaPrecio(float precio, DetalleCotizacion materialLista){
        try{
            Material material = materialLista.getMaterial();
            material.setPrecio(precio);
            float nuevoCosto = materialLista.getCantidad() * precio;
            materialLista.setCosto(nuevoCosto);

            repositorioDetalleCotizacion.save(materialLista);

            return true;
        } catch(Exception e){
            System.err.println("Error al actualizar el precio: " + e.getMessage());
            return false;
        }
    }



}
