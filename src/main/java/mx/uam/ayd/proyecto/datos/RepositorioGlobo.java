package mx.uam.ayd.proyecto.datos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import javafx.scene.paint.Color;
import mx.uam.ayd.proyecto.negocio.modelo.Globo;
import mx.uam.ayd.proyecto.negocio.modelo.Globo.TipoGlobo;

/**
 * Repositorio para Globos
 */
public interface RepositorioGlobo extends CrudRepository <Globo, Long> {
    
    // Métodos para el repositorio
    // ...
    public List<Globo> findAll();

    public List<Globo> findByColorAndMedidaAndTipoGlobo(Color color, int medida, TipoGlobo tipoGlobo);
}
