package mx.uam.ayd.proyecto.datos;

import org.springframework.data.repository.CrudRepository;

import mx.uam.ayd.proyecto.negocio.modelo.Cliente;
import mx.uam.ayd.proyecto.negocio.modelo.Cotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;

/**
 * Respositorio para Clientes
 */
public interface RepositorioCliente extends CrudRepository <Cliente, Long> {
    
    // Inician los métodos del repositorio
    Cliente findByNombreAndNumTelefono(String nombre, String numTelefono);

    Cliente findByEventosContains(Evento evento);

    Cliente findByCotizacion(Cotizacion cotizacion);
}
