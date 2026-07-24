package mx.uam.ayd.proyecto.negocio.modelo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
 * Entidad de negocio Cotizacion
 */
@Entity
public class Cotizacion {
    // ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCotizacion;

    public enum Tamano{
        PEQUENO, MEDIANO, GRANDE
    }

    // Atributos de la entidad
    @Enumerated(EnumType.STRING)
    private Tamano tamano;
    @Column(nullable = true)
    private float anticipo;
    @Column(nullable = true)
    private float total;
    @Column(nullable = true)
    private float totalMaterial;
    @Column(nullable = true)
    private float transporte;
    @Column(nullable = true)
    private float extra;
    @Column(nullable = true)
    private float materialPersonalizado;
    @Column(nullable = true)
    private float materialCliente;
    @Column(nullable = true)
    private float manoDeObra;
    @Column(nullable = true)
    private float ganancia;
    @Column(nullable = true)
    private float consumibles;
    @Column(nullable = true)
    private boolean aprobada;

    /**
     * Indica la relación que tiene con Evento
     * Mediante mappedBy evitamos que la tabla de Cotización genere algun idEvento y de esta manera -
     * sólo hace referencia mediante el objeto cotizacion creado en Evento
     */
    @OneToOne(mappedBy = "cotizacion", targetEntity = Evento.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private Evento evento;
    
    // Relación con DetalleCotizacion (no guarda columna)
    @OneToMany(mappedBy = "cotizacion", targetEntity = DetalleCotizacion.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List <DetalleCotizacion> detalles;

    // Relación con Cliente (sí guarda columna)
    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;


    // ----------- MÉTODOS -----------

    // getters
    public List<DetalleCotizacion> getDetalles(){
        List<DetalleCotizacion> detalles = new ArrayList<>();
        return detalles;
    }public Long getId(){
        return idCotizacion;
    }
    public float getTotal(){
        return total;
    }

    // setters

}
