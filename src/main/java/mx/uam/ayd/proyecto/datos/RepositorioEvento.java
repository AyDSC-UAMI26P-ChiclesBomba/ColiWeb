package mx.uam.ayd.proyecto.datos;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import mx.uam.ayd.proyecto.negocio.modelo.Evento;

/**
 * Repositorio para Eventos
 */
public interface RepositorioEvento extends CrudRepository <Evento, Long> {
    
    // Métodos necesarios para el repositorio
    //Find by Mes
    @Query("SELECT e FROM Evento e WHERE MONTH(e.fecha) = :#{#mes.value}")
    List<Evento> findByMes(@Param("mes") Month mes);

    Evento findByFecha(LocalDate fecha);
}
