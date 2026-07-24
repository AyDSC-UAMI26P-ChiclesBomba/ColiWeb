package mx.uam.ayd.proyecto.datos;

import org.springframework.data.repository.CrudRepository;

import mx.uam.ayd.proyecto.negocio.modelo.Cotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;

/**
 * Repositorio para Cotizaciones
 */
public interface RepositorioCotizacion extends CrudRepository <Cotizacion, Long> {

    Cotizacion findByEvento(Evento evento);
    // Métodos del respositorio de Cotización
    // ...

}
