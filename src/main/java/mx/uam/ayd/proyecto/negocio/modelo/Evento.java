package mx.uam.ayd.proyecto.negocio.modelo;

import java.sql.Date;
import java.sql.Time;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Entidad de negocio Evento
 */
@Entity
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idEvento;

    private enum tipoEvento {
        CONFIRMADO, BORRADOR, FINALIZADO
    }
    private Date fecha;
    private Time hora;
    private String lugar;
    private String referencias;
    private String direccion;
    private float totalPagado;
    private enum estadoEvento {
        BORRADOR, CONFIRMADO, FINALIZADO
    }
    private String detalles;
    private String visualRecinto;
    // Pagos
    private enum estadoPago {

    }
    // Contrato
    private String clausulasExtras;
    private boolean contratoFirmado;
    
    /**
     * Permite la creación de la tabla idCotizacion en la tabla Evento
     * Se crea cotizacion para dar esa relación a la entidad Cotizacion
     */
    @ManyToOne
    @JoinColumn(name = "idCotizacion")
    private Cotizacion cotizacion;

}
