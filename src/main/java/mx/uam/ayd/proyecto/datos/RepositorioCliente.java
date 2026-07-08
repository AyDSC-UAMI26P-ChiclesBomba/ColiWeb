package mx.uam.ayd.proyecto.datos;

import org.springframework.data.repository.CrudRepository;

import mx.uam.ayd.proyecto.negocio.modelo.Cliente;

/**
 * Respositorio para Clientes
 */
public interface RepositorioCliente extends CrudRepository <Cliente, Long> {
    
    // Inician los métodos del repositorio
    
}
