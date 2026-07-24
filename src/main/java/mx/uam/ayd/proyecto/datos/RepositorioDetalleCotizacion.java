package mx.uam.ayd.proyecto.datos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import mx.uam.ayd.proyecto.negocio.modelo.Cotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.DetalleCotizacion;

/**
 * Repositorio para Detalles de Cotización
 */
public interface RepositorioDetalleCotizacion extends CrudRepository <DetalleCotizacion, Long>{
    
    // Métodos para el repositorio de DetalleCotizacion
    // ...
    List<DetalleCotizacion> findByCotizacion(Cotizacion cotizacion);
}
