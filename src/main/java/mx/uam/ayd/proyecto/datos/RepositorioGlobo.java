package mx.uam.ayd.proyecto.datos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import mx.uam.ayd.proyecto.negocio.modelo.Globo;

/**
 * Repositorio para Globos
 */
public interface RepositorioGlobo extends CrudRepository <Globo, Long> {
    
    // Métodos para el repositorio
    // ...
    public List<Globo> findAll();

}
