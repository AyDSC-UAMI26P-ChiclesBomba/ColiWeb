package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class DetalleCotizacion {
    
    // ==========================================
    // 1. ATRIBUTOS Y LLAVES PRIMARIAS
    // ==========================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    private int cantidad;
    private Float costo;
    private boolean preciosCompletos;

    // ==========================================
    // 2. RELACIONES CON OTRAS TABLAS (CORREGIDO)
    // ==========================================
    @ManyToOne
    @JoinColumn(name="idCotizacion")
    private Cotizacion cotizacion;

    @ManyToOne
    @JoinColumn(name="idMaterial")
    private Material material;

    // ==========================================
    // 3. GETTERS Y SETTERS
    // ==========================================
    
    public Long getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Long idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getCantidad(){
        return cantidad;
    }
    
    public void setCantidad(int cantidad){
        this.cantidad = cantidad;
    }

    public Float getCosto(){
        return costo;
    }
    
    public void setCosto(float costo){
        this.costo = costo;
    }

    public boolean getPreciosCompletos(){
        return preciosCompletos;
    }

    public void setPreciosCompletos(boolean preciosCompletos){
        this.preciosCompletos = preciosCompletos;
    }

    public Material getMaterial(){
        return material;
    }
    
    public void setMaterial(Material material){
        this.material = material;
    }

    public Cotizacion getCotizacion() {
        return cotizacion;
    }
    
    public void setCotizacion(Cotizacion cotizacion){
        this.cotizacion = cotizacion;
    }
}