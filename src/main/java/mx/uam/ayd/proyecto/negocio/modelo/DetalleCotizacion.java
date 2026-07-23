package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Entidad de negocio DetalleCotizacion
 */
@Entity
public class DetalleCotizacion {
    // ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    // Atributos de la entidad
    private int cantidad;
    private float costo;
    private boolean preciosCompletos;

    // Relación con Cotizacion, donde se guarda la columna "idCotizacion"
    @ManyToOne
    @JoinColumn(name="idCotizacion")
    private Cotizacion cotizacion;

    // Relación con el material al que se hace referencia (sí guarda columna)
    @ManyToOne
    @JoinColumn(name="idMaterial")
    private Material material;

    // Métodos de la cotización como getters y setters
    // ...
    
    public void setMaterial(Material materia){
        this.material = material;
    }
}
