package mx.uam.ayd.proyecto.negocio;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.RepositorioMobiliario;
import mx.uam.ayd.proyecto.negocio.modelo.Mobiliario;

@Service
public class ServicioMobiliario {
    
    RepositorioMobiliario repositorioMobiliario;

    @Autowired
    ServicioMobiliario(RepositorioMobiliario repositorioMobiliario){
        this.repositorioMobiliario = repositorioMobiliario;
    }

    public List<Mobiliario> recuperaTodoMobiliario(){
        ArrayList<Mobiliario> mobiliarios = new ArrayList<>();
        for(Mobiliario mobiliario:repositorioMobiliario.findAll()){
            mobiliarios.add(mobiliario);
        }
        return mobiliarios;
    }


    
}
