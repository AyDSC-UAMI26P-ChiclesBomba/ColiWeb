package mx.uam.ayd.proyecto.negocio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.RepositorioCotizacion;

@Service
public class ServicioCotizacion {
    private RepositorioCotizacion repositorioCotizacion;

    @Autowired
    public ServicioCotizacion(RepositorioCotizacion repositorioCotizacion){
        this.repositorioCotizacion = repositorioCotizacion;
    }

        
}
