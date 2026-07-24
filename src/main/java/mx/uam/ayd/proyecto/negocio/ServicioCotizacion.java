package mx.uam.ayd.proyecto.negocio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.RepositorioCliente;
import mx.uam.ayd.proyecto.datos.RepositorioCotizacion;
import mx.uam.ayd.proyecto.datos.RepositorioEvento;



@Service
public class ServicioCotizacion {
    
    RepositorioCotizacion repositorioCotizacion;
    RepositorioEvento repositorioEvento;
    RepositorioCliente repositorioCliente;

    @Autowired
    public ServicioCotizacion(RepositorioCotizacion repositorioCotizacion){
        this.repositorioCotizacion = repositorioCotizacion;
    }

    
    
}
