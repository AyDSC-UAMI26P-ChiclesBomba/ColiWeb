package mx.uam.ayd.proyecto.negocio;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.RepositorioComestible;
import mx.uam.ayd.proyecto.negocio.modelo.Comestible;

@Service
public class ServicioComestible {
    
    RepositorioComestible repositorioComestible;

    @Autowired
    ServicioComestible(RepositorioComestible repositorioComestible){
        this.repositorioComestible = repositorioComestible;
    }

    public List<Comestible> recuperaTodoComestible(){
        ArrayList<Comestible> comestibles = new ArrayList<>();
        for (Comestible comestible:repositorioComestible.findAll()){
            comestibles.add(comestible);
        }
        return comestibles;
    }
}
