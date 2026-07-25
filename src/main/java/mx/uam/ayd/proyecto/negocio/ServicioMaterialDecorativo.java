package mx.uam.ayd.proyecto.negocio;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.RepositorioMaterialDecorativo;
import mx.uam.ayd.proyecto.negocio.modelo.MaterialDecorativo;

@Service
public class ServicioMaterialDecorativo {
    
    RepositorioMaterialDecorativo repositorioMaterialDecorativo;

    @Autowired
    ServicioMaterialDecorativo(RepositorioMaterialDecorativo repositorioMaterialDecorativo){
        this.repositorioMaterialDecorativo = repositorioMaterialDecorativo;
    }

    public List<MaterialDecorativo> recuperaTodoMaterialDecorativo(){
        ArrayList<MaterialDecorativo> materialesDecorativos = new ArrayList<>();
        for (MaterialDecorativo materialDecorativo:repositorioMaterialDecorativo.findAll()){
            materialesDecorativos.add(materialDecorativo);
        }
        return materialesDecorativos;
    }
}
