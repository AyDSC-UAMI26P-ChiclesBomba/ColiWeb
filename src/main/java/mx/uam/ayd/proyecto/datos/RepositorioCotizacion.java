package mx.uam.ayd.proyecto.datos;

import org.springframework.data.repository.CrudRepository;

import mx.uam.ayd.proyecto.negocio.modelo.Cotizacion;

/**
 * Repositorio para Cotización
 */
public interface RepositorioCotizacion extends CrudRepository <Cotizacion, Long> {

    // Métodos del respositorio de Cotización
    
}
