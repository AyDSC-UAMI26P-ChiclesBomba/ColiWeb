package mx.uam.ayd.proyecto.negocio;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.RepositorioDetalleCotizacion;


@Service
public class ServicioDetalleCotizacion {
    
    RepositorioDetalleCotizacion repositorioDetalleCotizacion;

    @Autowired
    ServicioDetalleCotizacion(RepositorioDetalleCotizacion repositorioDetalleCotizacion){
        this.repositorioDetalleCotizacion = repositorioDetalleCotizacion;
    }

}
