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
        DetalleCotizacion nuevoMaterial = new DetalleCotizacion();
        nuevoMaterial.setMaterial(materialSeleccionado);
        repositorioDetalleCotizacion.save(materialSeleccionado);
        ArrayList<DetalleCotizacion> materialesSeleccionados = new ArrayList<>();
        for(DetalleCotizacion materialSeleccionadoL:repositorioDetalleCotizacion.findAll()){
            materialesSeleccionados.add(materialSeleccionadoL);
        }
        return materialesSeleccionados;
    }
}
