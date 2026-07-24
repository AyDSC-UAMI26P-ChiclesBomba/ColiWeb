package mx.uam.ayd.proyecto.datos;

import java.util.List;


import org.springframework.data.repository.CrudRepository;

import mx.uam.ayd.proyecto.datos.RepositorioComestible;
import mx.uam.ayd.proyecto.negocio.modelo.Comestible;

/**
 * Repositorio para Comestibles
 */
public interface RepositorioComestible extends CrudRepository <Comestible, Long> {

    // Métodos para el repositorio
    List<Comestible>  findAll();
}
