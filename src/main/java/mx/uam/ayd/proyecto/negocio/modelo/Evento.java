package mx.uam.ayd.proyecto.negocio.modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

/**
 * Entidad de negocio Evento
 */
@Entity
public class Evento {
    // ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvento;

    // Se hacen los enum necesarios
    public enum TipoEvento {
        CONFIRMADO, BORRADOR, FINALIZADO
    }
    public enum EstadoEvento {
        BORRADOR, CONFIRMADO, FINALIZADO
    }
    public enum EstadoPago {
        // ...
    }

    // Atributos de la entidad
    @Enumerated(EnumType.STRING)
    private TipoEvento tipEvento;
    private LocalDate fecha;
    private LocalTime hora;
    private String lugar;
    private String referencias;
    private String direccion;
    private float totalPagado;
    @Enumerated(EnumType.STRING)
    private EstadoEvento estadoEvento;
    private String detalles;
    private String visualRecinto;
    @Enumerated(EnumType.STRING)
    private EstadoPago estadoPago;   
    private String clausulasExtras;
    private boolean contratoFirmado;
    
    /**
     * Permite la creación de la columna idCotizacion en la tabla Evento
     * Se crea cotizacion para dar esa relación a la entidad Cotizacion
     */
    @OneToOne
    @JoinColumn(name = "idCotizacion")
    private Cotizacion cotizacion;

    /**
     * Enlaza a Evento con sus pagos por medio del id establecido en Pago
     */
    @OneToMany(mappedBy = "evento", targetEntity = Pago.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pago> pagos;

    /**
     * Crea en Evento el idCliente para relacionarlo con Cliente
     */
    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;


    // Métodos de la cotización como getters y setters
    // ...
}
