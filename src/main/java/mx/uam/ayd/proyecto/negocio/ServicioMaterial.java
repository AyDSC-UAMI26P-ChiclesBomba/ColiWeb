package mx.uam.ayd.proyecto.negocio;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.RepositorioMaterial;
import mx.uam.ayd.proyecto.negocio.modelo.Material;

@Service
public class ServicioMaterial {
    
    RepositorioMaterial repositorioMaterial;

    @Autowired
    ServicioMaterial(RepositorioMaterial repositorioMaterial){
        this.repositorioMaterial = repositorioMaterial;
    }
    
    public List<Material> recuperaTodoMaterial(){
        ArrayList<Material> materiales = new ArrayList<>();
        for(Material material:repositorioMaterial.findAll()){
            materiales.add(material);
        }
        return materiales;
    }
}
