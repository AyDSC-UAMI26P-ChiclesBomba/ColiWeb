package mx.uam.ayd.proyecto.datos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import mx.uam.ayd.proyecto.negocio.modelo.Material;

/**
 * Repositorio para Materiales
 */
public interface RepositorioMaterial extends CrudRepository <Material, Long> {
    
    // Métodos necesarios para el repositorio de Material
    // ...

    //Regresa una lista de todo el material existente
    public List<Material>  findAll();

    
}
