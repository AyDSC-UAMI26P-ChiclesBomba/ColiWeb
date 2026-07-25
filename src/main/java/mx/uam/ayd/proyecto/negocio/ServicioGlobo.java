package mx.uam.ayd.proyecto.negocio;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javafx.scene.paint.Color;
import mx.uam.ayd.proyecto.datos.RepositorioGlobo;
import mx.uam.ayd.proyecto.negocio.modelo.Globo;
import mx.uam.ayd.proyecto.negocio.modelo.Globo.TipoGlobo;


@Service
public class ServicioGlobo {
    
    RepositorioGlobo repositorioGlobo;

    @Autowired
    ServicioGlobo(RepositorioGlobo repositorioGlobo){
        this.repositorioGlobo = repositorioGlobo;
    }

    public List<Globo> recuperaTodoGlobo(){
        ArrayList<Globo> globos = new ArrayList<>();
        for(Globo globo:repositorioGlobo.findAll()){
            globos.add(globo);
        }
        return globos;
    }

    public List<Globo> recuperaGlobosFiltro(Color color, int medida, TipoGlobo tipoGlobo){
        ArrayList<Globo> globosfiltro = new ArrayList<>();
        for(Globo globo:repositorioGlobo.findByColorAndMedidaAndTipoGlobo(color, medida, tipoGlobo)){
            globosfiltro.add(globo);
        }
        return globosfiltro;
    }
}
