package mx.uam.ayd.proyecto.datos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import mx.uam.ayd.proyecto.negocio.modelo.MaterialDecorativo;

/**
 * Repositorio para Materiales Decorativos
 */
public interface RepositorioMaterialDecorativo extends CrudRepository <MaterialDecorativo, Long> {
    // Métodos para el repositorio
    List<MaterialDecorativo>  findAll();
}
