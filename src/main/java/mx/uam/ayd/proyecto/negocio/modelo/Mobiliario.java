package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * Entidad de Negocio Mobiliario
 * Se utiliza el método Tabla por Tipo (Table per Type, TPT)
*/
@Entity
public class Mobiliario extends Material {
    // Gracias al inheritance ya está automático el id del material

    // Se hacen los enum necesarios
    public enum TipoMobiliario{
        MAMPARASMETALICAS, MAMPARASDEMADERA, FIGURADECOROPLAST, LUCESLED, LETREROS, MESASDEMADERA, CARRITODEDULCES, BASTIDORES, NUMEORSMADERA, LONAS, TAPETES, CORTINAS, SERIESDELUCES, FLORES, FOLLAFUES
    }public enum TipoDano{
        TOTAL, PARCIAL, NINGUNO
    }

    // Atributos de la entidad
    @Enumerated(EnumType.STRING)
    private TipoMobiliario tipoMobiliario;
    private boolean estadoMobiliario;
    private float precioCompra;
    private float costoDanoTotal;
    private float costoDanoParcial;
    @Enumerated(EnumType.STRING)
    private TipoDano tipoDano;

    // Métodos de la cotización como getters y setters
    // ...
    public boolean getEstadoMobiliario(){
        return estadoMobiliario;
    }
    public float getPrecioCompra(){
        return precioCompra;
    }
    public float getCostoDanoTotal(){
        return costoDanoTotal;
    }
    public float getCostoDanoParcial(){
        return costoDanoParcial;
    }
    public TipoDano getTipoDano(){
        return tipoDano;
    }


    public void setEstadoMobiliario(boolean estadoMobiliario){
        this.estadoMobiliario = estadoMobiliario;
    }
    public void setPrecioCompra(float precioCompra){
        this.precioCompra = precioCompra;
    }
    public void setCostoDanoTotal(float costoDanoTotal){
        this.costoDanoTotal = costoDanoTotal;
    }
    public void setCostoDanoParcial(float costoDanoParcial){
        this.costoDanoParcial = costoDanoParcial;
    }
    public void setTipoDano(TipoDano tipoDano){
        this.tipoDano = tipoDano;
    }
}
