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
    public int getCantidad(){
        return cantidad;
    }
    public float getCosto(){
        return costo;
    }
    public Material getMaterial(){
        return material;
    }
    public boolean getPreciosCompletos(){
        return preciosCompletos;
    }
      
    public void setCantidad(int cantidad){
        this.cantidad = cantidad;
    }
    public void setCosto(float costo){
        this.costo = costo;
    }
    public void setPreciosCompletos(boolean preciosCompletos){
        this.preciosCompletos = preciosCompletos;
    }
    
}

